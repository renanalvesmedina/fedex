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
 * @spring.bean id="lms.vendas.emitirRelatorioContabilService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioFinanceiro.jasper"
 */
public class RelatorioFinanceiroService extends ReportServiceSupport {
	
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
		StringBuilder query = new StringBuilder()
		.append("SELECT SG_FILIAL, ")
		.append("    VL_TOTAL_DOC, ")
		.append("    VL_PARTICIPACAO + VL_SERVICO_ADICIONAL + VL_IRE + VL_SERV_REEMB as VL_PARTICIPACAO, ")
		.append("    VL_CREDITO_DIVERSO AS VL_CREDITO_DIVERSO, ")
		.append("    VL_RECALCULO, ")
		.append("    VL_BDM, ")
		.append("    VL_DEBITO_DIVERSO, ")
		.append("    VL_OVER_60, ")
		.append("    VL_INDENIZACAO, ")
		.append("    VL_RECALCULO + VL_BDM + VL_DEBITO_DIVERSO + VL_OVER_60 + VL_INDENIZACAO AS VL_TOTAL_DEBITO_DIVERSO, ")
		.append("    VL_PARTICIPACAO + VL_CREDITO_DIVERSO + VL_SERVICO_ADICIONAL + VL_IRE + VL_SERV_REEMB + VL_RECALCULO + VL_BDM + VL_DEBITO_DIVERSO + VL_OVER_60 + VL_INDENIZACAO AS VL_A_PAGAR, ")
		.append("    ROUND((VL_PARTICIPACAO + VL_CREDITO_DIVERSO + VL_SERVICO_ADICIONAL + VL_IRE + VL_SERV_REEMB + VL_RECALCULO + VL_BDM + VL_DEBITO_DIVERSO + VL_OVER_60 + VL_INDENIZACAO) / 2, 2) AS VL_DIA15, ")
		.append("    (VL_PARTICIPACAO + VL_CREDITO_DIVERSO + VL_SERVICO_ADICIONAL + VL_IRE + VL_SERV_REEMB + VL_RECALCULO + VL_BDM + VL_DEBITO_DIVERSO + VL_OVER_60 + VL_INDENIZACAO) - ROUND((VL_PARTICIPACAO + VL_CREDITO_DIVERSO + VL_SERVICO_ADICIONAL + VL_IRE + VL_SERV_REEMB + VL_RECALCULO + VL_BDM + VL_DEBITO_DIVERSO + VL_OVER_60 + VL_INDENIZACAO) / 2, 2) AS VL_DIA30 ")
		.append("from ( ")
		.append("    SELECT X.SG_FILIAL, ")
		.append("       X.VL_TOTAL_DOC, ")
		.append("       X.VL_PARTICIPACAO, ")
		.append("       NVL((SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
		.append("            FROM LANCAMENTO_FRQ LF, CONTA_CONTABIL_FRQ CCF ")
		.append("            WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
		.append("            AND CCF.TP_CONTA_CONTABIL = 'IR' ")
		.append("            AND LF.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND LF.DT_COMPETENCIA = X.DT_COMPETENCIA ")
		.append("            AND LF.TP_SITUACAO_PENDENCIA = 'A'), 0) AS VL_IRE, ")
		.append("       NVL((SELECT SUM(RBQ.VL_CTE + RBQ.VL_TONELADA) ")
		.append("            FROM REEMBARQUE_DOC_SERV_FRQ RBQ ")
		.append("            WHERE RBQ.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND RBQ.DT_COMPETENCIA = X.DT_COMPETENCIA), 0) AS VL_SERV_REEMB, ")
		.append("       NVL((SELECT SUM(DSF.VL_PARTICIPACAO) ")
		.append("            FROM DOCTO_SERVICO_FRQ DSF ")
		.append("            WHERE DSF.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND DSF.DT_COMPETENCIA = X.DT_COMPETENCIA ")
		.append("            AND DSF.TP_FRETE = 'SE' ")
		.append("            AND DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL), 0) AS VL_SERVICO_ADICIONAL, ")
		.append("       NVL((SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1)) ")
		.append("            FROM LANCAMENTO_FRQ LF, CONTA_CONTABIL_FRQ CCF ")
		.append("            WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
		.append("            AND CCF.TP_CONTA_CONTABIL IN ('CD','CA') ")
		.append("            AND LF.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND LF.DT_COMPETENCIA = X.DT_COMPETENCIA ")
		.append("            AND LF.TP_SITUACAO_PENDENCIA = 'A'), 0) AS VL_CREDITO_DIVERSO, ")
		.append("       NVL((SELECT SUM(DSF.VL_DIFERENCA_PARTICIPACAO) * -1 ")
		.append("            FROM DOCTO_SERVICO_FRQ DSF ")
		.append("            WHERE DSF.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND DSF.DT_COMPETENCIA = X.DT_COMPETENCIA ")
		.append("            AND DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NOT NULL), 0) AS VL_RECALCULO, ")
		.append("       NVL((SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
		.append("            FROM LANCAMENTO_FRQ LF, CONTA_CONTABIL_FRQ CCF ")
		.append("            WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
		.append("            AND CCF.TP_CONTA_CONTABIL = 'BD' ")
		.append("            AND LF.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND LF.DT_COMPETENCIA = X.DT_COMPETENCIA ")
		.append("            AND LF.TP_SITUACAO_PENDENCIA = 'A'), 0) AS VL_BDM, ")
		.append("       NVL((SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
		.append("            FROM LANCAMENTO_FRQ LF, CONTA_CONTABIL_FRQ CCF ")
		.append("            WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
		.append("            AND CCF.TP_CONTA_CONTABIL IN ('DD','DA') ")
		.append("            AND LF.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND LF.DT_COMPETENCIA = X.DT_COMPETENCIA ")
		.append("            AND LF.TP_SITUACAO_PENDENCIA = 'A'), 0) AS VL_DEBITO_DIVERSO, ")
		.append("       NVL((SELECT SUM(LF.VL_LANCAMENTO  * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1) ) ")
		.append("            FROM LANCAMENTO_FRQ LF, CONTA_CONTABIL_FRQ CCF ")
		.append("            WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
		.append("            AND CCF.TP_CONTA_CONTABIL = 'OV' ")
		.append("            AND LF.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND LF.DT_COMPETENCIA = X.DT_COMPETENCIA ")
		.append("            AND LF.TP_SITUACAO_PENDENCIA = 'A'), 0) AS VL_OVER_60, ")
		.append("       NVL((SELECT SUM(LF.VL_LANCAMENTO * DECODE(CCF.TP_LANCAMENTO, 'C', 1, -1)) ")
		.append("            FROM LANCAMENTO_FRQ LF, CONTA_CONTABIL_FRQ CCF ")
		.append("            WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ ")
		.append("            AND CCF.TP_CONTA_CONTABIL IN ('IA','IF','IO') ")
		.append("            AND LF.ID_FRANQUIA = X.ID_FRANQUIA ")
		.append("            AND LF.DT_COMPETENCIA = X.DT_COMPETENCIA ")
		.append("            AND LF.TP_SITUACAO_PENDENCIA = 'A'), 0) AS VL_INDENIZACAO ")
		.append("FROM ( ")
		.append("      SELECT DSF.ID_FRANQUIA, ")
		.append("             DSF.DT_COMPETENCIA, ")
		.append("             F.SG_FILIAL, ")
		.append("             SUM(DSF.VL_DOCTO_SERVICO) AS VL_TOTAL_DOC, ")
		.append("             SUM(DSF.VL_PARTICIPACAO) AS VL_PARTICIPACAO ")
		.append("      FROM DOCTO_SERVICO_FRQ DSF, ")
		.append("           FILIAL F ")
		.append("      WHERE DSF.ID_FRANQUIA = F.ID_FILIAL ")
		.append("      AND   DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL ")
		.append("      AND   DSF.TP_FRETE <> 'SE' ")
		.append("	   AND   DSF.DT_COMPETENCIA = TO_DATE(':competencia','dd/MM/yyyy') ");
		
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			query.append("       AND   DSF.ID_FRANQUIA = :franquia ");
		}
		query.append("      GROUP BY DSF.ID_FRANQUIA, ")
		.append("             DSF.DT_COMPETENCIA, ")
		.append("             F.SG_FILIAL ")
		.append("      ) X ")
		.append(" ) Y ")
		.append(" ORDER BY SG_FILIAL ");
    
		String sql = query.toString();

		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
    		Long idFranquia = (Long) parameters.get("idFilial");
    		sql = sql.replaceAll(":franquia", idFranquia.toString());
    	}

		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");

			String competencia = dtCompetencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
			sql = sql.replaceAll(":competencia", competencia);
		}

		return sql;
	}
	
	
	@SuppressWarnings("rawtypes")
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
