package com.mercurio.lms.entrega.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andresa Vargas
 * 
 * @spring.bean id="lms.entrega.emitirDocumentosServicoDPEVencidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirDocumentosServicoDPEVencido.jasper"
 */
public class EmitirDocumentosServicoDPEVencidoService extends ReportServiceSupport {
	
	private void montaFilterSummary(TypedFlatMap map, SqlTemplate sql) {
		
		if (map.getLong("filialOrigem.idFilial") != null) {
			sql.addFilterSummary("filialOrigem", map.getString("filialOrigem.sgFilial") + " - " + map.getString("filialOrigem.pessoa.nmFantasia"));
		}
		if (map.getLong("filialDestino.idFilial") != null) {
			sql.addFilterSummary("filialDestino", map.getString("filialDestino.sgFilial") + " - " + map.getString("filialDestino.pessoa.nmFantasia"));
		}		
		sql.addFilterSummary("periodoDpeInicial", map.getInteger("diaVencimentoInicial"));
		sql.addFilterSummary("periodoDpeFinal", map.getInteger("diaVencimentoFinal"));		
		sql.addFilterSummary("periodoEmissaoInicial", map.getYearMonthDay("periodoEmissaoInicial"));
		sql.addFilterSummary("periodoEmissaoFinal", map.getYearMonthDay("periodoEmissaoFinal"));
		
	}
	 
	public JRReportDataObject execute(Map criteria) throws Exception {
		
		TypedFlatMap map = (TypedFlatMap) criteria;
		Filial filial = new Filial();
		boolean isFilialUsuarioLogadoMatriz = false;
		
		Long idFilialUsuarioLogado = SessionUtils.getFilialSessao().getIdFilial();
		isFilialUsuarioLogadoMatriz = SessionUtils.isFilialSessaoMatriz();
		
		if (map.getLong("filialOrigem.idFilial") == null && map.getLong("filialDestino.idFilial") == null) 
			throw new BusinessException("LMS-00013");
		else{
			if(map.getLong("filialOrigem.idFilial") != null){
				if(map.getLong("filialDestino.idFilial") == null)
					if (!idFilialUsuarioLogado.equals(map.getLong("filialOrigem.idFilial")) && !isFilialUsuarioLogadoMatriz)
						throw new BusinessException("LMS-09026");
				  
			} 
			
			if (map.getLong("filialDestino.idFilial") != null){
				if(map.getLong("filialOrigem.idFilial") == null)
					if(!idFilialUsuarioLogado.equals(map.getLong("filialDestino.idFilial")) && !isFilialUsuarioLogadoMatriz)
						throw new BusinessException("LMS-09026");
				
			}
			
			if ((!idFilialUsuarioLogado.equals(map.getLong("filialOrigem.idFilial")) && !isFilialUsuarioLogadoMatriz) &&
					(!idFilialUsuarioLogado.equals(map.getLong("filialDestino.idFilial")) && !isFilialUsuarioLogadoMatriz)) {
				throw new BusinessException("LMS-09026");
			}
		}
		 
		if (map.getLong("filialOrigem.idFilial") != null) {
			filial.setIdFilial(map.getLong("filialOrigem.idFilial"));			
			if (!SessionUtils.isFilialAllowedByUsuario(filial) && !isFilialUsuarioLogadoMatriz) {
				throw new BusinessException("LMS-09078");
			}
		}
		if (map.getLong("filialDestino.idFilial") != null) {
			filial.setIdFilial(map.getLong("filialDestino.idFilial"));			
			if (!SessionUtils.isFilialAllowedByUsuario(filial) && !isFilialUsuarioLogadoMatriz) {
				throw new BusinessException("LMS-09078");
			}
		}
		
		
		SqlTemplate sql = createSqlTemplate(map);

		montaFilterSummary(map, sql);
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		// Seta os parametros
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, criteria.get("tpFormatoRelatorio"));

		jr.setParameters(parametersReport);

