package com.mercurio.lms.franqueados.report;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioParametrosCalculoLimiteService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioParametrosCalculoLimite.jasper"
 */
public class RelatorioParametrosCalculoLimiteService extends ReportServiceSupport {

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
		String query = "SELECT TP_FRETE, "
				+"        NR_KM, "
				+"        TP_OPERACAO, "
				+"        PC_LIMITE, "
				+"        DT_VIGENCIA_INICIAL, "
				+"        DT_VIGENCIA_FINAL "
				+" FROM   LIMITE_PARTICIPACAO_FRQ "
				+" WHERE  TO_DATE(':competencia','dd/MM/yyyy') BETWEEN DT_VIGENCIA_INICIAL AND DT_VIGENCIA_FINAL "
				+" ORDER BY TP_FRETE, "
				+"         NR_KM, "
				+"         TP_OPERACAO ";

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
	
	public String getTipoFrete(String siglaFrete){
		if (StringUtils.isBlank(siglaFrete)) {
			return null;
		}
		return getDomainValueService().findDomainValueByValue("DM_TIPO_FRETE_FRQ", siglaFrete).getDescription().getValue();
	}
    
	public String getTipoOperacao(String siglaOperacao){
		if (StringUtils.isBlank(siglaOperacao)) {
			return null;
		}
		return getDomainValueService().findDomainValueByValue("DM_TP_OPERACAO_FRQ", siglaOperacao).getDescription().getValue();
	}
	
}
