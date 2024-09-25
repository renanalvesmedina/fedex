package com.mercurio.lms.expedicao.model.dao;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.OrdemServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

public class OrdemServicoDAO extends BaseCrudDao<OrdemServico, Long> {

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return OrdemServico.class;
	}
	
	@Override
	public OrdemServico findById(Long id) {
		StringBuilder query = new StringBuilder();
	
		query.append("from " + getPersistentClass().getName() + " as os ");
		query.append(mountJoinOrdemServico());
		
		query.append("where ");
		query.append(" os.idOrdemServico = :id ");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
		
		return (OrdemServico) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}

	private String mountJoinOrdemServico() {
		StringBuilder query = new StringBuilder();		
		
		query.append(" inner join fetch os.filialExecucao fe ");
		query.append("inner join fetch fe.pessoa pfe ");
		query.append("inner join fetch os.filialRegistro fr ");
		query.append("inner join fetch fr.pessoa pfr ");
		query.append("inner join fetch os.clienteTomador ct ");
		query.append("inner join fetch ct.pessoa pessoaTomador ");
		query.append("inner join fetch os.municipioExecucao municipioExecucao ");
		query.append("left join fetch os.ordemServicoDocumentos osd ");
		query.append("left join fetch osd.doctoServico doc ");
		query.append("left join fetch doc.filialByIdFilialOrigem fo ");
		query.append("left join fetch os.manifestoColeta mac ");
		query.append("left join fetch mac.filial macf ");
		query.append("left join fetch os.manifesto ma ");
		query.append("left join fetch ma.filialByIdFilialOrigem maf ");
		query.append("left join fetch ma.manifestoEntrega mae ");
		query.append("left join fetch ma.manifestoViagemNacional mav ");
		
		return query.toString();
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdemServico> findByDoctoServico(Long idDoctoServico, List<String> tpSituacoes){		
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as os ");
		query.append("left join fetch os.ordemServicoDocumentos osd ");
		query.append("left join fetch osd.doctoServico doc ");
		query.append("where ");
		query.append("os.tpSituacao IN (:listSituacoes) ");
		query.append("AND doc.idDoctoServico = :idDoctoServico ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("listSituacoes", tpSituacoes);
		parameters.put("idDoctoServico", idDoctoServico);
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parameters);
	}
	
	private String getFindPaginatedQueryFilters(TypedFlatMap criteria) {
		StringBuilder query = new StringBuilder();		

		query.append(" where ");
		
		query.append("os.ID_ORDEM_SERVICO = osi.ID_ORDEM_SERVICO(+) ");
		query.append("and pfi.ID_PRE_FATURA_SERVICO = pf.ID_PRE_FATURA_SERVICO(+) ");
		query.append("and osi.ID_ORDEM_SERVICO_ITEM = pfi.ID_ORDEM_SERVICO_ITEM(+) ");
	    query.append("and os.ID_FILIAL_REGISTRO = fi.ID_FILIAL ");
	    query.append("and pf.ID_FILIAL_COBRANCA = fip.ID_FILIAL(+) ");
	    query.append("and os.ID_CLIENTE_TOMADOR = cli.ID_CLIENTE ");
	    query.append("and cli.ID_CLIENTE = pessoaTomador.ID_PESSOA ");
	    query.append("and osi.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO(+) ");
	     
		/* - Se informada uma filial de registro e um número de controle acessar diretamente a base de 
		 * dados sem considerar os demais campos;  */
		if(criteria.get("idFilialRegistro") != null && criteria.get("nrOrdemServico") != null) {
			query.append("and os.NR_ORDEM_SERVICO = :nrOrdemServico ");
			query.append("and os.ID_FILIAL_REGISTRO = :idFilialRegistro ");
		/* - Se for informado um documento de serviço ou um manifesto acessar diretamente a base de 
		 * dados sem considerar os demais campos; */
		} else if(criteria.get("tpDocumento") != null && criteria.get("idDoctoServico") != null) {						
			query.append("and os.TP_DOCUMENTO = :tpDocumento ");
			query.append("and exists (select * from ORDEM_SERVICO_DOCUMENTO "
					+ " osd where osd.ID_ORDEM_SERVICO = os.ID_ORDEM_SERVICO and osd.ID_DOCTO_SERVICO = :idDoctoServico ) ");			
		} else if(criteria.get("tpDocumento") != null && criteria.get("idManifesto") != null) {
			query.append("and os.TP_DOCUMENTO = :tpDocumento ");	
			if(ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_COLETA.equals(criteria.get("tpDocumento"))) {
				query.append("and os.ID_MANIFESTO_COLETA = :idManifesto ");
			} else {
				query.append("and os.ID_MANIFESTO = :idManifesto ");
			}			
		} else {
			if (criteria.get("idFilialRegistro") != null) {
				query.append("and os.ID_FILIAL_REGISTRO = :idFilialRegistro ");
			}			
			if (criteria.get("nrOrdemServico") != null) {
				query.append("and os.NR_ORDEM_SERVICO = :nrOrdemServico ");
			}			
			if (criteria.get("idFilialExecucao") != null) {
				query.append("and os.ID_FILIAL_EXECUCAO = :idFilialExecucao ");
			}
			if (criteria.get("idUsuario") != null) {
				query.append("and os.ID_USUARIO_REGISTRANTE = :idUsuario ");
			}
			if (criteria.get("tpSituacao") != null) {
				query.append("and os.TP_SITUACAO = :tpSituacao ");
			}
			if (criteria.get("dtInicial") != null) {
				query.append("and os.DT_SOLICITACAO >= :dtInicial ");
			}
			if (criteria.get("dtFinal") != null) {
				query.append("and os.DT_SOLICITACAO <= :dtFinal ");
			}
			if (criteria.get("idCliente") != null) {
				query.append("and os.ID_CLIENTE_TOMADOR = :idCliente ");
			}
			if (criteria.get("tpDocumento") != null) {
				query.append("and os.TP_DOCUMENTO = :tpDocumento ");
			}			
			if (criteria.get("idParcelaPreco") != null) {
				query.append("and pp.ID_PARCELA_PRECO = :idParcelaPreco ");
			}			
		}

		return query.toString();
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findPaginated(PaginatedQuery paginatedQuery) {		
	
		TypedFlatMap criteria = new TypedFlatMap(paginatedQuery.getCriteria());
		StringBuilder query = new StringBuilder();		
		
		//LMS-7113
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				/*00*/sqlQuery.addScalar("idOrdemServico", Hibernate.LONG);
				/*01*/sqlQuery.addScalar("sgFilialRegistro", Hibernate.STRING);
				/*02*/sqlQuery.addScalar("nrOrdemServico", Hibernate.STRING);
				/*03*/sqlQuery.addScalar("dtSolicitacao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				/*04*/sqlQuery.addScalar("nmTomador", Hibernate.STRING);	
				/*05*/sqlQuery.addScalar("nrIdentificacaoTomador", Hibernate.STRING);
				/*06*/sqlQuery.addScalar("tpIdentificacaoTomador", Hibernate.STRING);
				/*07*/sqlQuery.addScalar("tpSituacaoOrdemServico",Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_SITUACAO_ORDEM_SERVICO"}));
				/*08*/sqlQuery.addScalar("dsMotivo", Hibernate.STRING);
				/*09*/sqlQuery.addScalar("preFatura", Hibernate.STRING);
				/*10*/sqlQuery.addScalar("sgFilialCobranca", Hibernate.STRING);
				/*11*/sqlQuery.addScalar("tpSituacaoPreFatura", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_SITUACAO_PRE_FATURA"}));
		
				}
		};
			
		query.append(mountSqlSelect());
		query.append(mountSqlFrom());
		query.append(getFindPaginatedQueryFilters(criteria));
		query.append(" order by fi.SG_FILIAL, os.NR_ORDEM_SERVICO ");
				
		return getAdsmHibernateTemplate().findPaginatedBySqlToMappedResult(query.toString(), criteria, csq);
	}

	/*** 
	 * LMS-7113
	 * @return
	 */	
	private String mountSqlSelect(){
		StringBuilder query = new StringBuilder();		
		
		query.append(" select distinct "  );
		query.append(" os.ID_ORDEM_SERVICO as idOrdemServico, ");
		query.append(" fi.SG_FILIAL as sgFilialRegistro, ");
		query.append(" os.NR_ORDEM_SERVICO as nrOrdemServico, ");
		query.append(" os.DT_SOLICITACAO as dtSolicitacao, ");
		query.append(" pessoaTomador.NM_PESSOA as nmTomador, ");
		query.append(" pessoaTomador.NR_IDENTIFICACAO as nrIdentificacaoTomador, ");
		query.append(" pessoaTomador.TP_IDENTIFICACAO as tpIdentificacaoTomador, ");
		query.append(" os.TP_SITUACAO as tpSituacaoOrdemServico, ");
		query.append(" os.DS_MOTIVO_REJEICAO as dsMotivo, ");
		query.append(" pf.NR_PRE_FATURA as preFatura, ");
		query.append(" fip.sg_FILIAL as sgFilialCobranca, ");
		query.append(" pf.TP_SITUACAO as tpSituacaoPreFatura ");
		
		return query.toString();
	}
		
	/*** 
	 * LMS-7113
	 * @return
	 */
	private String mountSqlFrom() {
		StringBuilder query = new StringBuilder();		
	
		query.append("  from   ");
		query.append("ORDEM_SERVICO os, ");
	    query.append("ORDEM_SERVICO_ITEM osi, ");
	    query.append("FILIAL fi, ");
	    query.append("FILIAL fip, ");
	    query.append("CLIENTE cli, ");
	    query.append("PESSOA pessoaTomador, ");
	    query.append("PRE_FATURA_SERVICO_ITEM pfi, ");
	    query.append("PRE_FATURA_SERVICO pf, ");
	    query.append("PARCELA_PRECO pp ");
		
		return query.toString();
	}
		
	public Integer getRowCount(TypedFlatMap criteria) {
		StringBuilder query = new StringBuilder();	
		
		query.append(" select distinct os.id_ordem_servico ");	
		query.append(mountSqlFrom());	
		query.append(getFindPaginatedQueryFilters(criteria));
		
		return getAdsmHibernateTemplate().getRowCountBySql(query.toString(), criteria);
	}
	
	private String mountHqlPaginated(TypedFlatMap criteria) {		
		StringBuilder sb = new StringBuilder();
		sb.append("from " + getPersistentClass().getName() + " as os ");
		sb.append("left join os.divisaoCliente dc ");
		sb.append("left join os.ordemServicoItens osi ");
		sb.append("inner join os.clienteTomador ct ");
		sb.append("inner join ct.pessoa pt ");		
		
		sb.append("where os.tpSituacao = :tpSituacao ");
		if (criteria.getList("idsFiliaisUsuario") != null) {
			sb.append("and os.filialExecucao.idFilial in (:idsFiliaisUsuario) ");
		}
		if (criteria.getLong("idCliente") != null) {
			sb.append("and ct.idCliente = :idCliente ");
		}
		
		if (criteria.getYearMonthDay("dtPeriodoInicial") != null && criteria.getYearMonthDay("dtPeriodoFinal") != null) {
			sb.append("and os.dtSolicitacao >= :dtPeriodoInicial ");
			sb.append("and os.dtSolicitacao <= :dtPeriodoFinal ");
		}
		
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findPaginatedAprovacao(PaginatedQuery paginatedQuery) {		
		paginatedQuery.addCriteria("tpSituacao", ConstantesExpedicao.TP_SITUACAO_DIGITADA);
		TypedFlatMap criteria = new TypedFlatMap(paginatedQuery.getCriteria());
		
		StringBuilder sb = new StringBuilder();
		sb.append("select new map(");
		sb.append("		ct.idCliente as idCliente,  ");		
		sb.append("		pt.tpIdentificacao as tpIdentificacao, ");
		sb.append("		pt.nrIdentificacao as nrIdentificacao, ");
		sb.append("		pt.nmPessoa as nmPessoa, ");
		sb.append("		count(distinct os.idOrdemServico) as qtTotal, ");
		sb.append("		sum(NVL(osi.vlNegociado, osi.vlTabela)) as vlTotal, ");
		sb.append("		min(os.dtSolicitacao) as dtExecucaoMin, ");
		sb.append("		max(os.dtSolicitacao) as dtExecucaoMax) ");		
				
		sb.append(mountHqlPaginated(criteria));
				
		sb.append("group by ");
		sb.append("		ct.idCliente, ");
		sb.append("		pt.tpIdentificacao, ");
		sb.append("		pt.nrIdentificacao, ");
		sb.append("		pt.nmPessoa ");
		
		sb.append("order by ");
		sb.append("		pt.nmPessoa");
		
		return getAdsmHibernateTemplate().findPaginated(				
				sb.toString(),
				paginatedQuery.getCurrentPage(),
				paginatedQuery.getPageSize(),
				criteria);
	}
	
	public Integer getRowCountAprovacao(TypedFlatMap criteria) {
		criteria.put("tpSituacao", ConstantesExpedicao.TP_SITUACAO_DIGITADA);
		StringBuilder sb = new StringBuilder();
		sb.append("select count(distinct ct.idCliente) ");
		sb.append(mountHqlPaginated(criteria));
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sb.toString(), criteria); 
    	return result.intValue();
	}
		
	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findPaginatedAprovacaoOrdens(PaginatedQuery paginatedQuery) {		
		paginatedQuery.addCriteria("tpSituacao", ConstantesExpedicao.TP_SITUACAO_DIGITADA);
		TypedFlatMap criteria = new TypedFlatMap(paginatedQuery.getCriteria());		
		
		StringBuilder sb = new StringBuilder();
		sb.append("select new map(");
		sb.append("		os.idOrdemServico as idOrdemServico, ");
		sb.append("		os.filialRegistro.sgFilial as filialRegistro, ");
		sb.append("		os.nrOrdemServico as nrOrdemServico, ");
		sb.append("		os.dtSolicitacao as dtSolicitacao, ");
		sb.append("		os.usuarioRegistrante.usuarioADSM.nmUsuario as nmUsuario, ");
		sb.append("		sum(osi.vlNegociado) as vlNegociado, ");
		sb.append("		sum(osi.vlCusto) as vlCusto, ");
		sb.append("		sum(osi.vlTabela) as vlTabela) ");
		
		sb.append(mountHqlPaginated(criteria));
		
		sb.append("group by ");
		sb.append("		os.idOrdemServico, ");
		sb.append("		os.filialRegistro.sgFilial, ");
		sb.append("		os.nrOrdemServico, ");
		sb.append("		os.dtSolicitacao, ");
		sb.append("		os.usuarioRegistrante.usuarioADSM.nmUsuario ");

		sb.append("order by ");
		sb.append("		os.filialRegistro.sgFilial, ");
		sb.append("		os.nrOrdemServico ");
		
		return getAdsmHibernateTemplate().findPaginated(				
				sb.toString(),
				paginatedQuery.getCurrentPage(),
				paginatedQuery.getPageSize(),
				criteria);
	}
	
	public Integer getRowCountAprovacaoOrdens(TypedFlatMap criteria) {
		criteria.put("tpSituacao", ConstantesExpedicao.TP_SITUACAO_DIGITADA);
		StringBuilder sb = new StringBuilder();		
		sb.append("select count(distinct os.idOrdemServico) ");
		sb.append(mountHqlPaginated(criteria));
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sb.toString(),criteria);
    	return result.intValue();
	}
	
	public void executeUpdateOrdensServicoEmPreFatura(List<Long> idsItens) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE ORDEM_SERVICO ORSE SET TP_SITUACAO = '" + ConstantesExpedicao.TP_SITUACAO_OS_EM_PRE_FATURA + "' ");
		sql.append("WHERE ORSE.TP_SITUACAO='" + ConstantesExpedicao.TP_SITUACAO_OS_APROVADA + "' ");		
		/* APENAS SE NÃO HOUVEREM ITENS NÃO FATURADOS */
		sql.append("AND NOT EXISTS (");
		sql.append("	SELECT DISTINCT SBQR.ID_ORDEM_SERVICO "); 
		sql.append("	FROM ORDEM_SERVICO_ITEM SBQR ");
		sql.append("	WHERE BL_FATURADO = 'N' ");
		sql.append("	AND SBQR.ID_ORDEM_SERVICO = ORSE.ID_ORDEM_SERVICO ");
		sql.append(") ");		
		/* APENAS ORDENS DE SERVICO REFERENTES AOS IDS DE ITENS PASSADOS POR PARAMETRO */
		sql.append("AND ORSE.ID_ORDEM_SERVICO IN (");
		sql.append("	SELECT ID_ORDEM_SERVICO ");
		sql.append("	FROM ORDEM_SERVICO_ITEM ");
		sql.append("	WHERE ID_ORDEM_SERVICO_ITEM IN (");
		for(int i=0; i<idsItens.size(); i++) {			
			if(i==0) {
				sql.append(idsItens.get(i));
			} else {
				sql.append(","+idsItens.get(i));
			}
		}			
		sql.append("))");		
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), null);			
	}
	
	/**
	 * LMS-7113
	 * @param id
	 * @return
	 */
	public TypedFlatMap findPreFaturaByIdOrdemServico(Long id) {
		StringBuilder query = new StringBuilder();
				
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				/*00*/sqlQuery.addScalar("sgFilialCobranca", Hibernate.STRING);
				/*01*/sqlQuery.addScalar("nrPreFatura", Hibernate.STRING);
				/*02*/sqlQuery.addScalar("tpSituacaoPreFatura", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_SITUACAO_PRE_FATURA"}));		
				}
		};
			
		query.append(" SELECT  pf.NR_PRE_FATURA as nrPreFatura, ");
		query.append(" pf.TP_SITUACAO as tpSituacaoPreFatura, ");
		query.append(" fi.SG_FILIAL as sgFilialCobranca ");		
		query.append(" FROM ORDEM_SERVICO os , ");
		query.append(" ORDEM_SERVICO_ITEM osi,  ");
		query.append(" PRE_FATURA_SERVICO_ITEM pfi , ");
		query.append(" PRE_FATURA_SERVICO pf,  ");
		query.append(" FILIAL fi ");
		query.append(" where ");
		query.append(" os.id_ordem_servico = osi.id_ordem_servico(+) ");
		query.append(" and osi.ID_ORDEM_SERVICO_ITEM = pfi.id_ordem_servico_item(+) ");
		query.append(" and pfi.id_pre_fatura_servico = pf.id_pre_fatura_servico(+) ");
		query.append(" and pf.ID_FILIAL_COBRANCA = fi.ID_FILIAL ");
		query.append(" and os.ID_ORDEM_SERVICO = :id "); 
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
		
		List<Object[]> tuples = getAdsmHibernateTemplate().findBySql(query.toString(), criteria, csq);
		TypedFlatMap map = new TypedFlatMap();
		
		for(Object[] tuple : tuples) {
			
			map.put("sgFilialCobranca", tuple[0] );
			map.put("nrPreFatura", tuple[1]);
			map.put("tpSituacaoPreFatura", tuple[2]);
			
		}
		
		return  map;
	}
}
