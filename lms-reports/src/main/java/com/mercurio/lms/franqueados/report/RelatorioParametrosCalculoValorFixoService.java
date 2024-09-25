package com.mercurio.lms.franqueados.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioParametrosCalculoValorFixoService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioParametrosCalculoValorFixo.jasper"
 */
public class RelatorioParametrosCalculoValorFixoService extends ReportServiceSupport {

	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		String sql = getQueryDefault(parameters);
		JRReportDataObject jr = executeQuery(sql, new Object[]{});
		final List listDescontos = getJdbcTemplate().queryForList(getQueryDefault(parameters));
		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", getFilterSummary(parameters));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null)
			parametersReport.put("idFranquia", parameters.get("idFilial"));
		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
			parametersReport.put("competencia", dtCompetencia);
		}
		
		parametersReport.put("dataSource", new JRBeanCollectionDataSource(listDescontos));
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	protected String getQueryDefault(Map parameters){
		String query = " SELECT FI.SG_FILIAL, "
		+" 		NVL(M.NM_MUNICIPIO, ' Geral') AS MUNICIPIO, "
		+" 		DECODE(P.NR_IDENTIFICACAO, NULL, ' Geral', P.NR_IDENTIFICACAO || ' ' || P.NM_PESSOA) AS CLIENTE,  "
		+" 		FX.VL_COLETA,  "
		+" 		FX.VL_ENTREGA,  "
		+" 		FX.DT_VIGENCIA_INICIAL,  "
		+" 		FX.DT_VIGENCIA_FINAL   "
		+" 	FROM   FIXO_FRQ FX,  "
		+" 		FILIAL FI,  "
		+" 		PESSOA P,  "
		+" 		MUNICIPIO M "
		+" WHERE  FX.ID_FRANQUIA = FI.ID_FILIAL "
		+" AND    FX.ID_CLIENTE = P.ID_PESSOA (+) "
		+" AND    FX.ID_MUNICIPIO = M.ID_MUNICIPIO (+) "
		+" AND    TO_DATE(':competencia','dd/MM/yyyy') BETWEEN FX.DT_VIGENCIA_INICIAL AND FX.DT_VIGENCIA_FINAL "
		+" AND    FX.ID_FRANQUIA = :franquia "
		+" ORDER BY 1, 2, 3 ";

		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
    		Long idFranquia = (Long) parameters.get("idFilial");
    		query = query.replaceAll(":franquia", idFranquia.toString());
    	}
		
		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");

			String competencia = dtCompetencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
			query = query.replaceAll(":competencia", competencia);
		}

		return query;
	}
	
	
	protected String getFilterSummary(Map parameters) {
		SqlTemplate sql = createSqlTemplate();

		if (parameters.containsKey("dsFranquia") && parameters.get("dsFranquia") != null) {
			String dsFranquia = (String) parameters.get("dsFranquia");
			sql.addFilterSummary("franquia", dsFranquia);
		}

		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");

			String competencia = dtCompetencia.toString(DateTimeFormat.forPattern("MM/yyyy"));

			sql.addFilterSummary("competencia", competencia);
		}

		return sql.getFilterSummary();
	}
    
}
