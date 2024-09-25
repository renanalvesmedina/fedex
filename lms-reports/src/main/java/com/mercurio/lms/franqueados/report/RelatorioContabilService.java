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
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioContabil.jasper"
 */
public class RelatorioContabilService extends ReportServiceSupport {
	
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
StringBuilder query = new StringBuilder();
        
        query.append("SELECT Y.SG_FILIAL, \n");
        query.append("  Y.VL_TOTAL_DOC, \n");
        query.append("  Y.VL_PARTICIPACAO, \n");
        query.append("  ROUND((Y.VL_PARTICIPACAO / Y.VL_TOTAL_DOC),4) AS PC_FRETE, \n");
        query.append("  Y.VL_IRE, \n");
        query.append("  Y.VL_SERV_REEMB, \n");
        query.append("  Y.VL_SERVICO_ADICIONAL, \n");
        query.append("  Y.VL_CREDITO_DIVERSO,-- \n");
        query.append("  Y.VL_RECALCULO, \n");
        query.append("  Y.VL_BDM, \n");
        query.append("  Y.VL_DEBITO_DIVERSO,-- \n");
        query.append("  Y.VL_OVER_60, \n");
        query.append("  Y.VL_INDENIZACAO,-- \n");
        query.append("  Y.VL_DEBITO_ANTECIPACAO, \n");
        query.append("  Y.VL_CREDITO_DIVERSO + Y.VL_CREDITO_ANTECIPACAO + Y.VL_RECALCULO    + Y.VL_BDM + Y.VL_DEBITO_DIVERSO + Y.VL_DEBITO_ANTECIPACAO + Y.VL_OVER_60 + Y.VL_INDENIZACAO AS VL_TOTAL_DEB_DIV, \n");
        query.append("  Y.VL_PARTICIPACAO + Y.VL_IRE + Y.VL_SERV_REEMB + Y.VL_SERVICO_ADICIONAL                                        AS VL_TOTAL, \n");
        query.append("  Y.VL_CREDITO_ANTECIPACAO \n");
        query.append("FROM \n");
        query.append("  (SELECT X.DT_COMPETENCIA, \n");
        query.append("    X.SG_FILIAL, \n");
        query.append("    X.VL_TOTAL_DOC, \n");
        query.append("    X.VL_PARTICIPACAO, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(RBQ.VL_CTE + RBQ.VL_TONELADA) \n");
        query.append("    FROM REEMBARQUE_DOC_SERV_FRQ RBQ \n");
        query.append("    WHERE RBQ.ID_FRANQUIA  = X.ID_FRANQUIA \n");
        query.append("    AND RBQ.DT_COMPETENCIA = X.DT_COMPETENCIA \n");
        query.append("    ), 0) AS VL_SERV_REEMB, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(DSF.VL_PARTICIPACAO) \n");
        query.append("    FROM DOCTO_SERVICO_FRQ DSF \n");
        query.append("    WHERE DSF.ID_FRANQUIA                  = X.ID_FRANQUIA \n");
        query.append("    AND DSF.DT_COMPETENCIA                 = X.DT_COMPETENCIA \n");
        query.append("    AND DSF.TP_FRETE                       = 'SE' \n");
        query.append("    AND DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL \n");
        query.append("    ), 0) AS VL_SERVICO_ADICIONAL, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(LF.VL_LANCAMENTO) \n");
        query.append("    FROM LANCAMENTO_FRQ LF, \n");
        query.append("      CONTA_CONTABIL_FRQ CCF \n");
        query.append("    WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ \n");
        query.append("    AND CCF.TP_CONTA_CONTABIL     IN ('IR') \n");
        query.append("    AND LF.ID_FRANQUIA             = X.ID_FRANQUIA \n");
        query.append("    AND LF.DT_COMPETENCIA          = X.DT_COMPETENCIA \n");
        query.append("    AND LF.TP_SITUACAO_PENDENCIA   = 'A' \n");
        query.append("    ), 0) AS VL_IRE, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(LF.VL_LANCAMENTO) \n");
        query.append("    FROM LANCAMENTO_FRQ LF, \n");
        query.append("      CONTA_CONTABIL_FRQ CCF \n");
        query.append("    WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ \n");
        query.append("    AND CCF.TP_CONTA_CONTABIL     IN ('CD') \n");
        query.append("    AND LF.ID_FRANQUIA             = X.ID_FRANQUIA \n");
        query.append("    AND LF.DT_COMPETENCIA          = X.DT_COMPETENCIA \n");
        query.append("    AND LF.TP_SITUACAO_PENDENCIA   = 'A' \n");
        query.append("    ), 0) AS VL_CREDITO_DIVERSO, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(NVL(LF.VL_CONTABIL, LF.VL_LANCAMENTO)) \n");
        query.append("    FROM LANCAMENTO_FRQ LF, \n");
        query.append("      CONTA_CONTABIL_FRQ CCF \n");
        query.append("    WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ \n");
        query.append("    AND CCF.TP_CONTA_CONTABIL     IN ('CA') \n");
        query.append("    AND LF.ID_FRANQUIA             = X.ID_FRANQUIA \n");
        query.append("    AND LF.DT_COMPETENCIA          = X.DT_COMPETENCIA \n");
        query.append("    AND LF.TP_SITUACAO_PENDENCIA   = 'A' \n");
        query.append("    ), 0) AS VL_CREDITO_ANTECIPACAO, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(DSF.VL_DIFERENCA_PARTICIPACAO) * -1 \n");
        query.append("    FROM DOCTO_SERVICO_FRQ DSF \n");
        query.append("    WHERE DSF.ID_FRANQUIA                  = X.ID_FRANQUIA \n");
        query.append("    AND DSF.DT_COMPETENCIA                 = X.DT_COMPETENCIA \n");
        query.append("    AND DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NOT NULL \n");
        query.append("    ), 0) AS VL_RECALCULO, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(LF.VL_LANCAMENTO) * -1 \n");
        query.append("    FROM LANCAMENTO_FRQ LF, \n");
        query.append("      CONTA_CONTABIL_FRQ CCF \n");
        query.append("    WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ \n");
        query.append("    AND CCF.TP_CONTA_CONTABIL      = 'BD' \n");
        query.append("    AND LF.ID_FRANQUIA             = X.ID_FRANQUIA \n");
        query.append("    AND LF.DT_COMPETENCIA          = X.DT_COMPETENCIA \n");
        query.append("    AND LF.TP_SITUACAO_PENDENCIA   = 'A' \n");
        query.append("    ), 0) AS VL_BDM, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(LF.VL_LANCAMENTO) * -1 \n");
        query.append("    FROM LANCAMENTO_FRQ LF, \n");
        query.append("      CONTA_CONTABIL_FRQ CCF \n");
        query.append("    WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ \n");
        query.append("    AND CCF.TP_CONTA_CONTABIL     IN ('DD') \n");
        query.append("    AND LF.ID_FRANQUIA             = X.ID_FRANQUIA \n");
        query.append("    AND LF.DT_COMPETENCIA          = X.DT_COMPETENCIA \n");
        query.append("    AND LF.TP_SITUACAO_PENDENCIA   = 'A' \n");
        query.append("    ), 0) AS VL_DEBITO_DIVERSO, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(LF.VL_LANCAMENTO) * -1 \n");
        query.append("    FROM LANCAMENTO_FRQ LF, \n");
        query.append("      CONTA_CONTABIL_FRQ CCF \n");
        query.append("    WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ \n");
        query.append("    AND CCF.TP_CONTA_CONTABIL     IN ('DA') \n");
        query.append("    AND LF.ID_FRANQUIA             = X.ID_FRANQUIA \n");
        query.append("    AND LF.DT_COMPETENCIA          = X.DT_COMPETENCIA \n");
        query.append("    AND LF.TP_SITUACAO_PENDENCIA   = 'A' \n");
        query.append("    ), 0) AS VL_DEBITO_ANTECIPACAO, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(NVL(LF.VL_CONTABIL, LF.VL_LANCAMENTO)) * -1 \n");
        query.append("    FROM LANCAMENTO_FRQ LF, \n");
        query.append("      CONTA_CONTABIL_FRQ CCF \n");
        query.append("    WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ \n");
        query.append("    AND CCF.TP_CONTA_CONTABIL      = 'OV' \n");
        query.append("    AND LF.ID_FRANQUIA             = X.ID_FRANQUIA \n");
        query.append("    AND LF.DT_COMPETENCIA          = X.DT_COMPETENCIA \n");
        query.append("    AND LF.TP_SITUACAO_PENDENCIA   = 'A' \n");
        query.append("    ), 0) AS VL_OVER_60, \n");
        query.append("    NVL( \n");
        query.append("    (SELECT SUM(NVL(LF.VL_CONTABIL, LF.VL_LANCAMENTO)) * -1 \n");
        query.append("    FROM LANCAMENTO_FRQ LF, \n");
        query.append("      CONTA_CONTABIL_FRQ CCF \n");
        query.append("    WHERE LF.ID_CONTA_CONTABIL_FRQ = CCF.ID_CONTA_CONTABIL_FRQ \n");
        query.append("    AND CCF.TP_CONTA_CONTABIL     IN ('IA','IF','IO') \n");
        query.append("    AND LF.ID_FRANQUIA             = X.ID_FRANQUIA \n");
        query.append("    AND LF.DT_COMPETENCIA          = X.DT_COMPETENCIA \n");
        query.append("    AND LF.TP_SITUACAO_PENDENCIA   = 'A' \n");
        query.append("    ), 0) AS VL_INDENIZACAO \n");
        query.append("  FROM \n");
        query.append("    (SELECT DSF.ID_FRANQUIA, \n");
        query.append("      DSF.DT_COMPETENCIA, \n");
        query.append("      F.SG_FILIAL, \n");
        query.append("      SUM(DSF.VL_DOCTO_SERVICO) AS VL_TOTAL_DOC, \n");
        query.append("      SUM(DSF.VL_PARTICIPACAO)  AS VL_PARTICIPACAO \n");
        query.append("    FROM DOCTO_SERVICO_FRQ DSF, \n");
        query.append("      FILIAL F \n");
        query.append("    WHERE DSF.ID_FRANQUIA                  = F.ID_FILIAL \n");
        query.append("    AND DSF.ID_DOCTO_SERVICO_FRQ_ORIGINAL IS NULL \n");
        query.append("    AND DSF.TP_FRETE                      <> 'SE' \n");
        query.append("    AND DSF.DT_COMPETENCIA = TO_DATE(':competencia','dd/MM/yyyy')  \n");
        
        if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
            query.append("       AND   DSF.ID_FRANQUIA = :franquia ");
        }
        query.append("    GROUP BY DSF.ID_FRANQUIA, \n");
        query.append("      DSF.DT_COMPETENCIA, \n");
        query.append("      F.SG_FILIAL \n");
        query.append("    ) X \n");
        query.append("  ) Y \n");
        query.append("ORDER BY SG_FILIAL");
		
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
