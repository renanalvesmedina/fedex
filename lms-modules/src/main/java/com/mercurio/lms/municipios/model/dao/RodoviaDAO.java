package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Rodovia;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RodoviaDAO extends BaseCrudDao<Rodovia, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Rodovia.class;
    } 

    protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("pais",FetchMode.JOIN);
    }
    
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("pais",FetchMode.JOIN);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = montaQuery(criteria);
	    ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());		
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = montaQuery(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;    
	}
	
	private SqlTemplate montaQuery(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("new Map(RO.idRodovia", "idRodovia");
        sql.addProjection("RO.sgRodovia", "sgRodovia");
		sql.addProjection("RO.dsRodovia", "dsRodovia"); 
		sql.addProjection("PA.nmPais", "pais_nmPais");
		sql.addProjection("UF.sgUnidadeFederativa", "unidadeFederativa_sgUnidadeFederativa");
		sql.addProjection("UF.idUnidadeFederativa", "unidadeFederativa_idUnidadeFederativa");
		sql.addProjection("RO.tpSituacao", "tpSituacao)");
		
		StringBuilder sqlFrom = new StringBuilder()
				.append(Rodovia.class.getName()).append(" RO ")
				.append("left join RO.unidadeFederativa  as UF ")
				.append("inner join RO.pais              as PA ");
		sql.addFrom(sqlFrom.toString());
		
		if (StringUtils.isBlank(criteria.getString("flag")))
			sql.addCriteria("UF.idUnidadeFederativa","=",criteria.getLong("unidadeFederativa.idUnidadeFederativa"));
		else if (!StringUtils.isBlank(criteria.getString("unidadeFederativa.idUnidadeFederativa"))) {
			sql.addCustomCriteria(
				(new StringBuffer(80)).append("((UF.idUnidadeFederativa = ")
					.append(criteria.getString("unidadeFederativa.idUnidadeFederativa"))
					.append(") OR (UF.idUnidadeFederativa is null))").toString());
		}
		
		sql.addCriteria("RO.tpSituacao", "=", criteria.getString("tpSituacao"));
		if (criteria.get("dsRodovia") != null) {
			sql.addCriteria("lower(RO.dsRodovia)", "like", criteria.getString("dsRodovia").toLowerCase());
		}
 	    sql.addCriteria("RO.sgRodovia","like", criteria.getString("sgRodovia"));
	    sql.addCriteria("PA.idPais", "=", criteria.getLong("pais.idPais"));
	    
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("PA.nmPais", LocaleContextHolder.getLocale()));
    	sql.addOrderBy("RO.sgRodovia");
		
		return sql;
	}   
	
	public List findLookupWithUFNull(Map criteria) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.setFetchMode("unidadeFederativa",FetchMode.JOIN);
		dc.setFetchMode("pais",FetchMode.JOIN);
		if (StringUtils.isBlank((String)criteria.get("flag")) && !StringUtils.isBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"unidadeFederativa.idUnidadeFederativa")))
			dc.add(Restrictions.eq("unidadeFederativa.idUnidadeFederativa",Long.valueOf((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"unidadeFederativa.idUnidadeFederativa"))));
		else if (!StringUtils.isBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"unidadeFederativa.idUnidadeFederativa")))
			dc.add(Restrictions.or(Restrictions.eq("unidadeFederativa.idUnidadeFederativa",Long.valueOf((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"unidadeFederativa.idUnidadeFederativa"))),
					Restrictions.isNull("unidadeFederativa.idUnidadeFederativa")));
		if(!StringUtils.isBlank((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"pais.idPais")))
			dc.add(Restrictions.eq("pais.idPais",
					Long.valueOf((String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"pais.idPais"))));
		dc.add(Restrictions.ilike("sgRodovia",(String)ReflectionUtils.getNestedBeanPropertyValue(criteria,"sgRodovia")));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	
	public boolean possuiUnidadeFederativaDiferente(Long idRodovia, Long idUnidadeFederativa) {
		DetachedCriteria dc = DetachedCriteria.forClass( getPersistentClass());
			dc.createAlias("unidadeFederativa", "uf");
			dc.add(Restrictions.eq("idRodovia", idRodovia));
			dc.add(Restrictions.and(Restrictions.isNotNull("uf.idUnidadeFederativa"),
									Restrictions.ne("uf.id", idUnidadeFederativa)));
			return findByDetachedCriteria(dc).size() > 0;
	}
	public List findRodoviaByUf(Long idRodovia,Long idUf) {
		   DetachedCriteria dc = createDetachedCriteria();
		   dc.setProjection(Projections.property("idRodovia"));
		   dc.add(Restrictions.eq("idRodovia",idRodovia));
		   dc.add(Restrictions.or(Restrictions.isNull("unidadeFederativa.idUnidadeFederativa"),Restrictions.eq("unidadeFederativa.idUnidadeFederativa",idUf)));
		   return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List findRodoviaBySgAndPais(String sgRodovia,Long idPais) {
		   ProjectionList pl = Projections.projectionList()
		   				    .add(Projections.alias(Projections.property("R.dsRodovia"),"dsRodovia"))
		   				  	.add(Projections.alias(Projections.property("R.sgRodovia"),"sgRodovia"))
		   				  	.add(Projections.alias(Projections.property("R.idRodovia"),"idRodovia"));
			DetachedCriteria dc = DetachedCriteria.forClass(Rodovia.class,"R")
							   .setProjection(pl)
							   .add(Restrictions.ilike("R.sgRodovia",sgRodovia))
							   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
			if (idPais != null) {
				dc.createAlias("R.pais","P")
				   .add(Restrictions.eq("P.idPais",idPais));
			}
		   return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

}