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
 * @spring.bean id="lms.vendas.emitirRelatorioParametrosCalculoGeneralidadesService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioParametrosCalculoGeneralidades.jasper"
 */
public class RelatorioParametrosCalculoGeneralidadesService extends
		ReportServiceSupport {

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
		String query = "SELECT VI18N(PP.NM_PARCELA_PRECO_I) AS DS_GENERALIDADE,"
				+ " DECODE(GF.TP_FRETE,'CR','CIF Rec','CE','CIF Exp','FR','FOB Rec','FOB Exp') AS TP_FRETE,"
				+ " GF.PC_PARTICIPACAO,"
				+ " GF.DT_VIGENCIA_INICIAL,"
				+ " GF.DT_VIGENCIA_FINAL"
				+ " FROM GENERALIDADE_FRQ GF,"
				+ " PARCELA_PRECO PP"
				+ " WHERE GF.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO"
 				+ " AND TO_DATE(':competencia','dd/MM/yyyy') BETWEEN DT_VIGENCIA_INICIAL AND DT_VIGENCIA_FINAL"
				+ " ORDER BY 1, 2";

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
