package com.mercurio.lms.franqueados.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.franqueado.emitirRelatorioBaixaCessaoCreditoService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioBaixaCessaoCredito.jasper"
 */
public class RelatorioBaixaCessaoCreditoService extends ReportServiceSupport{
	
	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		String sql = getQuery(parameters);
		JRReportDataObject jr = executeQuery(sql, new Object[]{});
		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", getFilterSummary(parameters));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_CSV);
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			Long idFranquia = (Long) parameters.get("idFilial");
			parametersReport.put("idFranquia", idFranquia);
		}
		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
			parametersReport.put("competencia", dtCompetencia);
		}
		
		jr.setParameters(parametersReport);
		return jr;
	}
    
	@SuppressWarnings("rawtypes")
	private String getQuery(Map parameters){
		String query = " SELECT FF.SG_FILIAL, "
			+ " FFRQ.SG_FILIAL SG_FRANQUIA, "
			+ " DECODE(DS.TP_DOCUMENTO_SERVICO, 'NFS', 'NF-e', 'NFE', 'NF-e', 'CTE', 'CT-e', 'CTR', 'CT-e', 'NFT', 'NT-e', 'NT-e') AS TP_DOCUMENTO, "
			+ " FDS.SG_FILIAL, "
			+ " DS.NR_DOCTO_SERVICO, "
			+ " DECODE(DSF.TP_FRETE, 'CE', 'CIF', 'CR', 'CIF', 'FE', 'FOB', 'FR', 'FOB', 'CIF') AS TP_FRETE, "
			+ " DS.DH_EMISSAO, "
			+ " P.NM_PESSOA, "
			+ " SM.DS_SEGMENTO_MERCADO_I, "
			+ " DS.VL_TOTAL_DOC_SERVICO,  "
			+ " FF.SG_FILIAL,  "
			+ " F.NR_FATURA,  "
			+ " F.DT_EMISSAO, "
			+ " F.DT_VENCIMENTO, "
			+ " FR.SG_FILIAL, "
			+ " R.NR_REDECO, "
			+ " R.DT_EMISSAO "
			+ " FROM DOCTO_SERVICO_FRQ DSF, "
			+ " DOCTO_SERVICO DS, "
			+ " FILIAL FDS, "
			+ " DEVEDOR_DOC_SERV_FAT DEV,  "
			+ " PESSOA P, "
			+ " ITEM_FATURA IF,  "
			+ " FATURA F,  "
			+ " FILIAL FF, "
			+ " ITEM_REDECO IR,  "
			+ " REDECO R, "
			+ " RELACAO_COBRANCA RC, "
			+ " FILIAL FR, "
			+ " CLIENTE C, "
			+ " SEGMENTO_MERCADO SM, "
			+ " FILIAL FFRQ "
			+ " WHERE DSF.ID_FRANQUIA = FFRQ.ID_FILIAL "
			+ " AND DSF.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO "
			+ " AND C.ID_CLIENTE = P.ID_PESSOA "
			+ " AND C.ID_SEGMENTO_MERCADO = SM.ID_SEGMENTO_MERCADO (+) "
			+ " AND R.ID_REDECO = IR.ID_REDECO "
			+ " AND R.ID_REDECO = RC.ID_REDECO "
			+ " AND R.ID_FILIAL = FR.ID_FILIAL "
			+ " AND RC.ID_FILIAL = FF.ID_FILIAL "
			+ " AND IR.ID_FATURA = F.ID_FATURA "
			+ " AND IF.ID_FATURA = F.ID_FATURA "
			+ " AND F.ID_FATURA = IF.ID_FATURA "
			+ " AND IF.ID_DEVEDOR_DOC_SERV_FAT = DEV.ID_DEVEDOR_DOC_SERV_FAT "
			+ " AND DEV.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO "
			+ " AND DS.ID_FILIAL_ORIGEM = FDS.ID_FILIAL "
			+ " AND DEV.ID_CLIENTE = P.ID_PESSOA "
			+ " AND R.TP_FINALIDADE IN ('CJ','DF') ";

		
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null) {
			Long idFranquia = (Long) parameters.get("idFilial");
			query = query.concat(" AND DSF.ID_FRANQUIA = :franquia ");
			query = query.replaceAll(":franquia", idFranquia.toString());
		}
		
		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			query = query.concat(" AND R.DT_LIQUIDACAO BETWEEN TO_DATE(':dtInicioCompetencia','dd/MM/yyyy') AND TO_DATE(':dtFimCompetencia','dd/MM/yyyy') ");
			String competenciaInicial = dtCompetenciaInicial.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
			String competenciaFinal = dtCompetenciaFinal.toString(DateTimeFormat.forPattern("dd/MM/yyyy"));
			query = query.replaceAll(":dtInicioCompetencia", competenciaInicial);
			query = query.replaceAll(":dtFimCompetencia", competenciaFinal);
		}

		return query;
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
