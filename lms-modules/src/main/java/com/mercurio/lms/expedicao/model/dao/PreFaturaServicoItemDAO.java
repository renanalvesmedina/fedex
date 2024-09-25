package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import org.hibernate.Hibernate;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.expedicao.model.PreFaturaServicoItem;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;

public class PreFaturaServicoItemDAO extends BaseCrudDao<PreFaturaServicoItem, Long> {
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return PreFaturaServicoItem.class;
	}
	
	private String getSqlBaseItensPreFatura() {
		StringBuilder sql = new StringBuilder();
		
		/* Busca as informações de ORDEM_SERVICO_ITEM */
		sql.append("SELECT DISTINCT ");
		sql.append(" 	PFIT.ID_PRE_FATURA_SERVICO_ITEM, ");
		sql.append("	SUBQ.ID_DOCTO_SERVICO, ");
		sql.append("	OSIT.ID_PARCELA_PRECO, ");
		sql.append("	OSIT.DT_EXECUCAO, ");
		sql.append("	NVL(OSIT.VL_NEGOCIADO, OSIT.VL_TABELA) VL_COBRANCA, ");
		sql.append("	OSIT.VL_TABELA, ");
		sql.append("	OSIT.DS_SERVICO, ");
		sql.append("	ORSE.ID_MUNICIPIO_EXECUCAO, ");
		sql.append("	OSIT.QT_PALETE, ");
		sql.append("	ORSE.ID_FILIAL_REGISTRO, ");
		sql.append("	ORSE.NR_ORDEM_SERVICO, ");
		sql.append(" 	NULL AS QT_DIAS, ");
		sql.append(" 	PFIT.ID_ORDEM_SERVICO_ITEM, ");
		sql.append(" 	PFIT.ID_SERVICO_GERACAO_AUTOMATICA, ");
		sql.append(" 	NULL AS ID_OCORRENCIA_PENDENCIA, ");
		sql.append("	PFIT.VL_SERVICO_COM_DESCONTO AS VL_COBRANCA_AJUSTADO, ");
		sql.append("	PFIT.TP_SITUACAO AS TP_SITUACAO ");
		sql.append("FROM  ");
		sql.append("	PRE_FATURA_SERVICO_ITEM PFIT, ");
		sql.append("    ORDEM_SERVICO ORSE, ");
		sql.append("    ORDEM_SERVICO_ITEM OSIT, ");
		sql.append("    (SELECT ");
		sql.append("            ORSE.ID_ORDEM_SERVICO, ");
		sql.append("            NVL(DSOS.ID_DOCTO_SERVICO,NVL(DSME.ID_DOCTO_SERVICO, NVL(DSMV.ID_DOCTO_SERVICO,DSMC.ID_DOCTO_SERVICO))) AS ID_DOCTO_SERVICO ");
		sql.append("         	FROM ORDEM_SERVICO ORSE, ");
		sql.append("         	(	");
		sql.append("				SELECT ORSE.ID_ORDEM_SERVICO, OSDS.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO ");
		sql.append("           		FROM ORDEM_SERVICO_DOCUMENTO OSDS, ORDEM_SERVICO ORSE ");
		sql.append("          		WHERE ORSE.ID_ORDEM_SERVICO = OSDS.ID_ORDEM_SERVICO ");
		sql.append("        	) DSOS, ");
		sql.append("          	(  ");
		sql.append("          		SELECT ORSE.ID_ORDEM_SERVICO, MEDO.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO ");
		sql.append("              	FROM MANIFESTO_ENTREGA_DOCUMENTO MEDO, ORDEM_SERVICO ORSE ");
		sql.append("              	WHERE ORSE.ID_MANIFESTO = MEDO.ID_MANIFESTO_ENTREGA ");
		sql.append("            ) DSME, ");
		sql.append("            (  ");
		sql.append(" 				SELECT ORSE.ID_ORDEM_SERVICO, MVDO.ID_CONHECIMENTO AS ID_DOCTO_SERVICO ");
		sql.append(" 				FROM MANIFESTO_NACIONAL_CTO MVDO, ORDEM_SERVICO ORSE ");
		sql.append(" 				WHERE ORSE.ID_MANIFESTO = MVDO.ID_MANIFESTO_VIAGEM_NACIONAL ");
		sql.append(" 			) DSMV, ");
		sql.append(" 			( ");
		sql.append(" 				SELECT ORSE.ID_ORDEM_SERVICO, DOCT.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO ");
		sql.append(" 				FROM PEDIDO_COLETA PECO, DOCTO_SERVICO DOCT, ORDEM_SERVICO ORSE ");
		sql.append(" 				WHERE ORSE.ID_MANIFESTO_COLETA = PECO.ID_MANIFESTO_COLETA ");
		sql.append(" 				AND DOCT.ID_PEDIDO_COLETA = PECO.ID_PEDIDO_COLETA ");
		sql.append(" 			) DSMC ");
		sql.append(" 			WHERE ");
		sql.append("					ORSE.ID_ORDEM_SERVICO = DSOS.ID_ORDEM_SERVICO(+) ");
		sql.append(" 				AND ORSE.ID_ORDEM_SERVICO = DSME.ID_ORDEM_SERVICO(+) ");
		sql.append(" 				AND ORSE.ID_ORDEM_SERVICO = DSMV.ID_ORDEM_SERVICO(+) ");
		sql.append(" 				AND ORSE.ID_ORDEM_SERVICO = DSMC.ID_ORDEM_SERVICO(+) ");
		sql.append(" 	) SUBQ ");
		sql.append("WHERE ");
		sql.append("	PFIT.ID_ORDEM_SERVICO_ITEM = OSIT.ID_ORDEM_SERVICO_ITEM ");
		sql.append("AND ORSE.ID_ORDEM_SERVICO = SUBQ.ID_ORDEM_SERVICO ");
		sql.append("AND ORSE.ID_ORDEM_SERVICO = OSIT.ID_ORDEM_SERVICO ");
		sql.append("AND PFIT.ID_PRE_FATURA_SERVICO = :idPreFaturaServico ");
		
		sql.append("UNION ALL ");
		
		/* Busca as informações de SERVICO_GERACAO_AUTOMATICA */
		sql.append("SELECT ");
		sql.append(" 	PFIT.ID_PRE_FATURA_SERVICO_ITEM, ");
		sql.append(" 	SEGA.ID_DOCTO_SERVICO, ");
		sql.append("	SEGA.ID_PARCELA_PRECO, ");
		sql.append(" 	TRUNC(SEGA.DH_CALCULO) DT_EXECUCAO, ");
		sql.append(" 	SEGA.VL_SERVICO_ADICIONAL VL_COBRANCA, ");
		sql.append(" 	SEGA.VL_TABELA VL_TABELA, ");
		sql.append(" 	NULL AS DS_SERVICO, ");
		sql.append(" 	SEGA.ID_MUNICIPIO_EXECUCAO AS ID_MUNICIPIO_EXECUCAO, ");
		sql.append("	NULL AS QT_PALETE, ");
		sql.append("	NULL AS ID_FILIAL_REGISTRO, ");
		sql.append("	NULL AS NR_ORDEM_SERVICO, ");
		sql.append(" 	SEGA.QT_DIAS_COBRADOS AS QT_DIAS, ");
		sql.append(" 	PFIT.ID_ORDEM_SERVICO_ITEM, ");
		sql.append(" 	PFIT.ID_SERVICO_GERACAO_AUTOMATICA, ");
		sql.append(" 	SEGA.ID_OCORRENCIA_PENDENCIA AS ID_OCORRENCIA_PENDENCIA, ");
		sql.append("	PFIT.VL_SERVICO_COM_DESCONTO AS VL_COBRANCA_AJUSTADO, ");
		sql.append("	PFIT.TP_SITUACAO AS TP_SITUACAO ");
		sql.append("FROM ");
		sql.append(" 	PRE_FATURA_SERVICO_ITEM PFIT, ");
		sql.append(" 	SERVICO_GERACAO_AUTOMATICA SEGA ");
		sql.append("WHERE ");
		sql.append(" 	PFIT.ID_SERVICO_GERACAO_AUTOMATICA = SEGA.ID_SERVICO_GERACAO_AUTOMATICA ");
		sql.append("AND PFIT.ID_PRE_FATURA_SERVICO = :idPreFaturaServico ");
		
		return sql.toString();
	}
	
	private String getSqlAprovacaoPreFaturaItem() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("	SUBQ.ID_PRE_FATURA_SERVICO_ITEM idPreFaturaServicoItem, ");
		sql.append("	PFIT.ID_ORDEM_SERVICO_ITEM idOrdemServicoItem, ");
		sql.append("	PFIT.ID_SERVICO_GERACAO_AUTOMATICA idServicoGeracaoAutomatica, ");
		sql.append("	SUBQ.ID_DOCTO_SERVICO idDoctoServico, ");
		sql.append("	DOCT.TP_DOCUMENTO_SERVICO tpDocumentoServico, ");
		sql.append("	FILI.SG_FILIAL sgFilial, ");
		sql.append("	DOCT.NR_DOCTO_SERVICO nrDoctoServico, ");
		sql.append("	VI18N(PARC.DS_PARCELA_PRECO_I) dsParcelaPreco, ");
		sql.append("	SUBQ.DT_EXECUCAO dtExecucao, ");
		sql.append("	SUBQ.VL_COBRANCA vlCobranca, ");
		sql.append("	SUBQ.VL_TABELA vlTabela, ");
		sql.append("	SUBQ.DS_SERVICO dsServico, ");
		sql.append("	SUBQ.NR_ORDEM_SERVICO ordemServico, ");
		sql.append("	decode(PFIT.TP_SITUACAO,'I', null, PFIT.TP_SITUACAO) tpSituacao, ");
		sql.append("	PFIT.VL_SERVICO_COM_DESCONTO vlNotaFiscal, ");
		sql.append("	PFIT.ID_MOTIVO_PRE_FATURA_SERV idMotivo, ");
		sql.append("	SUBQ.ID_MUNICIPIO_EXECUCAO idMunicipioExecucao, ");
		sql.append("	PARC.ID_SERVICO_ADICIONAL idServicoAdicional, ");
		sql.append("	PARC.ID_PARCELA_PRECO idParcelaPreco ");
		sql.append("FROM ");
		sql.append("	(" + getSqlBaseItensPreFatura() + ") SUBQ, ");
		sql.append("	PRE_FATURA_SERVICO_ITEM PFIT, ");
		sql.append("	DOCTO_SERVICO DOCT, ");
		sql.append("	FILIAL FILI, ");
		sql.append("	PARCELA_PRECO PARC ");
		sql.append("WHERE ");
		sql.append("	SUBQ.ID_PRE_FATURA_SERVICO_ITEM = PFIT.ID_PRE_FATURA_SERVICO_ITEM ");
		sql.append("AND	SUBQ.ID_DOCTO_SERVICO = DOCT.ID_DOCTO_SERVICO(+) ");
		sql.append("AND DOCT.ID_FILIAL_ORIGEM = FILI.ID_FILIAL(+) ");
		sql.append("AND PARC.ID_PARCELA_PRECO = SUBQ.ID_PARCELA_PRECO ");
		sql.append("ORDER BY ");
		sql.append("	dtExecucao, ");
		sql.append("	dsParcelaPreco, ");
		sql.append("	idOrdemServicoItem, ");
		sql.append("	idDoctoServico DESC ");
		
		return sql.toString();
	}
	
	public ResultSetPage<Map<String, Object>> findPaginatedAprovacaoPreFaturaDetalhe(PaginatedQuery paginatedQuery) {		
		StringBuilder sql = new StringBuilder(getSqlAprovacaoPreFaturaItem());
						
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				/*00*/sqlQuery.addScalar("idPreFaturaServicoItem", Hibernate.LONG);
				/*01*/sqlQuery.addScalar("idOrdemServicoItem", Hibernate.LONG);
				/*02*/sqlQuery.addScalar("idServicoGeracaoAutomatica", Hibernate.LONG);
				/*03*/sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
				/*04*/sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
				/*05*/sqlQuery.addScalar("sgFilial", Hibernate.STRING);
				/*06*/sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);
				/*07*/sqlQuery.addScalar("dsParcelaPreco", Hibernate.STRING);
				/*08*/sqlQuery.addScalar("dtExecucao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				/*09*/sqlQuery.addScalar("vlCobranca", Hibernate.BIG_DECIMAL);
				/*10*/sqlQuery.addScalar("vlTabela", Hibernate.BIG_DECIMAL);
				/*11*/sqlQuery.addScalar("dsServico", Hibernate.STRING);
				/*12*/sqlQuery.addScalar("tpSituacao", Hibernate.custom(DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_ACAO_PRE_FATURA"}));
				/*13*/sqlQuery.addScalar("vlNotaFiscal", Hibernate.BIG_DECIMAL);
				/*14*/sqlQuery.addScalar("idMotivo", Hibernate.LONG);
				/*15*/sqlQuery.addScalar("idMunicipioExecucao", Hibernate.LONG);
				/*16*/sqlQuery.addScalar("idServicoAdicional", Hibernate.LONG);
				/*17*/sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);	
				//LMS-7113
				/*18*/sqlQuery.addScalar("ordemServico", Hibernate.LONG);
			}			
		};
		
		List<Object[]> sqlResult = getAdsmHibernateTemplate().findBySql(sql.toString(), paginatedQuery.getCriteria(), csq);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Long idAgrupador = null;
		List<Long> idsDoctos = null;
		Map<String, Object> map = null;
		
		for(Object[] tuple : sqlResult) {
			if(idAgrupador == null || !idAgrupador.equals((Long)tuple[1])) {
				map =  new HashMap<String, Object>();
				idsDoctos = new ArrayList<Long>();
				idAgrupador = (Long)tuple[1];
				map.put("idPreFaturaServicoItem", tuple[0]);
				map.put("idOrdemServicoItem", tuple[1]);
				map.put("idServicoGeracaoAutomatica", tuple[2]);
				map.put("sgFilial", tuple[5]);
				map.put("nrDoctoServico", (tuple[6] != null &&  tuple[4] != null) ? FormatUtils.formataNrDocumento((String)tuple[6], (String)tuple[4]):null);
				map.put("dsParcelaPreco", tuple[7]);
				map.put("dtExecucao", tuple[8]);
				map.put("vlCobranca", tuple[9]);
				map.put("vlTabela", tuple[10]);
				map.put("dsServico", tuple[11]);
				map.put("tpSituacao", tuple[12]);
				map.put("vlNotaFiscal", tuple[13]);
				map.put("idMotivo", tuple[14]);
				map.put("idMunicipioExecucao", tuple[15]);
				map.put("idServicoAdicional", tuple[16]);
				map.put("idParcelaPreco", tuple[17]);
				map.put("idsDoctosServico", idsDoctos);		
				map.put("ordemServico", tuple[18]);
				result.add(map);
			}
			if (tuple[3] != null) {
				idsDoctos.add((Long)tuple[3]);
			}
		}
		
		return new ResultSetPage<Map<String, Object>>(0, result);
	}
	
	public void storeSitucaoValorMotivoById(Long id, String situacao, BigDecimal valor, Long idMotivo) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> criteria = new HashMap<String, Object>();
		sb.append("UPDATE PRE_FATURA_SERVICO_ITEM SET ");
		sb.append(" TP_SITUACAO = :situacao ");
		criteria.put("situacao", situacao);
		
		if (valor != null) {
			sb.append(" , VL_SERVICO_COM_DESCONTO = :valor ");
			criteria.put("valor", valor);
		} else {
			sb.append(" , VL_SERVICO_COM_DESCONTO = null ");
		}
		
		if (idMotivo != null) {
			sb.append(" , ID_MOTIVO_PRE_FATURA_SERV = :idMotivo ");
			criteria.put("idMotivo", idMotivo);
		} else {
			sb.append(" , ID_MOTIVO_PRE_FATURA_SERV = null ");
		}
		
		sb.append(" WHERE ID_PRE_FATURA_SERVICO_ITEM  = :id");
		criteria.put("id", id);
		
		getAdsmHibernateTemplate().executeUpdateBySql(sb.toString(), criteria);
	}
	
	private String getSqlEmissaoPreFaturaItem(String tpRelatorio) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("	SUBQ.ID_PRE_FATURA_SERVICO_ITEM idPreFaturaServicoItem, ");
		sql.append("	PFIT.ID_ORDEM_SERVICO_ITEM idOrdemServicoItem, ");
		sql.append("	PFIT.ID_SERVICO_GERACAO_AUTOMATICA idServicoGeracaoAutomatica, ");
		sql.append("	SUBQ.DT_EXECUCAO dtExecucao, ");
		sql.append("	REME.NM_PESSOA nmRemetente, ");
		sql.append("	DEST.NM_PESSOA nmDestinatario, ");
		sql.append("	MUEX.NM_MUNICIPIO nmMunicipioExecucao, ");
		sql.append("	SUBQ.ID_DOCTO_SERVICO idDoctoServico, ");
		sql.append("	DOCT.TP_DOCUMENTO_SERVICO tpDocumentoServico, ");
		sql.append("	FILI.SG_FILIAL sgFilial, ");
		sql.append("	DOCT.NR_DOCTO_SERVICO nrDoctoServico, ");
		sql.append("	FILD.SG_FILIAL sgFilialDestino, ");
		sql.append("	DOCT.VL_TOTAL_DOC_SERVICO vlFrete, ");
		sql.append("	NOTA.NR_NOTA_FISCAL nrNotaFiscal, ");
		sql.append("	NOTA.QT_VOLUMES qtVolumes, ");
		sql.append("	NOTA.PS_MERCADORIA psReferencia, ");
		sql.append("	NOTA.VL_TOTAL vlMercadoria, ");
		sql.append("	VI18N(PARC.DS_PARCELA_PRECO_I) dsParcelaPreco, ");
		sql.append("	VI18N(OCOP.DS_OCORRENCIA_I) dsOcorrencia, ");
		sql.append("	SUBQ.QT_DIAS qtDias, ");
		sql.append("	SUBQ.VL_COBRANCA vlCobranca, ");
		sql.append("	SUBQ.QT_PALETE quantidade, ");
		sql.append("	FILR.SG_FILIAL sgFilialOrdermServico, ");
		sql.append("	SUBQ.NR_ORDEM_SERVICO nrOrdermServico, ");
		sql.append("	(   select ae.ds_email_tomador  ");
		sql.append("		from AGENDAMENTO_ENTREGA AE, ");
		sql.append("		AGENDAMENTO_DOCTO_SERVICO AED, ");
		sql.append("		SERVICO_GERACAO_AUTOMATICA sga, ");
		sql.append("		PARCELA_PRECO PP ");
		sql.append("		where ");
		sql.append("		ae.id_agendamento_entrega = aed.id_agendamento_entrega ");
		sql.append("		and sga.id_docto_servico = aed.id_docto_servico  ");
		sql.append("		and ae.tp_situacao_agendamento = 'F' ");
		sql.append("		and sga.id_parcela_preco = PP.id_parcela_preco ");
		sql.append("		and pp.cd_parcela_preco = '" + ConstantesExpedicao.CD_AGENDAMENTO_COLETA + "'");
		sql.append("		and sga.id_servico_geracao_automatica = SUBQ.id_servico_geracao_automatica ) email, ");
		sql.append("	SUBQ.VL_COBRANCA_AJUSTADO vlCobrancaAjustado, ");
		sql.append("	SUBQ.TP_SITUACAO tpSituacao, ");
        sql.append("    DEST.NR_IDENTIFICACAO destNrIdentificacao ");
		sql.append("FROM ");
		sql.append("	(" + getSqlBaseItensPreFatura() + ") SUBQ, ");
		sql.append("	PRE_FATURA_SERVICO_ITEM PFIT, ");
		sql.append("	MUNICIPIO MUEX, ");
		sql.append("	PARCELA_PRECO PARC, ");
		sql.append("	DOCTO_SERVICO DOCT, ");
		sql.append("	NOTA_FISCAL_CONHECIMENTO NOTA, ");
		sql.append("	FILIAL FILI, ");
		sql.append("	FILIAL FILD, ");
		sql.append("	FILIAL FILR, ");
		sql.append("	PESSOA REME, ");
		sql.append("	PESSOA DEST, ");
		sql.append("	OCORRENCIA_PENDENCIA OCOP ");
		sql.append("WHERE ");
		sql.append("	SUBQ.ID_PRE_FATURA_SERVICO_ITEM = PFIT.ID_PRE_FATURA_SERVICO_ITEM ");
		sql.append("AND SUBQ.ID_PARCELA_PRECO = PARC.ID_PARCELA_PRECO ");
		sql.append("AND	SUBQ.ID_DOCTO_SERVICO = DOCT.ID_DOCTO_SERVICO(+) ");
		sql.append("AND	SUBQ.ID_MUNICIPIO_EXECUCAO = MUEX.ID_MUNICIPIO(+) ");
		sql.append("AND DOCT.ID_DOCTO_SERVICO = NOTA.ID_CONHECIMENTO(+) ");
		sql.append("AND DOCT.ID_FILIAL_ORIGEM = FILI.ID_FILIAL(+) ");
		sql.append("AND DOCT.ID_FILIAL_DESTINO = FILD.ID_FILIAL(+) ");
		sql.append("AND SUBQ.ID_FILIAL_REGISTRO = FILR.ID_FILIAL(+) ");
		sql.append("AND DOCT.ID_CLIENTE_REMETENTE = REME.ID_PESSOA(+) ");
		sql.append("AND DOCT.ID_CLIENTE_DESTINATARIO = DEST.ID_PESSOA(+) ");
		sql.append("AND SUBQ.ID_OCORRENCIA_PENDENCIA = OCOP.ID_OCORRENCIA_PENDENCIA(+) ");
		if (!ConstantesExpedicao.ANALITICO_ORIGINAL.equals(tpRelatorio)) {
			sql.append("AND SUBQ.TP_SITUACAO != '").append(ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_POSTERGADO).append("' ");
		}
		sql.append("ORDER BY ");
		sql.append("	dtExecucao, ");
		sql.append("	dsParcelaPreco, ");
		sql.append("	idOrdemServicoItem, ");
		sql.append("	idDoctoServico DESC ");
		
		return sql.toString();
	}
	
	public List<Map<String, Object>> findReportEmissaoPreFaturaItem(Long idPreFatura, String tpRelatorio) {
		String sql = getSqlEmissaoPreFaturaItem(tpRelatorio).toString();
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("idPreFaturaServico", idPreFatura);
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				/*00*/sqlQuery.addScalar("idPreFaturaServicoItem", Hibernate.LONG);
				/*01*/sqlQuery.addScalar("idOrdemServicoItem", Hibernate.LONG);
				/*02*/sqlQuery.addScalar("idServicoGeracaoAutomatica", Hibernate.LONG);
				/*03*/sqlQuery.addScalar("dtExecucao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				/*04*/sqlQuery.addScalar("nmRemetente", Hibernate.STRING);
				/*05*/sqlQuery.addScalar("nmDestinatario", Hibernate.STRING);
				/*06*/sqlQuery.addScalar("nmMunicipioExecucao", Hibernate.STRING);
				/*07*/sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
				/*08*/sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
				/*09*/sqlQuery.addScalar("sgFilial", Hibernate.STRING);
				/*10*/sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);
				/*11*/sqlQuery.addScalar("sgFilialDestino", Hibernate.STRING); 
				/*12*/sqlQuery.addScalar("vlFrete", Hibernate.BIG_DECIMAL);
				/*13*/sqlQuery.addScalar("nrNotaFiscal", Hibernate.STRING);
				/*14*/sqlQuery.addScalar("qtVolumes", Hibernate.INTEGER);
				/*15*/sqlQuery.addScalar("psReferencia", Hibernate.BIG_DECIMAL);
				/*16*/sqlQuery.addScalar("vlMercadoria", Hibernate.BIG_DECIMAL);
				/*17*/sqlQuery.addScalar("dsParcelaPreco", Hibernate.STRING);
				/*18*/sqlQuery.addScalar("dsOcorrencia", Hibernate.STRING);
				/*19*/sqlQuery.addScalar("qtDias", Hibernate.INTEGER);
				/*20*/sqlQuery.addScalar("vlCobranca", Hibernate.BIG_DECIMAL);
				/*21*/sqlQuery.addScalar("quantidade", Hibernate.INTEGER);
				/*22*/sqlQuery.addScalar("sgFilialOrdermServico", Hibernate.STRING);
				/*23*/sqlQuery.addScalar("nrOrdermServico", Hibernate.STRING);
				/*24*/sqlQuery.addScalar("email", Hibernate.STRING);
				/*25*/sqlQuery.addScalar("vlCobrancaAjustado", Hibernate.BIG_DECIMAL);
				/*26*/sqlQuery.addScalar("tpSituacao", Hibernate.STRING);
                /*27*/sqlQuery.addScalar("destNrIdentificacao", Hibernate.STRING);
			}
		};
		
		List<Object[]> sqlResult = getAdsmHibernateTemplate().findBySql(sql, criteria, csq);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		for(Object[] tuple : sqlResult) {
			Map<String, Object> map = new HashMap<String, Object>();
            
			map.put("nrNotaFiscal", tuple[13]);
            map.put("qtVolumes", tuple[14]);
            map.put("psReferencia", tuple[15]);
            map.put("vlMercadoria", tuple[16]);
            map.put("dsParcelaPreco", tuple[17]);
            map.put("dsOcorrencia", tuple[18]);
            map.put("qtDias", tuple[19]);
				
			String nrDoctoServico=null;
			
            if (tuple[10] != null) {
				nrDoctoServico = 
					(String)tuple[8]+"-"+
					(String)tuple[9]+" "+
					FormatUtils.formataNrDocumento((String)tuple[10], (String)tuple[8]);
			}
				

			map.put("nmRemetente", tuple[4]);
			map.put("nmDestinatario", tuple[5]);
			map.put("nmMunicipioExecucao", tuple[6]);
					
			map.put("nrDoctoServico", nrDoctoServico);
			map.put("sgFilialDestino", tuple[11]);
			map.put("vlFrete", tuple[12]);
					
			map.put("dsParcelaPreco", tuple[17]);
			map.put("dsOcorrencia", tuple[18]);
                      
			map.put("qtDias", tuple[19]);
			map.put("vlCobranca", tuple[20]);
						
			map.put("quantidade", tuple[21]);
				
            if (tuple[23] != null) {
				map.put("ordermServico", tuple[22] + " - " + FormatUtils.formataNrDocumento((String)tuple[23], ConstantesExpedicao.ORDEM_SERVICO));
			}
					
			map.put("email", tuple[24]);
			map.put("vlCobrancaAjustado", tuple[25]);
			map.put("tpSituacao", tuple[26]);
            map.put("destNrIdentificacao", tuple[27]);
            
			result.add(map);
		}
		
		return result;
	}
	
	private String getSqlEmissaoPreFatura() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("	SUBQ.ID_ORDEM_SERVICO_ITEM idOrdemServicoItem, ");
		sql.append("	PARC.ID_PARCELA_PRECO idParcelaPreco, ");
		sql.append("	VI18N(PARC.DS_PARCELA_PRECO_I) dsParcelaPreco, ");
		sql.append("	SUBQ.VL_COBRANCA vlTotal, ");
		sql.append("	SUBQ.VL_COBRANCA_AJUSTADO vlAprovado, ");
		sql.append("	SUBQ.TP_SITUACAO tpSituacao ");
		sql.append("FROM ");
		sql.append("	(" + getSqlBaseItensPreFatura() + ") SUBQ, ");
		sql.append("	PARCELA_PRECO PARC ");
		sql.append("WHERE ");
		sql.append("	SUBQ.ID_PARCELA_PRECO = PARC.ID_PARCELA_PRECO ");
		sql.append("AND SUBQ.TP_SITUACAO != '").append(ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_POSTERGADO).append("' ");
		sql.append("ORDER BY ");
		sql.append("	idOrdemServicoItem ");

		return sql.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> findReportEmissaoPreFatura(Long idPreFatura) {
		StringBuilder sql = new StringBuilder(getSqlEmissaoPreFatura());
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("idPreFaturaServico", idPreFatura);
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				/*00*/sqlQuery.addScalar("idOrdemServicoItem", Hibernate.LONG);
				/*01*/sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);
				/*02*/sqlQuery.addScalar("dsParcelaPreco", Hibernate.STRING);
				/*03*/sqlQuery.addScalar("vlTotal", Hibernate.BIG_DECIMAL);
				/*04*/sqlQuery.addScalar("vlAprovado", Hibernate.BIG_DECIMAL);
				/*05*/sqlQuery.addScalar("tpSituacao", Hibernate.STRING);
			}
		};
		
		List<Object[]> sqlResult = getAdsmHibernateTemplate().findBySql(sql.toString(), criteria, csq);
		Map<Long, Map<String, Object>> result = new HashMap<Long, Map<String, Object>>();
		
		Map<String, Object> map = null;
		Long idAgrupador = null;		
		
		for(Object[] tuple : sqlResult) {			
			Long idOrdemServicoItem = (Long) tuple[0];
			if(idAgrupador == null || !idAgrupador.equals(idOrdemServicoItem)) {				
				Long idParcelaPreco = (Long) tuple[1];
				String dsParcelaPreco = (String) tuple[2];
				BigDecimal vlTotalCorrente = (BigDecimal) tuple[3];
				BigDecimal vlAprovadoCorrente = (BigDecimal) tuple[4];
				String tpSituacao = (String) tuple[5];
				
				idAgrupador = idOrdemServicoItem;
				Integer qtServicos = 0;
				BigDecimal vlTotal = BigDecimal.ZERO;
				BigDecimal vlTotalAprovado = null;
				
				if(!result.containsKey(idParcelaPreco)) {
					map =  new HashMap<String, Object>();
					map.put("dsParcelaPreco", dsParcelaPreco);
				} else {
					map = result.get(idParcelaPreco);
					qtServicos = (Integer)map.get("qtServico");
					vlTotal = (BigDecimal)map.get("vlTotal");
					vlTotalAprovado = (BigDecimal)map.get("vlAprovado");
				}
								
				map.put("qtServico", qtServicos+1);
				map.put("vlTotal", vlTotal.add(vlTotalCorrente));
				map.put("tpSituacao", tpSituacao);
				sumVlAprovado(map, vlTotalCorrente, vlAprovadoCorrente, tpSituacao, vlTotalAprovado);				
				
				result.put(idParcelaPreco, map);
			}
		}
		
		return new ArrayList(result.values());
	}

	public boolean executeExistsPreFaturaServicoItemByOrdemServicoItem(Long idOrdemServicoItem, DomainValue tpSituacao) {
		StringBuilder sql = getExistsPreFaturaServicoItemByTpSituacao();
		sql.append("AND ID_ORDEM_SERVICO_ITEM = :idOrdemServicoItem");

		Map parameters = new HashMap();
		parameters.put("idOrdemServicoItem", idOrdemServicoItem);
		parameters.put("tpSituacao", tpSituacao.getValue());

		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), parameters) > 0;
	}

	public boolean executeExistsPreFaturaServicoItemByServicoGeracaoAutomatica(Long idServicoGeracaoAutomatica, DomainValue tpSituacao) {
		StringBuilder sql = getExistsPreFaturaServicoItemByTpSituacao();
		sql.append("AND ID_SERVICO_GERACAO_AUTOMATICA = :idServicoGeracaoAutomatica");

		Map parameters = new HashMap();
		parameters.put("idServicoGeracaoAutomatica", idServicoGeracaoAutomatica);
		parameters.put("tpSituacao", tpSituacao.getValue());

		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), parameters) > 0;
	}

	private StringBuilder getExistsPreFaturaServicoItemByTpSituacao() {
		StringBuilder sql = new StringBuilder();
		sql.append("FROM PRE_FATURA_SERVICO_ITEM ")
		  .append("WHERE 1=1 ")
		  .append("AND TP_SITUACAO = :tpSituacao ");
		return sql;
	}

	private void sumVlAprovado(Map<String, Object> map, BigDecimal vlTotalCorrente, BigDecimal vlAprovadoCorrente,
			String tpSituacao, BigDecimal vlTotalAprovado) {
		if (BigDecimalUtils.hasValue(vlAprovadoCorrente) && BigDecimalUtils.hasValue(vlTotalAprovado)) {
			map.put("vlAprovado", vlTotalAprovado.add(vlAprovadoCorrente));
		} else if(!BigDecimalUtils.hasValue(vlTotalAprovado)) {
			map.put("vlAprovado", vlAprovadoCorrente);
		}
		
		if (ConstantesExpedicao.TP_ACAO_APROVAR_ITEM_PRE_FATURA.equals(tpSituacao)
				|| ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_INFORMADO.equals(tpSituacao)) {
			if (BigDecimalUtils.hasValue(vlTotalAprovado)) {
				map.put("vlAprovado", vlTotalAprovado.add(vlTotalCorrente));
			}
		}
	}
}