package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.PreFaturaServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.FormatUtils;

public class PreFaturaServicoDAO extends BaseCrudDao<PreFaturaServico, Long> {
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return PreFaturaServico.class;
	}
		
	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findPaginated(PaginatedQuery paginatedQuery) {
		TypedFlatMap criteria = new TypedFlatMap(paginatedQuery.getCriteria());
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct new map( ");
		sb.append("		div.idDivisaoCliente as idDivisaoCliente, ");
		sb.append("		div.dsDivisaoCliente as dsDivisaoCliente, ");
		sb.append("		pfs.filialCobranca.idFilial as idFilialCobranca,  ");
		sb.append("		pfs.filialCobranca.sgFilial as sgFilialCobranca,  ");
		sb.append("		pfs.nrPreFatura as nrPreFatura,  ");
		sb.append("		pfs.idPreFaturaServico as idPreFaturaServico,  ");
		sb.append("		ct.idCliente as idCliente, ");
		sb.append("		pt.nrIdentificacao as nrIdentificacao, ");
		sb.append("		pt.tpIdentificacao as tpIdentificacao, ");
		sb.append("		pt.nmPessoa as nmPessoa, ");
		sb.append("		pfs.dhGeracao as dhGeracao, ");
		sb.append("		pfs.tpSituacao as tpSituacao, ");
		sb.append("		pfs.vlTotal as vlTotal, ");
		sb.append("		pfs.usuario.usuarioADSM.nmUsuario as nmUsuario, ");
		//LMS - 7113
		sb.append("		os.nrOrdemServico as nrOrdemServico, ");
		
		// LMS-7220
		sb.append("		usrADSM.nmUsuario as nmUsuarioFinalizacao ");
		
		sb.append(" )");
		sb.append(mountHqlPaginated(criteria));
		
		sb.append(" order by pfs.filialCobranca.sgFilial, pfs.nrPreFatura ");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, sb.toString());
	}

	private StringBuilder mountHqlPaginated(TypedFlatMap criteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("from " + getPersistentClass().getName() + " pfs ");		
		
		sb.append("inner join pfs.clienteTomador ct ");
		sb.append("inner join ct.pessoa pt ");
		sb.append("left join pfs.divisaoCliente div ");
		
		// LMS-7220
		sb.append("left join pfs.usuarioFinalizacao usrFin ");
		sb.append("left join usrFin.usuarioADSM usrADSM ");	
		
		// LMS-7113
		sb.append("left join pfs.preFaturaServicoItens pfsi ");
		sb.append("left join pfsi.ordemServicoItem osi ");
		sb.append("left join osi.ordemServico os ");
		
		sb.append(" where 1 = 1 ");
				
		if (criteria.getLong("idFilialCobranca") != null && criteria.getLong("nrPreFatura") != null) {
			sb.append(" and pfs.filialCobranca.idFilial = :idFilialCobranca ");
			sb.append(" and pfs.nrPreFatura = :nrPreFatura ");
		} else {
			if (criteria.getLong("idFilialCobranca") != null) {
				sb.append(" and pfs.filialCobranca.idFilial = :idFilialCobranca ");
			}
			if (criteria.getLong("nrPreFatura") != null) {
				sb.append(" and pfs.nrPreFatura = :nrPreFatura ");
			}
			if (criteria.getLong("idCliente") != null) {
				sb.append(" and ct.idCliente = :idCliente ");
			}
			if (criteria.getString("tpSituacao") != null) {
				sb.append(" and pfs.tpSituacao = :tpSituacao ");
			}
			
			if (criteria.getYearMonthDay("dtPeriodoInicial") != null) {
				sb.append(" and TRUNC(pfs.dhGeracao.value) >= :dtPeriodoInicial ");
			}
			
			if (criteria.getYearMonthDay("dtPeriodoFinal") != null) {
				sb.append(" and TRUNC(pfs.dhGeracao.value) <= :dtPeriodoFinal ");
			}
		}
		return sb;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(distinct pfs.idPreFaturaServico) ");
		sb.append(mountHqlPaginated(criteria));
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sb.toString(),criteria);
    	return result.intValue();
	}
	
	private String getSqlGeracaoPreFatura(Map<String, Object> criteria) {
		StringBuilder sql = new StringBuilder();
		
		/* QUERY QUE RETORNA OS ITENS DA ORDEM DE SERVI�O */		
		sql.append("		SELECT ");
		sql.append("			OSIT.ID_ORDEM_SERVICO_ITEM AS idItem, ");
		sql.append("			'OS' AS tpDocumento, ");
		sql.append("			FILR.SG_FILIAL AS sgFilial, ");
		sql.append("			ORSE.NR_ORDEM_SERVICO AS nrDocumento, ");
		sql.append("			PPOS.ID_PARCELA_PRECO AS idParcelaPreco, ");
		sql.append("			VI18N(PPOS.DS_PARCELA_PRECO_I) AS dsParcelaPreco, ");
		sql.append("			OSIT.DS_SERVICO AS dsServico, ");
		sql.append("			CLIE.ID_FILIAL_COBRANCA AS idFilialCobranca, ");
		sql.append("			PESS.ID_PESSOA AS idCliente, ");
		sql.append("			PESS.TP_IDENTIFICACAO AS tpIdentificacao, ");
		sql.append("			PESS.NR_IDENTIFICACAO AS nrIdentificacao, ");			
		sql.append("			PESS.NM_PESSOA AS nmPessoa, ");
		sql.append("			DIVI.ID_DIVISAO_CLIENTE AS idDivisaoCliente, ");
		sql.append("			DIVI.DS_DIVISAO_CLIENTE AS dsDivisaoCliente, ");		
		sql.append("			NVL(OSIT.VL_NEGOCIADO, OSIT.VL_TABELA) as vlCobranca, ");
		sql.append("			OSIT.VL_TABELA as vlTabela, ");
		sql.append("			OSIT.DT_EXECUCAO as dtSolicitacao ");
		sql.append("		FROM ");
		sql.append("			ORDEM_SERVICO ORSE, ");
		sql.append("			FILIAL FILR, ");
		sql.append("			ORDEM_SERVICO_ITEM OSIT, ");
		sql.append("			PARCELA_PRECO PPOS, ");
		sql.append("			CLIENTE CLIE, ");
		sql.append("			PESSOA PESS, ");
		sql.append("			DIVISAO_CLIENTE DIVI ");
		sql.append("		WHERE ");
		sql.append("			ORSE.ID_ORDEM_SERVICO = OSIT.ID_ORDEM_SERVICO ");
		sql.append("		AND	ORSE.ID_FILIAL_REGISTRO = FILR.ID_FILIAL ");
		sql.append("		AND	ORSE.ID_CLIENTE_TOMADOR = CLIE.ID_CLIENTE ");
		sql.append("		AND	CLIE.ID_CLIENTE = PESS.ID_PESSOA ");
		sql.append("		AND	ORSE.ID_DIVISAO_CLIENTE = DIVI.ID_DIVISAO_CLIENTE(+) ");
		sql.append("		AND	OSIT.ID_PARCELA_PRECO = PPOS.ID_PARCELA_PRECO ");
		sql.append("		AND ORSE.TP_SITUACAO IN ('A','P') ");
		sql.append("		AND OSIT.BL_FATURADO = 'N' ");
		sql.append("		AND OSIT.BL_SEM_COBRANCA = 'N' ");
		
		if ((Boolean) criteria.get("blClienteSemNegociacao")) {
			sql.append("	AND OSIT.VL_TABELA = 0 ");
		}else{
			sql.append("	AND OSIT.VL_TABELA > 0 ");
		}
		
		if(criteria.containsKey("idsPerfisServicosAdicionais")) {
			sql.append("	AND OSIT.ID_PARCELA_PRECO not in (:idsPerfisServicosAdicionais) ");
		}
		
		if (criteria.get("idsFiliaisUsuario") != null) {
			sql.append("	AND CLIE.ID_FILIAL_COBRANCA in (:idsFiliaisUsuario) ");
		}
		if (criteria.get("idCliente") != null) {
			sql.append("	AND ORSE.ID_CLIENTE_TOMADOR = :idCliente ");
		}
		if (criteria.get("idDivisaoCliente") != null) {
			sql.append("	AND DIVI.ID_DIVISAO_CLIENTE = :idDivisaoCliente ");
		}
		if(criteria.get("idParcelaPreco") != null) {
			sql.append("	AND PPOS.ID_PARCELA_PRECO = :idParcelaPreco ");
		}
		if(criteria.get("dtPeriodoInicial") != null) {
			sql.append("	AND OSIT.DT_EXECUCAO >= :dtPeriodoInicial ");
		}
		if(criteria.get("dtPeriodoFinal") != null) {
			sql.append("	AND OSIT.DT_EXECUCAO <= :dtPeriodoFinal ");
		}
		
		sql.append("		UNION ALL ");		
		
		/* QUERY QUE RETORNO OS ITENS DA GERA��O AUTOM�TICA */
		sql.append("		SELECT ");
		sql.append("             /*+ index(sega, SGAU_DOCTDH_IDX) */ ");
		sql.append("			SEGA.ID_SERVICO_GERACAO_AUTOMATICA AS idItem, ");
		sql.append("			DOCT.TP_DOCUMENTO_SERVICO AS tpDocumento, ");
		sql.append("			FILO.SG_FILIAL AS sgFilial, ");
		sql.append("			DOCT.NR_DOCTO_SERVICO AS nrDocumento, ");
		sql.append("			PPGA.ID_PARCELA_PRECO AS idParcelaPreco, ");
		sql.append("			VI18N(PPGA.DS_PARCELA_PRECO_I) AS dsParcelaPreco, ");
		sql.append("			'' AS dsServico, ");
		sql.append("			CLIE.ID_FILIAL_COBRANCA AS idFilialCobranca, ");
		sql.append("			PESS.ID_PESSOA AS idCliente, ");
		sql.append("			PESS.TP_IDENTIFICACAO AS tpIdentificacao, ");
		sql.append("			PESS.NR_IDENTIFICACAO AS nrIdentificacao, ");			
		sql.append("			PESS.NM_PESSOA AS nmPessoa, ");
		sql.append("			DIVI.ID_DIVISAO_CLIENTE AS idDivisaoCliente, ");
		sql.append("			DIVI.DS_DIVISAO_CLIENTE AS dsDivisaoCliente, ");
		sql.append("			SEGA.VL_SERVICO_ADICIONAL as vlCobranca, ");
		sql.append("			SEGA.VL_TABELA as vlTabela, ");
		sql.append("			TRUNC(SEGA.DH_CALCULO) as dtSolicitacao ");
		sql.append("		FROM ");
		sql.append("			SERVICO_GERACAO_AUTOMATICA SEGA, ");
		sql.append("			PARCELA_PRECO PPGA, ");
		sql.append("			DOCTO_SERVICO DOCT, ");
		sql.append("			FILIAL FILO, ");
		sql.append("			DEVEDOR_DOC_SERV DEVE, ");
		sql.append("			CLIENTE CLIE, ");
		sql.append("			DIVISAO_CLIENTE DIVI, ");
		sql.append("			PESSOA PESS ");
		sql.append("		WHERE ");
		sql.append("			SEGA.ID_PARCELA_PRECO = PPGA.ID_PARCELA_PRECO ");
		sql.append("		AND	SEGA.ID_DOCTO_SERVICO = DOCT.ID_DOCTO_SERVICO ");		
		sql.append("        AND  SEGA.ID_SERVICO_GERACAO_AUTOMATICA NOT IN (SELECT ID_SERVICO_GERACAO_AUTOMATICA ");
		sql.append("                          FROM PRE_FATURA_SERVICO_ITEM pfsi ");
		sql.append("                          WHERE pfsi.TP_SITUACAO IN ('A','D','R','I') ");
		sql.append("                          AND pfsi.ID_SERVICO_GERACAO_AUTOMATICA is not null ");
		sql.append("                          ) ");	
		sql.append("		AND	DOCT.ID_DOCTO_SERVICO = DEVE.ID_DOCTO_SERVICO ");
		sql.append("		AND	DOCT.ID_FILIAL_ORIGEM = FILO.ID_FILIAL ");
		sql.append("		AND	DEVE.ID_CLIENTE = CLIE.ID_CLIENTE ");
		sql.append("		AND	DOCT.ID_DIVISAO_CLIENTE = DIVI.ID_DIVISAO_CLIENTE(+) ");
		sql.append("		AND	CLIE.ID_CLIENTE = PESS.ID_PESSOA ");
		sql.append("        AND SEGA.TP_EXECUCAO <> 'R' ");
		
		if ((Boolean) criteria.get("blClienteSemNegociacao")) {
			sql.append("		AND SEGA.BL_SEM_COBRANCA = 'S' ");
			sql.append("        AND SEGA.BL_FATURADO = 'S' ");
		}else{
			sql.append("		AND SEGA.BL_SEM_COBRANCA = 'N' ");
			sql.append("        AND SEGA.BL_FATURADO = 'N' ");
		}
		
		// LSMA - 3470
		if(criteria.containsKey("idsPerfisServicosAdicionais")) {
			sql.append("	AND SEGA.ID_PARCELA_PRECO not in (:idsPerfisServicosAdicionais) ");
		}
		
		if (criteria.get("idsFiliaisUsuario") != null) {
			sql.append("	AND CLIE.ID_FILIAL_COBRANCA in (:idsFiliaisUsuario) ");
		}
		if (criteria.get("idCliente") != null) {
			sql.append("	AND CLIE.ID_CLIENTE = :idCliente ");
		}
		if (criteria.get("idDivisaoCliente") != null) {
			sql.append("	AND DIVI.ID_DIVISAO_CLIENTE = :idDivisaoCliente ");
		}
		if(criteria.get("idParcelaPreco") != null) {
			sql.append("	AND PPGA.ID_PARCELA_PRECO = :idParcelaPreco ");
		}
		if(criteria.get("dtPeriodoInicial") != null) {           
          sql.append("    AND SEGA.DH_CALCULO >=  to_timestamp_tz(TO_CHAR(:dtPeriodoInicial,'YYYY-MM-DD')||' 00:00:00,000000000 -03:00','YY/MM/DD HH24:MI:SS,FF TZR')");
          										
		}
        if(criteria.get("dtPeriodoFinal") != null) {           
           sql.append("   AND SEGA.DH_CALCULO <=  to_timestamp_tz(TO_CHAR(  :dtPeriodoFinal,'YYYY-MM-DD')||' 23:59:00,000000000 -03:00','YY/MM/DD HH24:MI:SS,FF TZR') ");
        }
		return sql.toString();
	}
	
	private String getGroupedSqlGeracaoPreFatura(Map<String, Object> criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("	idCliente, ");
		sql.append("	tpIdentificacao, ");
		sql.append("	nrIdentificacao, ");			
		sql.append("	nmPessoa, ");
		sql.append("	idDivisaoCliente, ");
		sql.append("	dsDivisaoCliente, ");
		sql.append("	count(*) as qtTotal, ");
		sql.append("	sum(vlCobranca) as vlTotal, ");
		sql.append("	min(dtSolicitacao) as dtExecucaoMin, ");
		sql.append("	max(dtSolicitacao) as dtExecucaoMax ");
		
		sql.append("FROM (");
		sql.append(getSqlGeracaoPreFatura(criteria));
		sql.append(") ");
		
		sql.append("GROUP BY ");					
		sql.append("	idCliente, ");
		sql.append("	tpIdentificacao, ");
		sql.append("	nrIdentificacao, ");			
		sql.append("	nmPessoa, ");
		sql.append("	idDivisaoCliente, ");
		sql.append("	dsDivisaoCliente ");
		sql.append("ORDER BY ");
		sql.append("	nmPessoa, ");
		sql.append("	dsDivisaoCliente ");
		
		return sql.toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage<Map<String, Object>> findPaginatedGeracaoPreFatura(PaginatedQuery paginatedQuery) {		
		StringBuilder sql = new StringBuilder(getGroupedSqlGeracaoPreFatura(paginatedQuery.getCriteria()));		
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {			
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idCliente",Hibernate.LONG);
				sqlQuery.addScalar("tpIdentificacao", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacao", Hibernate.STRING);
				sqlQuery.addScalar("nmPessoa", Hibernate.STRING);
				sqlQuery.addScalar("idDivisaoCliente", Hibernate.LONG);
				sqlQuery.addScalar("dsDivisaoCliente", Hibernate.STRING);
				sqlQuery.addScalar("qtTotal", Hibernate.INTEGER);
				sqlQuery.addScalar("vlTotal", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dtExecucaoMin", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dtExecucaoMax", Hibernate.custom(JodaTimeYearMonthDayUserType.class));							
			}			
		};
		
		ResultSetPage rs = getAdsmHibernateTemplate().findPaginatedBySql(
				sql.toString(), 
				paginatedQuery.getCurrentPage(), 
				paginatedQuery.getPageSize(), 
				paginatedQuery.getCriteria(),
				csq);
		
		FilterResultSetPage filter = new FilterResultSetPage(rs) {			
			@Override
			public Map<String, Object> filterItem(Object item) {
				Object[] tuple = (Object[]) item;				
				Map<String, Object> retorno = new HashMap<String, Object>();							
				retorno.put("idCliente", tuple[0]);
				retorno.put("tpIdentificacao", tuple[1]);
				retorno.put("nrIdentificacao", tuple[2]);
				retorno.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao((String)tuple[1], (String)tuple[2]));
				retorno.put("nmPessoa", tuple[3]);
				retorno.put("idDivisaoCliente", tuple[4]);
				retorno.put("dsDivisaoCliente", tuple[5]);
				retorno.put("qtTotal", tuple[6]);
				retorno.put("vlTotal", tuple[7]);
				retorno.put("dtExecucaoMin", tuple[8]);
				retorno.put("dtExecucaoMax", tuple[9]);	
				return retorno;
			}
		};
		
		return (ResultSetPage<Map<String, Object>>)filter.doFilter();
	}
	
	@SuppressWarnings("unchecked")
	public Integer getRowCountGeracaoPreFatura(TypedFlatMap criteria) {		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) as rowCount ");
		sql.append("FROM ( ");
		sql.append(getGroupedSqlGeracaoPreFatura(criteria));
		sql.append(")");
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), criteria);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage<Map<String, Object>> findPaginatedGeracaoPreFaturaDetalhamento(PaginatedQuery paginatedQuery) {		
		StringBuilder sql = new StringBuilder(getSqlGeracaoPreFatura(paginatedQuery.getCriteria()));		
				
		sql.append("ORDER BY dtSolicitacao, dsParcelaPreco, vlCobranca ");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idItem", Hibernate.LONG);
				sqlQuery.addScalar("tpDocumento", Hibernate.STRING);
				sqlQuery.addScalar("sgFilial", Hibernate.STRING);
				sqlQuery.addScalar("nrDocumento", Hibernate.LONG);
				sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);
				sqlQuery.addScalar("dsParcelaPreco", Hibernate.STRING);
				sqlQuery.addScalar("dtSolicitacao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("vlCobranca", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vlTabela", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("dsServico", Hibernate.STRING);
				sqlQuery.addScalar("idFilialCobranca", Hibernate.LONG);				
			}			
		};
		
		ResultSetPage rs = getAdsmHibernateTemplate().findPaginatedBySql(
				sql.toString(), 
				paginatedQuery.getCurrentPage(), 
				paginatedQuery.getPageSize(), 
				paginatedQuery.getCriteria(),
				csq);
		
		FilterResultSetPage filter = new FilterResultSetPage(rs) {			
			@Override
			public Map<String, Object> filterItem(Object item) {
				Object[] tuple = (Object[]) item;				
				
				Map<String, Object> retorno = new HashMap<String, Object>();								
				retorno.put("idItem", tuple[0]);
				retorno.put("tpDocumento", tuple[1]);
				if("OS".equals(tuple[1])) {
					retorno.put("sgFilialOrdemServico", tuple[2]);
					retorno.put("nrOrdemServico", tuple[3]);
					retorno.put("nrOrdemServicoFormatado", FormatUtils.formataNrDocumento(String.valueOf((Long)tuple[3]), (String)tuple[1]));	
				} else {
					retorno.put("sgFilialDoctoServico", tuple[2]);
					retorno.put("nrDoctoServico", tuple[3]);
					retorno.put("nrDoctoServicoFormatado", FormatUtils.formataNrDocumento(String.valueOf((Long)tuple[3]), (String)tuple[1]));
				}
				retorno.put("idParcelaPreco", tuple[4]);
				retorno.put("dsParcelaPreco", tuple[5]);
				retorno.put("dtSolicitacao", tuple[6]);
				retorno.put("vlCobranca", tuple[7]);
				retorno.put("vlTabela", tuple[8]);
				retorno.put("dsServico", tuple[9]);
				retorno.put("idFilialCobranca", tuple[10]);
				return retorno;
			}
		};
		
		return (ResultSetPage<Map<String, Object>>)filter.doFilter();
	}
	
	private String getSubqueryRegionalPlanilhaServicosAdicionais(String selectField, String idFilialField) {
		StringBuilder sql = new StringBuilder();
		sql.append("(");
		sql.append("	SELECT ");
		sql.append("" + 	selectField);
		sql.append("	FROM ");
		sql.append("    	REGIONAL_FILIAL REFI, ");
		sql.append("    	REGIONAL REGI ");
		sql.append("	WHERE ");
		sql.append("	" + idFilialField + " = REFI.ID_FILIAL ");
		sql.append("    AND REGI.ID_REGIONAL = REFI.ID_REGIONAL ");
		sql.append("    AND REFI.DT_VIGENCIA_INICIAL <= SYSDATE ");		
		sql.append("	AND (REFI.DT_VIGENCIA_FINAL >= SYSDATE OR REFI.DT_VIGENCIA_FINAL IS NULL) ");		
		sql.append(")");
		return sql.toString();
	}
	
	private String getSubqueryPromotorPlanilhaServicosAdicionais(String selectField, String idClienteField, String tpModalField) {
		StringBuilder sql = new StringBuilder();
		sql.append("(");
		sql.append("	SELECT ");
		sql.append("" + 	selectField);		
		sql.append("	FROM "); 
		sql.append("		PROMOTOR_CLIENTE PRCL, ");
		sql.append("		USUARIO_ADSM USUA ");
		sql.append("	WHERE ");     
		sql.append("	" + idClienteField + " = PRCL.ID_CLIENTE ");
		sql.append("	AND PRCL.ID_USUARIO = USUA.ID_USUARIO ");
		sql.append("	AND (" + tpModalField + " = PRCL.TP_MODAL OR PRCL.TP_MODAL IS NULL) ");
		sql.append("	AND PRCL.DT_INICIO_PROMOTOR <= SYSDATE ");
		sql.append("	AND (PRCL.DT_FIM_PROMOTOR >= SYSDATE OR PRCL.DT_FIM_PROMOTOR IS NULL) ");
		sql.append("    AND ROWNUM = 1 ");
		sql.append(")");
		return sql.toString();
	}
	
	private String getSubqueryOrdemServicoJoinDoctoServico() {
		StringBuilder sql = new StringBuilder();
		sql.append("(");
		sql.append("	SELECT ");
		sql.append("		ORSE.ID_ORDEM_SERVICO, ");
		sql.append("		NVL(DSOS.ID_DOCTO_SERVICO,NVL(DSME.ID_DOCTO_SERVICO, NVL(DSMV.ID_DOCTO_SERVICO,DSMC.ID_DOCTO_SERVICO))) AS ID_DOCTO_SERVICO ");
		sql.append("	FROM ");
		sql.append("		ORDEM_SERVICO ORSE, ");
		sql.append("		( ");
		sql.append("			SELECT ORSE.ID_ORDEM_SERVICO, OSDS.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO ");
		sql.append("          	FROM ORDEM_SERVICO_DOCUMENTO OSDS, ORDEM_SERVICO ORSE ");
		sql.append("         	WHERE ORSE.ID_ORDEM_SERVICO = OSDS.ID_ORDEM_SERVICO ");
		sql.append("       	) DSOS, ");
		sql.append("        ( ");
		sql.append("        	SELECT ORSE.ID_ORDEM_SERVICO, MEDO.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO ");
		sql.append("           	FROM MANIFESTO_ENTREGA_DOCUMENTO MEDO, ORDEM_SERVICO ORSE ");
		sql.append("           	WHERE ORSE.ID_MANIFESTO = MEDO.ID_MANIFESTO_ENTREGA ");
		sql.append("        ) DSME, ");
		sql.append("        ( ");
		sql.append("			SELECT ORSE.ID_ORDEM_SERVICO, MVDO.ID_CONHECIMENTO AS ID_DOCTO_SERVICO ");
		sql.append("			FROM MANIFESTO_NACIONAL_CTO MVDO, ORDEM_SERVICO ORSE ");
		sql.append("			WHERE ORSE.ID_MANIFESTO = MVDO.ID_MANIFESTO_VIAGEM_NACIONAL ");
		sql.append("		) DSMV, ");
		sql.append("		( ");
		sql.append("			SELECT ORSE.ID_ORDEM_SERVICO, DOCT.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO ");
		sql.append("			FROM PEDIDO_COLETA PECO, DOCTO_SERVICO DOCT, ORDEM_SERVICO ORSE ");
		sql.append("			WHERE ORSE.ID_MANIFESTO_COLETA = PECO.ID_MANIFESTO_COLETA ");
		sql.append("			AND DOCT.ID_PEDIDO_COLETA = PECO.ID_PEDIDO_COLETA ");
		sql.append("		) DSMC ");
		sql.append("	WHERE ");
		sql.append("		ORSE.ID_ORDEM_SERVICO = DSOS.ID_ORDEM_SERVICO(+) ");
		sql.append("	AND ORSE.ID_ORDEM_SERVICO = DSME.ID_ORDEM_SERVICO(+) ");
		sql.append("	AND ORSE.ID_ORDEM_SERVICO = DSMV.ID_ORDEM_SERVICO(+) ");
		sql.append("	AND ORSE.ID_ORDEM_SERVICO = DSMC.ID_ORDEM_SERVICO(+) ");
		sql.append(")");
		return sql.toString();
	}
	
	private String getSubqueryOrdemServicoItemJoinNotaFiscalServico() {
		StringBuilder sql = new StringBuilder();
		sql.append("(");		
		sql.append("SELECT ");	
		sql.append("	FINF.SG_FILIAL, ");
		sql.append("	NFSE.TP_NOTA_FISCAL_SERVICO, ");
		sql.append("  	NFSE.NR_NOTA_FISCAL_SERVICO, ");
		sql.append("  	OSIT.ID_ORDEM_SERVICO_ITEM, ");
		sql.append("  	NFSD.ID_DOCTO_SERVICO ");
		sql.append("FROM ");
		sql.append("  	NOTA_FISCAL_SERVICO_DOCUMENTO NFSD, ");
		sql.append("  	PRE_FATURA_SERVICO_ITEM PFSI, ");
		sql.append("  	ORDEM_SERVICO_ITEM OSIT, ");
		sql.append("  	NOTA_FISCAL_SERVICO NFSE, ");
		sql.append("  	FILIAL FINF ");
		sql.append("WHERE ");
		sql.append("	NFSD.ID_PRE_FATURA_SERVICO_ITEM = PFSI.ID_PRE_FATURA_SERVICO_ITEM ");
		sql.append("AND PFSI.ID_ORDEM_SERVICO_ITEM = OSIT.ID_ORDEM_SERVICO_ITEM ");
		sql.append("AND NFSD.ID_NOTA_FISCAL_SERVICO = NFSE.ID_NOTA_FISCAL_SERVICO ");
		sql.append("AND NFSE.ID_FILIAL_ORIGEM = FINF.ID_FILIAL ");
		sql.append(")");
		
		return sql.toString();
	}
	
	private String getSqlGeracaoPlanilhaServicosAdicionaisOS(Map<String, Object> criteria) {
		StringBuilder sql = new StringBuilder();
		
		/* QUERY QUE RETORNA OS ITENS DE ORDEM DE SERVI�O */
		sql.append("SELECT ");
		sql.append("	DOCT.TP_DOCUMENTO_SERVICO AS tpDoctoServico, ");
		sql.append("	FIDO.SG_FILIAL AS sgFilialDoctoServico, ");
		sql.append("	DOCT.NR_DOCTO_SERVICO AS nrDoctoServico, ");
		sql.append("" + getSubqueryRegionalPlanilhaServicosAdicionais("REGI.SG_REGIONAL", "ORSE.ID_FILIAL_EXECUCAO") + " AS sgRegionalExecucao, ");
		sql.append("	FIEX.SG_FILIAL AS sgFilialExecucao, ");
		sql.append("	SERV.TP_MODAL AS tpModal, "); 
		sql.append("" + getSubqueryPromotorPlanilhaServicosAdicionais("USUA.NM_USUARIO", "ORSE.ID_CLIENTE_TOMADOR", "SERV.TP_MODAL") + " AS nmUsuarioPromotor, ");
		sql.append("	PESS.NR_IDENTIFICACAO AS nrIdentificacaoTomador, ");
		sql.append("	PESS.NM_PESSOA AS nmPessoaTomador, ");
		sql.append("	OSIT.DT_EXECUCAO as dtExecucao, ");
		sql.append("	VI18N(PARC.DS_PARCELA_PRECO_I) AS dsParcelaPreco, ");
		sql.append("	ORSE.TP_SITUACAO as tpSituacao, ");
		sql.append("	OSIT.VL_TABELA as vlTabela, ");
		sql.append("	OSIT.VL_CUSTO as vlCusto, ");
		sql.append("	NVL(OSIT.VL_NEGOCIADO, OSIT.VL_TABELA) as vlCobranca, ");
		sql.append("	NFSE.TP_NOTA_FISCAL_SERVICO as tpNotaFiscalServico, ");
		sql.append("	NFSE.SG_FILIAL as sgFilialNotaFiscalServico, ");
		sql.append("	NFSE.NR_NOTA_FISCAL_SERVICO as nrNotaFiscalServico, ");
		sql.append("	OSIT.ID_ORDEM_SERVICO_ITEM as idItem ");
		sql.append("FROM ");
		sql.append("" + getSubqueryOrdemServicoJoinDoctoServico() + " SUBQ, ");
		sql.append("" + getSubqueryOrdemServicoItemJoinNotaFiscalServico() + " NFSE, ");
		sql.append("	ORDEM_SERVICO ORSE, ");
		sql.append("	ORDEM_SERVICO_ITEM OSIT, ");
		sql.append("	DOCTO_SERVICO DOCT, ");
		sql.append("	FILIAL FIDO, ");
		sql.append("	FILIAL FIEX, ");
		sql.append("	SERVICO SERV, ");
		sql.append("	PESSOA PESS, ");
		sql.append("	PARCELA_PRECO PARC ");
		sql.append("WHERE ");
		sql.append("	ORSE.ID_ORDEM_SERVICO = SUBQ.ID_ORDEM_SERVICO ");
		sql.append("AND	DOCT.ID_DOCTO_SERVICO(+) = SUBQ.ID_DOCTO_SERVICO ");
		sql.append("AND	DOCT.ID_FILIAL_ORIGEM = FIDO.ID_FILIAL(+) ");		
		sql.append("AND	ORSE.ID_ORDEM_SERVICO = OSIT.ID_ORDEM_SERVICO ");		
		sql.append("AND	ORSE.ID_FILIAL_EXECUCAO = FIEX.ID_FILIAL ");
		sql.append("AND	DOCT.ID_SERVICO = SERV.ID_SERVICO(+) ");
		sql.append("AND ORSE.ID_CLIENTE_TOMADOR = PESS.ID_PESSOA ");		
		sql.append("AND OSIT.ID_PARCELA_PRECO = PARC.ID_PARCELA_PRECO ");
		sql.append("AND OSIT.ID_ORDEM_SERVICO_ITEM = NFSE.ID_ORDEM_SERVICO_ITEM(+) ");
		sql.append("AND (NFSE.ID_DOCTO_SERVICO IS NULL OR SUBQ.ID_DOCTO_SERVICO = NFSE.ID_DOCTO_SERVICO) ");				
		
		if(criteria.get("idRegional") != null) {
			sql.append("AND " + 		
					getSubqueryRegionalPlanilhaServicosAdicionais("REGI.ID_REGIONAL", "ORSE.ID_FILIAL_EXECUCAO") + 
					" = :idRegional ");
					
		}
		if(criteria.get("idFilial") != null) {
			sql.append("AND	ORSE.ID_FILIAL_EXECUCAO = :idFilial ");
		}
		if(criteria.get("tpModal") != null) {
			sql.append("AND SERV.TP_MODAL = :tpModal ");
		}
		if(criteria.get("tpAbrangencia") != null) {
			sql.append("AND SERV.TP_ABRANGENCIA = :tpAbrangencia ");
		}
		if(criteria.get("idParcelaPreco") != null) {
			sql.append("AND OSIT.ID_PARCELA_PRECO = :idParcelaPreco ");
		}
		if(criteria.get("idCliente") != null) {
			sql.append("AND ORSE.ID_CLIENTE_TOMADOR = :idCliente ");
		}
		if(criteria.get("tpSituacao") != null) {
			sql.append("AND ORSE.TP_SITUACAO = :tpSituacao ");
		}
		if(criteria.get("idUsuario") != null) {
			sql.append("AND " + 
					getSubqueryPromotorPlanilhaServicosAdicionais("USUA.ID_USUARIO", "ORSE.ID_CLIENTE_TOMADOR", "SERV.TP_MODAL") + 
					" = :idUsuario ");
		}		
		if(criteria.get("dtInicial") != null) {
			sql.append("AND	OSIT.DT_EXECUCAO >= :dtInicial ");
		}
		if(criteria.get("dtFinal") != null) {
			sql.append("AND	OSIT.DT_EXECUCAO <= :dtFinal ");
		}
		
		return sql.toString();
	}
	
	private String getSqlGeracaoPlanilhaServicosAdicionaisAutomaticos(Map<String, Object> criteria) {
		StringBuilder sql = new StringBuilder();
		
		/* QUERY QUE RETORNA OS ITENS DE SERVI�O GERA��O AUTOM�TICA */
		sql.append("SELECT ");
		sql.append("	DOCT.TP_DOCUMENTO_SERVICO AS tpDoctoServico, ");
		sql.append("	FIDO.SG_FILIAL AS sgFilialDoctoServico, ");
		sql.append("	DOCT.NR_DOCTO_SERVICO AS nrDoctoServico, ");
		sql.append("" + getSubqueryRegionalPlanilhaServicosAdicionais("REGI.SG_REGIONAL", "SEGA.ID_FILIAL_EXECUCAO") + " AS sgRegionalExecucao, ");
		sql.append("	FIEX.SG_FILIAL AS sgFilialExecucao, ");
		sql.append("	SERV.TP_MODAL AS tpModal, "); 
		sql.append("" + getSubqueryPromotorPlanilhaServicosAdicionais("USUA.NM_USUARIO", "PESS.ID_PESSOA", "SERV.TP_MODAL") + " AS nmUsuarioPromotor, ");
		sql.append("	PESS.NR_IDENTIFICACAO AS nrIdentificacaoTomador, ");
		sql.append("	PESS.NM_PESSOA AS nmPessoaTomador, ");
		sql.append("	TRUNC(SEGA.DH_CALCULO) as dtExecucao, ");
		sql.append("	VI18N(PARC.DS_PARCELA_PRECO_I) AS dsParcelaPreco, ");
		sql.append("	DECODE(SEGA.BL_FATURADO, 'N', 'D', 'P') as tpSituacao, ");
		sql.append("	SEGA.VL_SERVICO_ADICIONAL as vlTabela, ");
		sql.append("	NULL as vlCusto, ");
		sql.append("	SEGA.VL_SERVICO_ADICIONAL as vlCobranca, ");
		sql.append("	NFSE.TP_NOTA_FISCAL_SERVICO as tpNotaFiscalServico, ");
		sql.append("	FINF.SG_FILIAL as sgFilialNotaFiscalServico, ");
		sql.append("	NFSE.NR_NOTA_FISCAL_SERVICO as nrNotaFiscalServico, ");
		sql.append("	NULL as idItem ");
		sql.append("FROM ");			
		sql.append("	SERVICO_GERACAO_AUTOMATICA SEGA, ");
		sql.append("	DOCTO_SERVICO DOCT, ");
		sql.append("	FILIAL FIDO, "); 
		sql.append("	FILIAL FIEX, ");
		sql.append("	SERVICO SERV, ");
		sql.append("	DEVEDOR_DOC_SERV DEVE, ");
		sql.append("	PESSOA PESS, ");
		sql.append("	PARCELA_PRECO PARC, ");
		sql.append("	NOTA_FISCAL_SERVICO_DOCUMENTO NFSD, ");
		sql.append("	PRE_FATURA_SERVICO_ITEM PFSI, ");
		sql.append("	NOTA_FISCAL_SERVICO NFSE, ");
		sql.append("	FILIAL FINF ");
		sql.append("WHERE ");
		sql.append("	SEGA.ID_DOCTO_SERVICO = DOCT.ID_DOCTO_SERVICO "); 
		sql.append("AND DOCT.ID_FILIAL_ORIGEM      = FIDO.ID_FILIAL ");
		sql.append("AND SEGA.ID_FILIAL_EXECUCAO    = FIEX.ID_FILIAL ");
		sql.append("AND DOCT.ID_SERVICO            = SERV.ID_SERVICO ");
		sql.append("AND DOCT.ID_DOCTO_SERVICO      = DEVE.ID_DOCTO_SERVICO ");
		sql.append("AND DEVE.ID_CLIENTE            = PESS.ID_PESSOA ");
		sql.append("AND SEGA.ID_PARCELA_PRECO      = PARC.ID_PARCELA_PRECO ");
		sql.append("AND SEGA.ID_SERVICO_GERACAO_AUTOMATICA = PFSI.ID_SERVICO_GERACAO_AUTOMATICA(+) ");
		sql.append("AND PFSI.ID_PRE_FATURA_SERVICO_ITEM = NFSD.ID_PRE_FATURA_SERVICO_ITEM(+) ");
		sql.append("AND NFSD.ID_NOTA_FISCAL_SERVICO       = NFSE.ID_NOTA_FISCAL_SERVICO(+) ");
		sql.append("AND NFSE.ID_FILIAL_ORIGEM             = FINF.ID_FILIAL(+) ");
		sql.append("AND SEGA.BL_SEM_COBRANCA = 'N' ");
		
		if(criteria.get("idRegional") != null) {
			sql.append("AND " + 		
					getSubqueryRegionalPlanilhaServicosAdicionais("REGI.ID_REGIONAL", "SEGA.ID_FILIAL_EXECUCAO") + 
					" = :idRegional ");
					
		}
		if(criteria.get("idFilial") != null) {
			sql.append("AND	SEGA.ID_FILIAL_EXECUCAO = :idFilial ");
		}
		if(criteria.get("tpModal") != null) {
			sql.append("AND SERV.TP_MODAL = :tpModal ");
		}
		if(criteria.get("tpAbrangencia") != null) {
			sql.append("AND SERV.TP_ABRANGENCIA = :tpAbrangencia ");
		}
		if(criteria.get("idParcelaPreco") != null) {
			sql.append("AND SEGA.ID_PARCELA_PRECO = :idParcelaPreco ");
		}
		if(criteria.get("idCliente") != null) {
			sql.append("AND PESS.ID_PESSOA = :idCliente ");
		}
		if(criteria.get("tpSituacao") != null) {
			if(ConstantesExpedicao.TP_SITUACAO_OS_DIGITADA.equals(criteria.get("tpSituacao")) || 
					ConstantesExpedicao.TP_SITUACAO_OS_APROVADA.equals(criteria.get("tpSituacao"))) {
				sql.append("AND SEGA.BL_FATURADO = 'N' ");
			}
			if(ConstantesExpedicao.TP_SITUACAO_OS_EM_PRE_FATURA.equals(criteria.get("tpSituacao"))) {
				sql.append("AND SEGA.BL_FATURADO = 'S' ");
			}
			if(ConstantesExpedicao.TP_SITUACAO_OS_REAPROVADA.equals(criteria.get("tpSituacao"))) {
				sql.append("AND 1=2 ");
			}						
		}
		if(criteria.get("idUsuario") != null) {
			sql.append("AND " + 
					getSubqueryPromotorPlanilhaServicosAdicionais("USUA.ID_USUARIO", "PESS.ID_PESSOA", "SERV.TP_MODAL") + 
					" = :idUsuario ");
		}		
		if(criteria.get("dtInicial") != null) {
			sql.append("AND	TRUNC(SEGA.DH_CALCULO) >= :dtInicial ");
		}
		if(criteria.get("dtFinal") != null) {
			sql.append("AND	TRUNC(SEGA.DH_CALCULO) <= :dtFinal ");
		}
		
		return sql.toString();
	}
	
	public List<Map<String, Object>> findGeracaoPlanilhaServicosAdicionaisGerados(Map<String, Object> criteria) {		
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlGeracaoPlanilhaServicosAdicionaisOS(criteria));
		sql.append(" UNION ALL ");
		sql.append(getSqlGeracaoPlanilhaServicosAdicionaisAutomaticos(criteria));
		sql.append("ORDER BY dtExecucao, idItem ");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				/*00*/sqlQuery.addScalar("tpDoctoServico", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_TIPO_DOCUMENTO_SERVICO"}));
				/*01*/sqlQuery.addScalar("sgFilialDoctoServico", Hibernate.STRING);				
				/*02*/sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);				
				/*03*/sqlQuery.addScalar("sgRegionalExecucao", Hibernate.STRING);				
				/*04*/sqlQuery.addScalar("sgFilialExecucao", Hibernate.STRING);	
				/*05*/sqlQuery.addScalar("tpModal", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_MODAL"}));
				/*06*/sqlQuery.addScalar("nmUsuarioPromotor", Hibernate.STRING);
				/*07*/sqlQuery.addScalar("nrIdentificacaoTomador", Hibernate.STRING);
				/*08*/sqlQuery.addScalar("nmPessoaTomador", Hibernate.STRING);
				/*09*/sqlQuery.addScalar("dtExecucao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				/*10*/sqlQuery.addScalar("dsParcelaPreco", Hibernate.STRING);
				/*11*/sqlQuery.addScalar("tpSituacao", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_SITUACAO_ORDEM_SERVICO"}));
				/*12*/sqlQuery.addScalar("vlTabela", Hibernate.BIG_DECIMAL);
				/*13*/sqlQuery.addScalar("vlCusto", Hibernate.BIG_DECIMAL);
				/*14*/sqlQuery.addScalar("vlCobranca", Hibernate.BIG_DECIMAL);
				/*15*/sqlQuery.addScalar("tpNotaFiscalServico", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_TIPO_DOCUMENTO_SERVICO"}));				
				/*16*/sqlQuery.addScalar("sgFilialNotaFiscalServico", Hibernate.STRING);
				/*17*/sqlQuery.addScalar("nrNotaFiscalServico", Hibernate.STRING);	
			}			
		};
		
		List<Object[]> tuples = getAdsmHibernateTemplate().findBySql(sql.toString(), criteria, csq);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for(Object[] tuple : tuples) {
			Map<String, Object> item = new HashMap<String, Object>();
			
			if(tuple[0] != null) {
				DomainValue tpDoctoServico = (DomainValue)tuple[0];
				String nrDoctoServico = FormatUtils.formataNrDocumento((String)tuple[2], tpDoctoServico.getValue());
				item.put("nrDoctoServico", tpDoctoServico.getDescriptionAsString()+" "+tuple[1]+" "+nrDoctoServico);
				item.put("tpModal", ((DomainValue)tuple[5]).getDescriptionAsString());
			}
			item.put("sgRegionalExecucao", tuple[3]);
			item.put("sgFilialExecucao", tuple[4]);			
			item.put("nmUsuarioPromotor", tuple[6]);
			item.put("nrIdentificacaoTomador", tuple[7]);
			item.put("nmPessoaTomador", tuple[8]);
			item.put("dtExecucao", ((YearMonthDay) tuple[9]).toString("dd/MM/yyyy"));
			item.put("dsParcelaPreco", tuple[10]);
			item.put("tpSituacao", ((DomainValue)tuple[11]).getDescriptionAsString());
			item.put("vlTabela", tuple[12]);
			item.put("vlCusto", tuple[13]);
			item.put("vlCobranca", tuple[14]);
			if(tuple[15] != null) {
				DomainValue tpNotaFiscalServico = (DomainValue)tuple[15];			
				String nrNotaFiscalServico = FormatUtils.formataNrDocumento((String)tuple[17], tpNotaFiscalServico.getValue());
				item.put("nrNotaFiscalServico", tpNotaFiscalServico.getDescriptionAsString()+" "+tuple[16]+" "+nrNotaFiscalServico);
			}
			
			result.add(item);
		}
		
		return result;				
	}
	
	public List<Map<String, Object>> findDadosRelatorioReceitasPotenciais(Map<String, Object> criteria) {		
		ConfigureSqlQuery csq = configureQueryForRelatorioServicosAdicionaiss();
		String sql = getSqlGeracaoPlanilhaServicosAdicionaiss(criteria);
		List<Object[]> tuples = getAdsmHibernateTemplate().findBySql(sql, criteria, csq);
		
		return createResultFromTupless(tuples);
	}

	private ConfigureSqlQuery configureQueryForRelatorioServicosAdicionaiss() {
		return new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				/*00*/sqlQuery.addScalar("tpDoctoServico", Hibernate.custom(DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_TIPO_DOCUMENTO_SERVICO"}));
				/*01*/sqlQuery.addScalar("sgFilialDoctoServico", Hibernate.STRING);				
				/*02*/sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);			
				/*03*/sqlQuery.addScalar("sgRegionalExecucao", Hibernate.STRING);			
				/*04*/sqlQuery.addScalar("nrOrdemServico", Hibernate.STRING);	
				/*05*/sqlQuery.addScalar("tpModal", Hibernate.custom(DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_MODAL"}));
				/*06*/sqlQuery.addScalar("nmUsuarioPromotor", Hibernate.STRING);
				/*07*/sqlQuery.addScalar("nrIdentificacaoTomador", Hibernate.STRING);
				/*08*/sqlQuery.addScalar("nmPessoaTomador", Hibernate.STRING);
				/*09*/sqlQuery.addScalar("dtExecucao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				/*10*/sqlQuery.addScalar("filialOrdemServico", Hibernate.STRING);
				/*11*/sqlQuery.addScalar("nrIdentificacaoPessoaDestino", Hibernate.STRING);
				/*12*/sqlQuery.addScalar("vlTabela", Hibernate.BIG_DECIMAL);
				/*13*/sqlQuery.addScalar("vlCusto", Hibernate.BIG_DECIMAL);
				/*14*/sqlQuery.addScalar("nomePessoaDestino", Hibernate.STRING);
				/*15*/sqlQuery.addScalar("dtSolicitacaoOrdemServico", Hibernate.custom(JodaTimeYearMonthDayUserType.class));			
				/*16*/sqlQuery.addScalar("vlCobrar", Hibernate.BIG_DECIMAL);
				/*17*/sqlQuery.addScalar("nmParcelaPreco", Hibernate.STRING);
				/*18*/sqlQuery.addScalar("filialTomador", Hibernate.STRING);
			}			
		};
	}	

	private String getSqlGeracaoPlanilhaServicosAdicionaiss(Map<String, Object> criteria) {
		StringBuilder sql = new StringBuilder();
		
		/* QUERY QUE RETORNA OS ITENS DE ORDEM DE SERVIÇO */
		sql.append("SELECT * FROM (");
		sql.append("SELECT ");
		sql.append("	DOCT.TP_DOCUMENTO_SERVICO AS tpDoctoServico, ");
		sql.append("    NVL(OSIT.VL_NEGOCIADO, OSIT.VL_TABELA) AS vlCobrar, ");
		sql.append("    PARC.NM_PARCELA_PRECO_I as nmParcelaPreco, ");
		sql.append("    FI_TOMADOR.SG_FILIAL as filialTomador, ");
		sql.append("	FIDO.SG_FILIAL AS sgFilialDoctoServico, ");
		sql.append("	DOCT.NR_DOCTO_SERVICO AS nrDoctoServico, ");
		sql.append("	PESSOA_DESTINO.NR_IDENTIFICACAO AS nrIdentificacaoPessoaDestino, ");
		sql.append("	PESSOA_DESTINO.NM_PESSOA AS nomePessoaDestino, ");
		sql.append("" + getSubqueryRegionalPlanilhaServicosAdicionais("REGI.SG_REGIONAL", "ORSE.ID_FILIAL_EXECUCAO") + " AS sgRegionalExecucao, ");
		sql.append("	FIEX.SG_FILIAL AS sgFilialExecucao, ");
		sql.append("	SERV.TP_MODAL AS tpModal, "); 
		sql.append("" + getSubqueryPromotorPlanilhaServicosAdicionais("USUA.NM_USUARIO", "ORSE.ID_CLIENTE_TOMADOR", "SERV.TP_MODAL") + " AS nmUsuarioPromotor, ");
		sql.append("	PESS.NR_IDENTIFICACAO AS nrIdentificacaoTomador, ");
		sql.append("	PESS.NM_PESSOA AS nmPessoaTomador, ");
		sql.append("	OSIT.DT_EXECUCAO as dtExecucao, ");
		sql.append("	VI18N(PARC.DS_PARCELA_PRECO_I) AS dsParcelaPreco, ");
		sql.append("	ORSE.TP_SITUACAO as tpSituacao, ");
		sql.append("	OSIT.VL_TABELA as vlTabela, ");
		sql.append("	OSIT.VL_CUSTO as vlCusto, ");
		sql.append("	NVL(OSIT.VL_NEGOCIADO, OSIT.VL_TABELA) as vlCobranca, ");
		sql.append("	NFSE.TP_NOTA_FISCAL_SERVICO as tpNotaFiscalServico, ");
		sql.append("	NFSE.SG_FILIAL as sgFilialNotaFiscalServico, ");
		sql.append("	NFSE.NR_NOTA_FISCAL_SERVICO as nrNotaFiscalServico, ");
		sql.append("	OSIT.ID_ORDEM_SERVICO_ITEM as idItem, ");
		sql.append("	ORSE.ID_DIVISAO_CLIENTE as idDivisaoCliente, ");
		sql.append("    ORSE.NR_ORDEM_SERVICO AS nrOrdemServico, ");
		sql.append("    FILIAL_OS.SG_FILIAL AS filialOrdemServico, ");
		sql.append("    ORSE.DT_SOLICITACAO AS dtSolicitacaoOrdemServico, ");
		sql.append("    PARC.ID_PARCELA_PRECO AS parcIdParcelaPreco, ");
		sql.append("    OSIT.ID_PARCELA_PRECO AS ositIdParcelaPreco ");
		sql.append("FROM ");
		sql.append("" + getSubqueryOrdemServicoJoinDoctoServico() + " SUBQ, ");
		sql.append("" + getSubqueryOrdemServicoItemJoinNotaFiscalServico() + " NFSE, ");
		sql.append("	ORDEM_SERVICO ORSE, ");
		sql.append("	ORDEM_SERVICO_ITEM OSIT, ");
		sql.append("	DOCTO_SERVICO DOCT, ");
		sql.append("	FILIAL FIDO, ");
		sql.append("	FILIAL FIEX, ");
		sql.append("    FILIAL FILIAL_OS, ");
		sql.append("    PESSOA PESSOA_DESTINO, ");
		sql.append("	SERVICO SERV, ");
		sql.append("	PESSOA PESS, ");
		sql.append("    CLIENTE CLIENTE, ");
		sql.append("    FILIAL FI_TOMADOR, ");
		sql.append("	PARCELA_PRECO PARC ");
		sql.append("WHERE ");
		sql.append("	ORSE.ID_ORDEM_SERVICO = SUBQ.ID_ORDEM_SERVICO ");
		sql.append("AND	DOCT.ID_DOCTO_SERVICO(+) = SUBQ.ID_DOCTO_SERVICO ");
		sql.append("AND	DOCT.ID_FILIAL_ORIGEM = FIDO.ID_FILIAL(+) ");		
		sql.append("AND	ORSE.ID_ORDEM_SERVICO = OSIT.ID_ORDEM_SERVICO ");		
		sql.append("AND	ORSE.ID_FILIAL_EXECUCAO = FIEX.ID_FILIAL ");
		sql.append("AND	DOCT.ID_SERVICO = SERV.ID_SERVICO(+) ");
		sql.append("AND ORSE.ID_CLIENTE_TOMADOR = PESS.ID_PESSOA ");		
		sql.append("AND OSIT.ID_PARCELA_PRECO = PARC.ID_PARCELA_PRECO ");
		sql.append("AND OSIT.ID_ORDEM_SERVICO_ITEM = NFSE.ID_ORDEM_SERVICO_ITEM(+) ");
		sql.append("AND (NFSE.ID_DOCTO_SERVICO IS NULL OR SUBQ.ID_DOCTO_SERVICO = NFSE.ID_DOCTO_SERVICO) ");
		sql.append("AND FILIAL_OS.ID_FILIAL = ORSE.ID_FILIAL_EXECUCAO ");
		sql.append("AND PESSOA_DESTINO.ID_PESSOA = DOCT.ID_CLIENTE_DESTINATARIO ");
		sql.append("AND PESS.ID_PESSOA = CLIENTE.ID_CLIENTE ");
		sql.append("AND FI_TOMADOR.ID_FILIAL = CLIENTE.ID_FILIAL_ATENDE_COMERCIAL ");
		sql.append("AND ORSE.TP_SITUACAO = 'A' ");
	    
		if(criteria.get("idRegional") != null) {
			sql.append("AND " + 		
					getSubqueryRegionalPlanilhaServicosAdicionais("REGI.ID_REGIONAL", "ORSE.ID_FILIAL_EXECUCAO") + 
					" = :idRegional ");
		}
		if(criteria.get("idFilial") != null) {
			sql.append("AND	ORSE.ID_FILIAL_EXECUCAO = :idFilial ");
		} 
		if(criteria.get("tpModal") != null) {
			sql.append("AND SERV.TP_MODAL = :tpModal ");
		}
		if(criteria.get("tpAbrangencia") != null) {
			sql.append("AND SERV.TP_ABRANGENCIA = :tpAbrangencia ");
		}
		if(criteria.get("idParcelaPreco") != null) {
			sql.append("AND OSIT.ID_PARCELA_PRECO = :idParcelaPreco ");
		}
		if(criteria.get("idCliente") != null) {
			sql.append("AND ORSE.ID_CLIENTE_TOMADOR = :idCliente ");
		}
		if(criteria.get("tpSituacao") != null) {
			sql.append("AND ORSE.TP_SITUACAO = :tpSituacao ");
		}
		if(criteria.get("idUsuario") != null) {
			sql.append("AND " + 
					getSubqueryPromotorPlanilhaServicosAdicionais("USUA.ID_USUARIO", "ORSE.ID_CLIENTE_TOMADOR", "SERV.TP_MODAL") + 
					" = :idUsuario ");
		}		
		if(criteria.get("dtInicial") != null) {
			sql.append("AND	OSIT.DT_EXECUCAO >= :dtInicial ");
		}
		if(criteria.get("dtFinal") != null) {
			sql.append("AND	OSIT.DT_EXECUCAO <= :dtFinal ");
		}
		
		
		sql.append(") t1 ");
		sql.append(" LEFT JOIN tabela_divisao_cliente tdc on tdc.id_divisao_cliente = t1.idDivisaoCliente ");
		sql.append(" LEFT JOIN servico_adicional_cliente sac ON ( sac.id_tabela_divisao_cliente = tdc.id_tabela_divisao_cliente AND t1.ositIdParcelaPreco = sac.ID_PARCELA_PRECO AND t1.parcIdParcelaPreco = sac.ID_PARCELA_PRECO ) ");
		sql.append(" WHERE sac.id_servico_adicional_cliente is NULL ");

		return sql.toString();
	}

	private List<Map<String,Object>> createResultFromTupless(List<Object[]> tuples) {
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		for(Object[] tuple : tuples) {
			Map<String, Object> item = new HashMap<String, Object>();
			
			if(tuple[0] != null) {
				DomainValue tpDoctoServico = (DomainValue)tuple[0];
				String nrDoctoServico = FormatUtils.formataNrDocumento((String)tuple[2], tpDoctoServico.getValue());
				item.put("nrDoctoServico", tpDoctoServico.getDescriptionAsString()+" "+tuple[1]+" "+nrDoctoServico);
				item.put("tpModal", ((DomainValue)tuple[5]).getDescriptionAsString());
			}
			item.put("nrOrdemServico", tuple[4]);
			item.put("filialOrdemServico", tuple[10]);
			item.put("sgRegionalExecucao", tuple[3]);
			item.put("nmUsuarioPromotor", tuple[6]);
			item.put("nrIdentificacaoTomador", tuple[7]);
			item.put("nmPessoaTomador", tuple[8]);
			item.put("nrIdentificacaoPessoaDestino", tuple[11]);
			item.put("nomePessoaDestino", tuple[14]);
			item.put("vlCusto", formatCurrency((BigDecimal)tuple[13]));
			item.put("dtExecucao", ((YearMonthDay) tuple[9]).toString("dd/MM/yyyy"));
			item.put("dtSolicitacaoOrdemServico", tuple[15]);
			item.put("vlTabela", formatCurrency((BigDecimal)tuple[12]));
			item.put("vlCobrar", formatCurrency((BigDecimal)tuple[16]));
			item.put("nmParcelaPreco", formatMessage(tuple[17].toString()));
			item.put("filialTomador", tuple[18]);
			
			result.add(item);
		}
		
		return result;
	}

	private String formatMessage(String string) {
		return StringUtils.substring(string, 6, string.length()-1);
	}

	private String formatCurrency(BigDecimal number) {
		if (number == null) {
			return "";
		}
        DecimalFormat nf = new DecimalFormat("#.00");
        return nf.format(number).replace(".", ",");
	}

}
