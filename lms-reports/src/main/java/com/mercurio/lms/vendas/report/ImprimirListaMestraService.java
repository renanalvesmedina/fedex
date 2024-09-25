package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author luisfco
 *
 * @spring.bean id="lms.vendas.imprimirListaMestraService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/imprimirListaMestra.jasper"
 */
public class ImprimirListaMestraService extends ReportServiceSupport {
	
	private PessoaService pessoaService; 
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap tfm = (TypedFlatMap) parameters;
        SqlTemplate sql = getSqlTemplate(tfm);
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
        return jr; 
	} 
	 
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) {
		
		Long idFilial = parameters.getLong("filial.idFilial");
		String sgFilial = parameters.getString("sgFilial");
		String nmFilial = parameters.getString("nmFantasia");
		String siglaNomeFilial = parameters.getString("siglaNomeFilial");
 		Long   idRegional =	parameters.getLong("regional.idRegional");
 		String sgRegional = parameters.getString("sgRegional");
		String nmRegional = parameters.getString("dsRegional");
		String siglaDescricaoRegional = parameters.getString("siglaDescricao");
		YearMonthDay dtReferencia = parameters.getYearMonthDay("dtReferencia");
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("P.ID_PESSOA", "ID_CLIENTE");
		sql.addProjection("P.NM_PESSOA", "NM_CLIENTE");
		
		sql.addProjection("P.TP_IDENTIFICACAO");
		sql.addProjection("P.NR_IDENTIFICACAO");
		
		sql.addProjection("VPCE.NR_VERSAO_PCE");
		sql.addProjection("VPCE.DT_VIGENCIA_INICIAL"); 
		sql.addProjection("VPCE.DT_VIGENCIA_FINAL");
		sql.addProjection("FIL.SG_FILIAL");
		sql.addProjection("PESSOA_FILIAL.NM_FANTASIA", "NM_FILIAL");
		sql.addProjection("RE.SG_REGIONAL");
		sql.addProjection("RE.DS_REGIONAL");
		sql.addProjection("RF.ID_REGIONAL_FILIAL");
		
		sql.addFrom("PESSOA", "P");
		sql.addFrom("CLIENTE", "CLI");
		sql.addFrom("VERSAO_PCE", "VPCE");
		sql.addFrom("FILIAL", "FIL");
		sql.addFrom("PESSOA", "PESSOA_FILIAL");
		sql.addFrom("REGIONAL_FILIAL", "RF");
		sql.addFrom("REGIONAL", "RE");
		
		sql.addJoin("P.ID_PESSOA", "CLI.ID_CLIENTE");
		sql.addJoin("VPCE.ID_CLIENTE", "CLI.ID_CLIENTE");
		sql.addJoin("CLI.ID_FILIAL_ATENDE_COMERCIAL", "FIL.ID_FILIAL");
		sql.addJoin("RF.ID_FILIAL", "FIL.ID_FILIAL");
		sql.addJoin("FIL.ID_FILIAL", "PESSOA_FILIAL.ID_PESSOA");
		sql.addJoin("RF.ID_REGIONAL", "RE.ID_REGIONAL");
		
		sql.addCriteria("FIL.ID_FILIAL", "=", idFilial, Long.class);
		sql.addCriteria("RE.ID_REGIONAL", "=", idRegional, Long.class);
		
		YearMonthDay currentDate = JTDateTimeUtils.getDataAtual();
		sql.addCustomCriteria("RF.DT_VIGENCIA_INICIAL <= ? AND RF.DT_VIGENCIA_FINAL >= ?");
		sql.addCriteriaValue(currentDate);
		sql.addCriteriaValue(currentDate);
		
	
		if (dtReferencia != null) {
			sql.addCustomCriteria("(? BETWEEN VPCE.DT_VIGENCIA_INICIAL AND VPCE.DT_VIGENCIA_FINAL)", dtReferencia, YearMonthDay.class);
		}
		 
		sql.addOrderBy("NM_CLIENTE");
		sql.addOrderBy("NR_IDENTIFICACAO");
		sql.addOrderBy("NR_VERSAO_PCE");
		
		
		if (StringUtils.isNotBlank(sgFilial) && StringUtils.isNotBlank(nmFilial) )
			sql.addFilterSummary("filial", siglaNomeFilial);
		if (StringUtils.isNotBlank(sgRegional) && StringUtils.isNotBlank(nmRegional) )		
			sql.addFilterSummary("regional", siglaDescricaoRegional);
		if (dtReferencia != null)
			sql.addFilterSummary("dataReferencia", parameters.getYearMonthDay("dtReferencia")) ;
		
		return sql;
	}	
	
    public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("P.TP_IDENTIFICACAO", "DM_TIPO_IDENTIFICACAO"); 
    } 

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}
