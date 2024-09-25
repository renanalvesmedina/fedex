package com.mercurio.lms.tributos.model.dao;

import java.util.List;
import java.util.Map;


import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.RemetenteExcecaoICMSCli;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RemetenteExcecaoICMSCliDAO extends BaseCrudDao<RemetenteExcecaoICMSCli, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RemetenteExcecaoICMSCli.class;
    }
    
    /**
	 * Método responsável por buscar RemetenteExcecaoICMSCli que estejam no mesmo intervalo de vigência
	 * 
	 * @author HectorJ
	 * @since 31/05/20006
	 * 
	 * @param vigenciaInicial
	 * @param vigenciaFinal
	 * @return List <RemetenteExcecaoICMSCli>
	 */
	public List findRemetenteExcecaoICMSCliByVigenciaEquals(
					  YearMonthDay vigenciaInicial
					, YearMonthDay vigenciaFinal
					, Long idExcecaoICMSCliente
					, String nrCnpjParcialRem
					, Long idRemetenteExcecaoICMSCli ){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("reic");
		
		hql.addFrom(getPersistentClass().getName() + " reic ");
		
		/** Criteria para buscar registros no mesmo intervalo de vigência */ 
		hql.addCustomCriteria("( (? between reic.dtVigenciaInicial and reic.dtVigenciaFinal) " +
							  " OR (? between reic.dtVigenciaInicial and reic.dtVigenciaFinal) " +
							  " OR (? < reic.dtVigenciaInicial  AND ? > reic.dtVigenciaFinal) )");
		
		hql.addCriteriaValue(vigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
		hql.addCriteriaValue(vigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
		
		hql.addCriteria("reic.idRemetenteExcecaoICMSCli", "<>", idRemetenteExcecaoICMSCli);
		
		hql.addCriteria("reic.excecaoICMSCliente.idExcecaoICMSCliente", "=", idExcecaoICMSCliente);
		if (nrCnpjParcialRem.length() == 8) {
			hql.addCriteria("SUBSTR(reic.nrCnpjParcialRem, 1, 8)", "=", nrCnpjParcialRem);
		} else {
			hql.addCustomCriteria("(reic.nrCnpjParcialRem = ? OR " +
					"reic.nrCnpjParcialRem = ?)");
			hql.addCriteriaValue(nrCnpjParcialRem.substring(0, 8));
			hql.addCriteriaValue(nrCnpjParcialRem);	
		}
		
		List lst = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		return lst;
	}

	
	/**
	 * Metodo utilizado nas regras do CFOP
	 * 
	 * @param nrIdentificacao
	 * @param idTipoTributacaoDevido
	 * @param idFilialOrigemDocto
	 * @return List
	 */
	public List findRemetenteExcecaoICMSByDadosCliente(String nrIdentificacao, Long idTipoTributacaoDevido, Long idFilialOrigemDocto){
		
		DetachedCriteria dc = createDetachedCriteria()
		.createAlias("excecaoICMSCliente", "eic")
		.createAlias("eic.tipoTributacaoIcms", "tpTrib")
		
		.add(Restrictions.or(Restrictions.eq("nrCnpjParcialRem", nrIdentificacao.substring(0, 8)), Restrictions.eq("nrCnpjParcialRem", nrIdentificacao)))
		.add(Restrictions.ge("dtVigenciaInicial", JTDateTimeUtils.getDataAtual()))
		.add(Restrictions.le("dtVigenciaFinal", JTDateTimeUtils.getDataAtual()))
		
		.add(Restrictions.eq("tpTrib.idTipoTributacaoIcms", idTipoTributacaoDevido))
		.add(Restrictions.or(Restrictions.eq("eic.nrCNPJParcialDev", nrIdentificacao.substring(0, 8)), Restrictions.eq("eic.nrCNPJParcialDev", nrIdentificacao)))
		.add(Restrictions.eq("eic.tpFrete", "C"))
		.add(Restrictions.ge("eic.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()))
		.add(Restrictions.le("eic.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		
	}		
	
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("excecaoICMSCliente", FetchMode.JOIN);
		lazyFindById.put("excecaoICMSCliente.unidadeFederativa", FetchMode.JOIN);
		lazyFindById.put("excecaoICMSCliente.tipoTributacaoIcms", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);		
		
	}

	
	/**
	* Retorna os dados para a grid.
	* 
	* @author Diego Umpierre
	* @since 20/07/2006
	* 
	* @param TypedFlatMap criteria, FindDefinition findDef
	* @return ResultSetPage
	* */
	public ResultSetPage findPaginatedTela(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = mountHql(criteria);
		
		sql.addProjection("reic");
		sql.addOrderBy("reic.nrCnpjParcialRem");
		sql.addOrderBy("reic.dtVigenciaInicial");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
		
		return rsp;
	}

	/**
	* Retorna o numero de linhas da grid
	* 
	* @author Diego Umpierre
	* @since 20/07/2006
	* 
	* @param TypedFlatMap criteria
	* @return Integer
	* */	
	public Integer getRowCountTela(TypedFlatMap criteria) {
		SqlTemplate sql = mountHql(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}


	/**
	* Monta o HQL para ser usado na consulta do findPaginated e rowCount.
	* 
	* @author Diego Umpierre
	* @since 20/07/2006
	* 
	* @param Map criteria
	* @return SqlTemplate
	* */
	private SqlTemplate mountHql(TypedFlatMap map){
		
		SqlTemplate sql = new SqlTemplate();
		sql.addInnerJoin(getPersistentClass().getName() + " reic ");
		sql.addInnerJoin(" fetch reic.excecaoICMSCliente","exIcmsCli");
		if (map.getString("nrCnpjParcialRem") != null){
			sql.addCriteria("reic.nrCnpjParcialRem","like",map.getString("nrCnpjParcialRem")+"%");
		}
		sql.addCriteria("exIcmsCli.idExcecaoICMSCliente","=",map.getLong("excecaoICMSCliente.idExcecaoICMSCliente"));
		sql.addCriteria("reic.dtVigenciaInicial","<=",map.getYearMonthDay("dtVigencia"));
		sql.addCriteria("reic.dtVigenciaFinal",">=",map.getYearMonthDay("dtVigencia"));

		return sql;
	}


}