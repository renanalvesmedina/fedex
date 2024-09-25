package com.mercurio.lms.contasreceber.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.util.AliasToNestedBeanResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ComposicaoPagamentoRedeco;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicaï¿½ï¿½o
 * atravï¿½s do suporte ao Hibernate em conjunto com o Spring.
 * Nï¿½o inserir documentaï¿½ï¿½o apï¿½s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ComposicaoPagamentoRedecoDAO extends BaseCrudDao<ComposicaoPagamentoRedeco, Long> {

	@Override
	protected Class getPersistentClass() {
		return ComposicaoPagamentoRedeco.class;
	}
	
	@Override
	public void initFindByIdLazyProperties(Map map) {
		map.put("redeco", FetchMode.JOIN);
		map.put("redeco.filial", FetchMode.JOIN);
		map.put("banco", FetchMode.JOIN);
	}
	
	/**
	 * Obtem o valor total das composiï¿½ï¿½es atravï¿½s do Redeco e informando se ï¿½
	 * FC - Frete Classificar ou nï¿½o
	 *  
	 * @param idRedeco
	 * @param freteClassificar
	 * @return BigDecimal
	 */
	public BigDecimal findSumCompPagamentoRedeco(Long idRedeco, Boolean freteClassificar){
		
		DetachedCriteria dc = createDetachedCriteria()
		.setProjection(Projections.sum("vlPagamento"))
		.setFetchMode("redeco", FetchMode.JOIN)
		.setFetchMode("banco", FetchMode.JOIN);
		
		if(freteClassificar){
			dc.add(Restrictions.eq("tpComposicaoPagamentoRedeco", "F"));
		}else{
			dc.add(Restrictions.ne("tpComposicaoPagamentoRedeco", "F"));
		}
		
		dc.add(Restrictions.eq("redeco.id", idRedeco));
		
		return (BigDecimal)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	/**
	 * Filtra os dados atravï¿½s da consulta feita na tela de listagem
	 * 
	 * @param criteria
	 * @return DetachedCriteria
	 */
	@SuppressWarnings("deprecation")
	private DetachedCriteria createCriteria(TypedFlatMap criteria) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(ComposicaoPagamentoRedeco.class,"cpr")
			.createAlias("cpr.redeco", "redeco");

		dc.createAlias("cpr.banco", "banco",JoinFragment.LEFT_OUTER_JOIN);		
		dc.createAlias("cpr.filial", "filial");		
						
		/** Restriï¿½ï¿½es */
		Long idRedeco = criteria.getLong("redeco.idRedeco");
		if(idRedeco != null) {
			dc.add(Restrictions.eq("redeco.idRedeco", idRedeco));
		}
		DomainValue tpComposicaoPagamentoRedeco = criteria.getDomainValue("tpComposicaoPagamentoRedeco");
		if(StringUtils.isNotBlank(tpComposicaoPagamentoRedeco.getValue())) {
			dc.add(Restrictions.eq("cpr.tpComposicaoPagamentoRedeco", tpComposicaoPagamentoRedeco.getValue()));
		}
		Long idBanco = criteria.getLong("banco.idBanco");
		if(idBanco != null){			
			dc.add(Restrictions.eq("banco.idBanco", idBanco));
		}
	
		/*Adiciona o periodo de pagamento na consulta*/  
		YearMonthDay dtPagamentoInicial = criteria.getYearMonthDay("dtPagamentoInicial");
		
		if(dtPagamentoInicial != null){
						
			YearMonthDay dtPagamentoFinal   = criteria.getYearMonthDay("dtPagamentoFinal");
			if(dtPagamentoFinal == null){
				dtPagamentoFinal = JTDateTimeUtils.MAX_YEARMONTHDAY;
			}
			
			dc.add(Restrictions.ge("cpr.dtPagamento", dtPagamentoInicial));
			dc.add(Restrictions.le("cpr.dtPagamento", dtPagamentoFinal));
		}
		
		dc.addOrder(Order.asc("cpr.tpComposicaoPagamentoRedeco"));
		dc.addOrder(Order.asc("banco.idBanco"));
		dc.addOrder(Order.asc("cpr.dtPagamento"));
		
		return dc;
	}	
	
	/**
	 * Cria a pï¿½ginaï¿½ï¿½o da grid na tela de listagem
	 * 
	 * @param  criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("idComposicaoPagamentoRedeco"), "idComposicaoPagamentoRedeco")
			.add(Projections.property("filial.sgFilial"), "filial.sgFilial")
			.add(Projections.property("tpComposicaoPagamentoRedeco"), "tpComposicaoPagamentoRedeco")
			.add(Projections.property("dtPagamento"), "dtPagamento")
			.add(Projections.property("vlPagamento"), "vlPagamento")
			.add(Projections.property("banco.nmBanco"), "banco.nmBanco")
			.add(Projections.property("obComposicaoPagamentoRedeco"), "obComposicaoPagamentoRedeco");
		
		DetachedCriteria dc = createCriteria(criteria);
		dc.setProjection(pl);
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());		
	}
	
	/**
	 * Obtem o numero de linhas da consulta atravï¿½s dos filtros da tela de 
	 * listagem
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCount(TypedFlatMap criteria) {	
		
		DetachedCriteria dc = createCriteria(criteria);
		dc.setProjection(Projections.rowCount());
		
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public List<ComposicaoPagamentoRedeco> findByIdRedeco(Long idRedeco) {
		DetachedCriteria dc = DetachedCriteria.forClass(ComposicaoPagamentoRedeco.class)
			.setFetchMode("redeco", FetchMode.JOIN)
			.setFetchMode("creditoBancario", FetchMode.JOIN)
			.add(Restrictions.eq("redeco.id", idRedeco));
		
		List<ComposicaoPagamentoRedeco> composicoesPagamentoRedeco = getAdsmHibernateTemplate().findByCriteria(dc);
		
		return composicoesPagamentoRedeco;
	}
	
	/**
	 * LMS-2772
	 * Mesmo que <i>findByIdRedeco</i>, porém, não utiliza DetachedCriteria
	 * 
	 * @param idRedeco
	 * @return
	 */
	public List<ComposicaoPagamentoRedeco> findByIdRedecoTpComposicao(Long idRedeco, String tpComposicaoPagamentoRedeco) {

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select cpr ");
		hql.append(" from ");
		hql.append(ComposicaoPagamentoRedeco.class.getName() + " as cpr ");
		hql.append(" join cpr.redeco as re ");
		hql.append(" left outer join cpr.creditoBancario as cb ");
		hql.append(" where ");
		hql.append("	re.id = ? ");
		hql.append("	and cpr.tpComposicaoPagamentoRedeco = ? ");
		
		List<ComposicaoPagamentoRedeco> composicoesPagamentoRedeco = super.getAdsmHibernateTemplate().find(hql.toString(), new Object[] {idRedeco, tpComposicaoPagamentoRedeco});
		return composicoesPagamentoRedeco;
	}

	/**
	 * Retorna a maior data de pagamento das composiÃ§Ãµes do redeco informado
	 * 
	 * @param idRedeco
	 * @return
	 */
	public YearMonthDay findMaiorDataCredito(Long idRedeco) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select max(CPR.dt_Pagamento) as DT_PAGAMENTO ");
		hql.append(" from	");
		hql.append("	COMPOSICAO_PAGAMENTO_REDECO CPR ");
		hql.append(" where ");
		hql.append("	CPR.ID_REDECO = :idRedeco  ");

        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {                
                sqlQuery.addScalar("DT_PAGAMENTO", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
            }
        };
                
		Map parameters = new HashMap();
		parameters.put("idRedeco", idRedeco);

		List result = this.getAdsmHibernateTemplate().findBySql(hql.toString(), parameters, configSql);
		YearMonthDay max = (YearMonthDay) result.get(0);

		return max;
	}

}