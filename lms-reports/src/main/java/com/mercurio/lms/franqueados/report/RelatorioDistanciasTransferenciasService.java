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
 * @spring.bean id="lms.vendas.emitirRelatorioDistanciasTransferenciasService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioDistanciasTransferencias.jasper"
 */
public class RelatorioDistanciasTransferenciasService extends ReportServiceSupport {
	
	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		String sql = getQueryDefault(parameters);
		JRReportDataObject jr = executeQuery(sql, new Object[]{});
		final List list = getJdbcTemplate().queryForList(getQueryDefault(parameters));
		
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
		
		parametersReport.put("dataSource", new JRBeanCollectionDataSource(list));
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	protected String getQueryDefault(Map parameters){
		String query = " SELECT DISTINCT "
					+"   FD.SG_FILIAL AS SG_FILIAL, "
					+"   P.NM_FANTASIA AS NM_FANTASIA, "
					+"   FF.NR_DISTANCIA AS NR_DISTANCIA "
					+"  FROM FLUXO_FILIAL FF, FILIAL FD, FILIAL FO, PESSOA P "
					+"  WHERE FF.ID_FILIAL_ORIGEM = FO.ID_FILIAL "
					+"  AND FF.ID_FILIAL_DESTINO = FD.ID_FILIAL "
					+"  AND FD.ID_FILIAL = P.ID_PESSOA "
					+"  AND FF.ID_SERVICO IS NULL "
					+"  AND FD.CD_FILIAL IS NOT NULL "
					+"  AND FF.ID_FILIAL_ORIGEM = :franquia "
					+"  AND TO_DATE(':competencia','dd/MM/yyyy') BETWEEN FF.DT_VIGENCIA_INICIAL AND FF.DT_VIGENCIA_FINAL "
					+"  ORDER BY FD.SG_FILIAL ";

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