		return jr;
	} 

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();
		
		/** SELECT */
		sql.addProjection("DOSE.TP_DOCUMENTO_SERVICO", "DOSE_TP_DOCUMENTO_SERVICO");
		sql.addProjection("DOSE.NR_DOCTO_SERVICO", "DOSE_NR_DOCTO_SERVICO");
		sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "FILIAL_ORIGEM_SG_FILIAL");
		sql.addProjection("FILIAL_DESTINO.ID_FILIAL", "FILIAL_DESTINO_ID_FILIAL");
		sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "FILIAL_DESTINO_SG_FILIAL");
		sql.addProjection("PEFI_DESTINO.NM_FANTASIA", "PEFI_DESTINO_NM_FANTASIA");						
		sql.addProjection("DOSE.DH_EMISSAO", "DOSE_DH_EMISSAO");
		sql.addProjection("(SELECT MAX(EVCC.DH_EVENTO) "+ 
							" FROM  PRE_MANIFESTO_DOCUMENTO PRMD,"+
							" MANIFESTO MANI,"+
						    " EVENTO_CONTROLE_CARGA EVCC "+
						    "  WHERE PRMD.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO"+
						    "  AND   MANI.ID_MANIFESTO = PRMD.ID_MANIFESTO"+
						    "  AND   EVCC.ID_CONTROLE_CARGA = MANI.ID_CONTROLE_CARGA"+
						    "  AND   EVCC.ID_FILIAL = DOSE.ID_FILIAL_DESTINO_OPERACIONAL"+
						    "  AND MANI.TP_MANIFESTO = 'V'"+
						    "  AND EVCC.TP_EVENTO_CONTROLE_CARGA = 'CP')", "EVMA_DH_EVENTO");
		sql.addProjection("DOSE.DT_PREV_ENTREGA", "DOSE_DT_PREV_ENTREGA");
		sql.addProjection("(? - TRUNC(DOSE.DT_PREV_ENTREGA))", "DOSE_DIAS_ATRASO"); // Data Atual
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());		
		sql.addProjection("NVL((SELECT SUM(DECODE(AWB.ID_FILIAL_ORIGEM, MAN.ID_FILIAL_ORIGEM, 0, 1))" +
							" FROM MANIFESTO_ENTREGA_DOCUMENTO MAED" + 
								" INNER JOIN MANIFESTO MAN ON MAED.ID_MANIFESTO_ENTREGA = MAN.ID_MANIFESTO" + 
								" INNER JOIN OCORRENCIA_ENTREGA OCEN ON OCEN.ID_OCORRENCIA_ENTREGA = MAED.ID_OCORRENCIA_ENTREGA " +
								" AND OCEN.TP_OCORRENCIA NOT IN ('E','A')" +
								" LEFT OUTER JOIN CTO_AWB CA ON MAED.ID_DOCTO_SERVICO = CA.ID_CONHECIMENTO " +
								" LEFT OUTER JOIN AWB ON CA.ID_AWB = AWB.ID_AWB AND AWB.TP_STATUS_AWB <> 'C'"+ 
							" WHERE MAED.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO), 0)", "DOSE_TENTATIVAS_ENTREGA");
		sql.addProjection("CLRE.ID_PESSOA", "CLRE_ID_PESSOA");
		sql.addProjection("CLRE.TP_IDENTIFICACAO", "CLRE_TP_IDENTIFICACAO");
		sql.addProjection("CLRE.NR_IDENTIFICACAO", "CLRE_NR_IDENTIFICACAO");
		sql.addProjection("CLRE.NM_PESSOA", "CLRE_NM_PESSOA");
		sql.addProjection("CLDE.ID_PESSOA", "CLDE_ID_PESSOA");
		sql.addProjection("CLDE.TP_IDENTIFICACAO", "CLDE_TP_IDENTIFICACAO");
		sql.addProjection("CLDE.NR_IDENTIFICACAO", "CLDE_NR_IDENTIFICACAO");
		sql.addProjection("CLDE.NM_PESSOA", "CLDE_NM_PESSOA");
		sql.addProjection("DOSE.PS_REAL", "DOSE_PS_REAL");
		sql.addProjection("DOSE.PS_AFORADO", "DOSE_PS_AFORADO");
		sql.addProjection("DOSE.VL_MERCADORIA", "DOSE_VL_MERCADORIA");
		sql.addProjection("MOEDA.DS_SIMBOLO", "MOEDA_DS_SIMBOLO");		
		
		/** FROM */
		sql.addFrom("DOCTO_SERVICO", "DOSE");
		sql.addFrom("FILIAL", "FILIAL_ORIGEM");
		sql.addFrom("FILIAL", "FILIAL_DESTINO");
		sql.addFrom("PESSOA", "CLRE");
		sql.addFrom("PESSOA", "CLDE");
		sql.addFrom("PESSOA", "PEFI_DESTINO");
		sql.addFrom("MOEDA", "MOEDA");
		
		/** JOIN */
		sql.addJoin("FILIAL_ORIGEM.ID_FILIAL(+)", "DOSE.ID_FILIAL_ORIGEM");
		sql.addJoin("FILIAL_DESTINO.ID_FILIAL(+)", "DOSE.ID_FILIAL_DESTINO_OPERACIONAL");
		sql.addJoin("CLRE.ID_PESSOA(+)", "DOSE.ID_CLIENTE_REMETENTE");
		sql.addJoin("CLDE.ID_PESSOA(+)", "DOSE.ID_CLIENTE_DESTINATARIO");
		sql.addJoin("MOEDA.ID_MOEDA", "DOSE.ID_MOEDA");
		sql.addJoin("PEFI_DESTINO.ID_PESSOA(+)", "FILIAL_DESTINO.ID_FILIAL");
		
		
		/** CRITERIA */
		sql.addCustomCriteria("NOT EXISTS (SELECT 1 " +
				  " FROM " +
				  " EVENTO_DOCUMENTO_SERVICO EDS," +
				  " EVENTO EVE " +
				  " WHERE EDS.ID_DOCTO_SERVICO = DOSE.ID_DOCTO_SERVICO " +
				  " AND EVE.ID_EVENTO = EDS.ID_EVENTO " +
				  " AND EDS.BL_EVENTO_CANCELADO = 'N' " +
				  " AND EVE.CD_EVENTO = 21)");
		

		if (criteria.getLong("filialOrigem.idFilial") != null) {
			sql.addCriteria("FILIAL_ORIGEM.ID_FILIAL", "=", criteria.getLong("filialOrigem.idFilial"));
		}
		
		if (criteria.getLong("filialDestino.idFilial") != null) {
			sql.addCriteria("FILIAL_DESTINO.ID_FILIAL", "=", criteria.getLong("filialDestino.idFilial"));
		}

		if ( criteria.getYearMonthDay("periodoEmissaoInicial") != null && criteria.getYearMonthDay("periodoEmissaoFinal") != null ) {
			
			sql.addCustomCriteria(" TRUNC(CAST(DOSE.DH_EMISSAO AS DATE) ) BETWEEN ? and ? " );
			sql.addCriteriaValue(criteria.getYearMonthDay("periodoEmissaoInicial"));
			sql.addCriteriaValue(criteria.getYearMonthDay("periodoEmissaoFinal"));
			
		} else if ( criteria.getYearMonthDay("periodoEmissaoInicial") != null ) {
			
			sql.addCriteria(" TRUNC(CAST(DOSE.DH_EMISSAO AS DATE) )",  ">=", criteria.getYearMonthDay("periodoEmissaoInicial"));
			
		} else if ( criteria.getYearMonthDay("periodoEmissaoFinal") != null ) {
			
			sql.addCriteria(" TRUNC(CAST(DOSE.DH_EMISSAO AS DATE) )", "<=",  criteria.getYearMonthDay("periodoEmissaoFinal"));
			
		} 
		
		if ( criteria.getInteger("diaVencimentoInicial") != null && criteria.getInteger("diaVencimentoFinal") != null ) {
			
			sql.addCustomCriteria("(? - TRUNC(DOSE.DT_PREV_ENTREGA)) BETWEEN ? and ? "); 
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());	
			sql.addCriteriaValue(criteria.getInteger("diaVencimentoInicial"));
			sql.addCriteriaValue(criteria.getInteger("diaVencimentoFinal"));
			
		} else if ( criteria.getInteger("diaVencimentoInicial") != null ) {
			
			sql.addCustomCriteria(" (? - TRUNC(DOSE.DT_PREV_ENTREGA)) >= ? ");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());	
			sql.addCriteriaValue(criteria.getInteger("diaVencimentoInicial"));
			
		} else if ( criteria.getInteger("diaVencimentoFinal") != null ) {
			
			sql.addCustomCriteria(" (? - TRUNC(DOSE.DT_PREV_ENTREGA)) <= ? ");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());	
			sql.addCriteriaValue(criteria.getInteger("diaVencimentoFinal"));
			 
		}		
		 
		/** ORDER BY */
		sql.addOrderBy("FILIAL_DESTINO.SG_FILIAL, DOSE.TP_DOCUMENTO_SERVICO, FILIAL_ORIGEM.SG_FILIAL, DOSE.NR_DOCTO_SERVICO");
		
		return sql;
	}
	
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("DOSE_TP_DOCUMENTO_SERVICO","DM_TIPO_DOCUMENTO_SERVICO");
	}

	
}
