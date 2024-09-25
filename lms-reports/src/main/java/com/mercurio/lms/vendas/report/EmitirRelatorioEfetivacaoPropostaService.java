package com.mercurio.lms.vendas.report;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.SimulacaoService;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioEfetivacaoPropostaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelatorioEfetivacaoProposta.jasper"
 */
public class EmitirRelatorioEfetivacaoPropostaService extends ReportServiceSupport {
	
	private SimulacaoService simulacaoService;
	private static final String REPORT_NAME = "relatorio-efetivacao";
	
    /**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio");
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);
		jr.setParameters(parametersReport);
		return jr;
	}

	public File executeCsv(TypedFlatMap parameters,	File generateOutputDir) {		
		List<Map<String, Object>> lista = getSimulacaoService().findRelatorioEfetivacaoProposta(parameters);
		
		return FileUtils.generateReportFile(lista, generateOutputDir, REPORT_NAME);
	}
	
    private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception { 
    	SqlTemplate sql = createSqlTemplate();

    	// SELECT
    	sql.addProjection("P.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
    	sql.addProjection("P.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
    	sql.addProjection("P.NM_PESSOA", "NM_PESSOA");
    	sql.addProjection("P.NM_FANTASIA", "NM_FANTASIA");
    	sql.addProjection("F.SG_FILIAL || ' ' || S.NR_SIMULACAO", "NM_PROPOSTA");
    	sql.addProjection("to_char(S.DT_EFETIVACAO,'DD/MM/YYYY')","DT_EFETIVACAO");
    	    	
    	// FROM
    	sql.addFrom("SIMULACAO", "S");
    	sql.addFrom("PESSOA", "P");
    	sql.addFrom("FILIAL", "F");

    	// JOIN
    	sql.addJoin("S.ID_FILIAL", "F.ID_FILIAL");
    	sql.addJoin("S.ID_CLIENTE", "P.ID_PESSOA");
    	
    	YearMonthDay dtInicial = parameters.getYearMonthDay("dtDataInicial");
    	YearMonthDay dtFinal = parameters.getYearMonthDay("dtDataFinal");
         
    	// CRITERIA
    	sql.addCriteria("S.BL_EFETIVADA", "=", "S");
    	sql.addCriteria("S.DT_EFETIVACAO ", ">=", dtInicial );
    	sql.addCriteria("S.DT_EFETIVACAO ", "<=", dtFinal );
    	
    	sql.addFilterSummary("dataEfetivacaoInicial",  JTFormatUtils.format(dtInicial));
    	sql.addFilterSummary("dataEfetivacaoFinal",  JTFormatUtils.format(dtFinal));

    	sql.addOrderBy("S.DT_EFETIVACAO");

    	return sql;
    }

	public SimulacaoService getSimulacaoService() {
		return simulacaoService;
	}

	public void setSimulacaoService(SimulacaoService simulacaoService) {
		this.simulacaoService = simulacaoService;
	}
}
