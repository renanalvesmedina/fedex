package com.mercurio.lms.prestcontasciaaerea.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.prestcontasciaaerea.model.FaturaCiaAerea;
import com.mercurio.lms.prestcontasciaaerea.model.FaturaCiaAereaAnexo;
import com.mercurio.lms.prestcontasciaaerea.model.ItemFaturaCiaAerea;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FaturaCiaAereaDAO extends BaseCrudDao<FaturaCiaAerea, Long> {
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return FaturaCiaAerea.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("ciaAerea", FetchMode.JOIN);
		lazyFindById.put("ciaAerea.pessoa", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	@Override
	public void removeById(Long id) {
		String hql = "delete from " + ItemFaturaCiaAerea.class.getName() + " where faturaCiaAerea.idFaturaCiaAerea = :id";
		getAdsmHibernateTemplate().removeById(hql, id);
		
		hql = "delete from " + FaturaCiaAereaAnexo.class.getName() + " where faturaCiaAerea.idFaturaCiaAerea = :id";
		getAdsmHibernateTemplate().removeById(hql, id);
		
		hql = "delete from " + FaturaCiaAerea.class.getName() + " where idFaturaCiaAerea = :id";
		getAdsmHibernateTemplate().removeById(hql, id);
	}
	
	/**
	 * 
	 * @param faturaCiaAerea
	 * @param listaItemFaturaCiaAerea
	 * @param listaFaturaCiaAereaAnexo
	 * @return
	 */
	public FaturaCiaAerea store(FaturaCiaAerea faturaCiaAerea, ItemList listaItemFaturaCiaAerea, ItemList listaFaturaCiaAereaAnexo) {
		super.store(faturaCiaAerea, true);
		
		/*
		 * Validação LMS-37013 - parte 3:
		 * - NÃO persistir instâncias de ITEM_FATURA_CIA_AEREA
		 */
		if(faturaCiaAerea.getAlteracaoCompleta() == null || faturaCiaAerea.getAlteracaoCompleta()){
			// Salva os filhos - ItemFaturaCiaAerea
			if (listaItemFaturaCiaAerea.getRemovedItems().size() > 0) {
				getAdsmHibernateTemplate().deleteAll(listaItemFaturaCiaAerea.getRemovedItems());
			}
			
			if (listaItemFaturaCiaAerea.getNewOrModifiedItems().size() > 0) {
				super.store(listaItemFaturaCiaAerea.getNewOrModifiedItems(), true);
			}
		}
		
		// Salva os filhos - FaturaCiaAereaAnexo
		if (listaFaturaCiaAereaAnexo.getRemovedItems().size() > 0) {
			getAdsmHibernateTemplate().deleteAll(listaFaturaCiaAereaAnexo.getRemovedItems());
		}
		
		if (listaFaturaCiaAereaAnexo.getNewOrModifiedItems().size() > 0) {
			super.store(listaFaturaCiaAereaAnexo.getNewOrModifiedItems(), true);
		}
		
		return faturaCiaAerea;
	}
	
	/**
	 * 
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		List param = new ArrayList();
		StringBuilder hql = getHqlFindPaginatedFaturaCiaAerea(criteria, param);
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), param.toArray());
	}

	/**
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Integer getRowCount(TypedFlatMap criteria) {
		List param = new ArrayList();
		StringBuilder hql = getHqlFindPaginatedFaturaCiaAerea(criteria, param);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), param.toArray());
	}
	
	/**
	 * 
	 * @param criteria
	 * @param param
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private StringBuilder getHqlFindPaginatedFaturaCiaAerea(TypedFlatMap criteria, List param) {
		StringBuilder query = new StringBuilder();
		query.append("select fca from ");
		query.append(FaturaCiaAerea.class.getName());
		query.append(" fca ");
		query.append("where 1=1 ");

		if(criteria.getLong("idEmpresa") != null){
			query.append("and fca.ciaAerea.idEmpresa = ? ");
			param.add(criteria.getLong("idEmpresa"));
		}
		
		if (criteria.getYearMonthDay("dtEmissaoInicial") != null) {
			query.append("and fca.dtEmissao >= ? ");
			param.add(criteria.getYearMonthDay("dtEmissaoInicial"));
		}

		if (criteria.getYearMonthDay("dtEmissaoFinal") != null) {
			query.append("and fca.dtEmissao <= ? ");
			param.add(criteria.getYearMonthDay("dtEmissaoFinal"));
		}
		
		if(criteria.getLong("nrFaturaCiaAerea") != null){
			query.append("and fca.nrFaturaCiaAerea = ? ");
			param.add(criteria.getLong("nrFaturaCiaAerea"));
		}
		
		String situacao = criteria.getString("situacao");
		if(StringUtils.isNotBlank(situacao)){
			if("DI".equals(situacao)){
				query.append("and fca.dtEnvioJDE is null ");
				query.append("and fca.dtPagamento is null ");
			} else if("EJ".equals(situacao)){
				query.append("and fca.dtEnvioJDE is not null ");
				query.append("and fca.dtPagamento is null ");
			} else if("LI".equals(situacao)){
				query.append("and fca.dtPagamento is not null ");
			}
		}
		
		if(criteria.getYearMonthDay("dtVencimentoInicial") != null){
			query.append("and fca.dtVencimento >= ? ");
			param.add(criteria.getYearMonthDay("dtVencimentoInicial"));
		}
		
		if(criteria.getYearMonthDay("dtVencimentoFinal") != null){
			query.append("and fca.dtVencimento <= ? ");
			param.add(criteria.getYearMonthDay("dtVencimentoFinal"));
		}
		
		if(criteria.getLong("idAwb") != null){
			query.append("and exists (select 1 from " + ItemFaturaCiaAerea.class.getName() + " ifca where ifca.faturaCiaAerea.idFaturaCiaAerea = fca.idFaturaCiaAerea and ifca.awb.idAwb = ?) ");
			param.add(criteria.getLong("idAwb"));
		}
		
		if(criteria.getYearMonthDay("dtPeriodoInicial") != null && criteria.getYearMonthDay("dtPeriodoFinal") != null){
			//Overlaps = where (s2 < e1 and e2 > s1) or (s1 < e2 and e1 > s2)
			query.append("and ((fca.dtPeriodoInicial < ? and fca.dtPeriodoFinal > ?) or (? < fca.dtPeriodoFinal and ? > fca.dtPeriodoInicial)) ");
			param.add(criteria.getYearMonthDay("dtPeriodoFinal"));
			param.add(criteria.getYearMonthDay("dtPeriodoInicial"));
			param.add(criteria.getYearMonthDay("dtPeriodoInicial"));
			param.add(criteria.getYearMonthDay("dtPeriodoFinal"));
			
		} else { 
			if(criteria.getYearMonthDay("dtPeriodoInicial") != null){
				query.append("and fca.dtPeriodoInicial >= ? ");
				param.add(criteria.getYearMonthDay("dtPeriodoInicial"));
			}
		
			if(criteria.getYearMonthDay("dtPeriodoFinal") != null){
				query.append("and fca.dtPeriodoFinal <= ? ");
				param.add(criteria.getYearMonthDay("dtPeriodoFinal"));
			}
		}
		
		if(criteria.getYearMonthDay("dtEnvioJDEInicial") != null){
			query.append("and fca.dtEnvioJDE >= ? ");
			param.add(criteria.getYearMonthDay("dtEnvioJDEInicial"));
		}
		
		if(criteria.getYearMonthDay("dtEnvioJDEFinal") != null){
			query.append("and fca.dtEnvioJDE <= ? ");
			param.add(criteria.getYearMonthDay("dtEnvioJDEFinal"));
		}
		
		if(criteria.getYearMonthDay("dtPagamentoInicial") != null){
			query.append("and fca.dtPagamento >= ? ");
			param.add(criteria.getYearMonthDay("dtPagamentoInicial"));
		}
		
		if(criteria.getYearMonthDay("dtPagamentoFinal") != null){
			query.append("and fca.dtPagamento <= ? ");
			param.add(criteria.getYearMonthDay("dtPagamentoFinal"));
		}
		
		query.append("order by fca.ciaAerea.pessoa.nmPessoa, fca.nrFaturaCiaAerea");

		return query;
	}

	/**
	 * 
	 * @param idFaturaCiaAerea
	 * @param idCiaAerea
	 * @param nrFaturaCiaAerea
	 * @return
	 */
	public List<FaturaCiaAerea> findFaturaCiaAereaMesmaCiaAereaNrFatura(Long idFaturaCiaAerea, Long idCiaAerea, Long nrFaturaCiaAerea) {
		Criteria crit = getSession().createCriteria(FaturaCiaAerea.class);
		crit.createAlias("ciaAerea", "ca");
		
		crit.add(Restrictions.eq("nrFaturaCiaAerea", nrFaturaCiaAerea));
		crit.add(Restrictions.eq("ca.idEmpresa", idCiaAerea));
		
		if (idFaturaCiaAerea != null) {
			crit.add(Restrictions.ne("idFaturaCiaAerea", idFaturaCiaAerea));
		}
		
		return crit.list();
	}
	
    /**
     * 
     * @param idFaturaCiaAerea
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<ItemFaturaCiaAerea> findItemFaturaCiaAereaByIdFaturaCiaAerea(Long idFaturaCiaAerea) {
		List<ItemFaturaCiaAerea> listaItemFaturaCiaAerea = new ArrayList<ItemFaturaCiaAerea>();

		if (idFaturaCiaAerea != null) {
			StringBuilder hql = new StringBuilder();
			hql.append("SELECT ifca ");
			hql.append("FROM ");
			hql.append(ItemFaturaCiaAerea.class.getName()).append(" ifca ");
			hql.append("join ifca.faturaCiaAerea fca ");
			hql.append("join ifca.awb awb ");
			hql.append("left join awb.ciaFilialMercurio cia ");
			hql.append("left join cia.empresa e ");
			hql.append("WHERE fca.idFaturaCiaAerea = ?");

			listaItemFaturaCiaAerea = this.getAdsmHibernateTemplate().find(hql.toString(), new Object[] { idFaturaCiaAerea });
		}
		
		return listaItemFaturaCiaAerea;
	}

	/**
	 * 
	 * @param idFaturaCiaAerea
	 * @return
	 */
	public Integer getRowCountItemFaturaCiaAerea(Long idFaturaCiaAerea) {
		Criteria crit = getSession().createCriteria(ItemFaturaCiaAerea.class);
		crit.add(Restrictions.eq("faturaCiaAerea.idFaturaCiaAerea", idFaturaCiaAerea));
		crit.setProjection(Projections.rowCount());
		return (Integer) crit.uniqueResult();
	}
	
	/**
	 * 
	 * @param idFaturaCiaAerea
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FaturaCiaAereaAnexo> findFaturaCiaAereaAnexoByIdFaturaCiaAerea(Long idFaturaCiaAerea) {
		List<FaturaCiaAereaAnexo> listaFaturaCiaAereaAnexo = new ArrayList<FaturaCiaAereaAnexo>();

		if (idFaturaCiaAerea != null) {
			StringBuilder hql = new StringBuilder();
			hql.append("SELECT fcaa ");
			hql.append("FROM ");
			hql.append(FaturaCiaAereaAnexo.class.getName()).append(" fcaa ");
			hql.append("join fcaa.faturaCiaAerea fca ");
			hql.append("WHERE fca.idFaturaCiaAerea = ?");

			listaFaturaCiaAereaAnexo = this.getAdsmHibernateTemplate().find(hql.toString(), new Object[] { idFaturaCiaAerea });
		}
		
		return listaFaturaCiaAereaAnexo;
	}
	
	/**
	 * 
	 * @param idFaturaCiaAerea
	 * @return
	 */
	public Integer getRowCountFaturaCiaAereaAnexo(Long idFaturaCiaAerea) {
		Criteria crit = getSession().createCriteria(FaturaCiaAereaAnexo.class);
		crit.add(Restrictions.eq("faturaCiaAerea.idFaturaCiaAerea", idFaturaCiaAerea));
		crit.setProjection(Projections.rowCount());
		return (Integer) crit.uniqueResult();
	}
	
	
	/**
	 * Busca a Fatura a partir do AWB
	 * 
	 * @param idAwb
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FaturaCiaAerea> findFaturaCiaAereaByIdAwb(Long idAwb) {
		Criteria crit = getSession().createCriteria(ItemFaturaCiaAerea.class);
		crit.createAlias("awb", "awb");
		crit.createAlias("faturaCiaAerea", "faturaCiaAerea");
		
		crit.add(Restrictions.eq("awb.idAwb", idAwb));
		
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("faturaCiaAerea.idFaturaCiaAerea").as("idFaturaCiaAerea"));
		pl.add(Projections.property("faturaCiaAerea.nrFaturaCiaAerea").as("nrFaturaCiaAerea"));

		crit.setProjection(pl);
		ResultTransformer resultTransformer = Transformers.aliasToBean(FaturaCiaAerea.class);
		crit.setResultTransformer(resultTransformer);
		
		List<FaturaCiaAerea> lista = crit.list();
		
		return lista;
	}

	/**
	 * 
	 * @param idAwb
	 * @param idFaturaCiaAerea
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemFaturaCiaAerea> findFaturaCiaAereaByIdAwbByIdFatura(Long idAwb, Long idFaturaCiaAerea) {
		Criteria crit = getSession().createCriteria(ItemFaturaCiaAerea.class);
		crit.createAlias("awb", "awb");
		crit.createAlias("faturaCiaAerea", "faturaCiaAerea");
		
		crit.add(Restrictions.eq("awb.idAwb", idAwb));
		if (idFaturaCiaAerea != null) {
			crit.add(Restrictions.ne("faturaCiaAerea.idFaturaCiaAerea", idFaturaCiaAerea));
		}
		List<ItemFaturaCiaAerea> lista = crit.list();
		
		return lista;
	}

	public FaturaCiaAereaAnexo findFaturaCiaAereaAnexoById(Long idFaturaCiaAereaAnexo) {
		Criteria crit = getSession().createCriteria(FaturaCiaAereaAnexo.class);
		crit.add(Restrictions.eq("idFaturaCiaAereaAnexo", idFaturaCiaAereaAnexo));
		return (FaturaCiaAereaAnexo) crit.uniqueResult();
	}
}