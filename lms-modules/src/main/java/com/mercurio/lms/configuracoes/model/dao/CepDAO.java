/*
 * Created on Aug 24, 2005
 *
 */
package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToNestedMapResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Cep;
import com.mercurio.lms.configuracoes.model.param.PesquisarCepParam;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 *@spring.bean 
 */
public class CepDAO extends BaseCrudDao<Cep, Long>{
	
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(" new Map(pa.nmPais as municipio_unidadeFederativa_pais_nmPais," +
				" pa.idPais as municipio_unidadeFederativa_pais_idPais," +
				" pa.sgPais as municipio_unidadeFederativa_pais_sgPais," +
				" cp.dsTipoLogradouro as dsTipoLogradouro, " +
				" cp.dsLogComplemento as dsLogComplemento, " +
				" uf.sgUnidadeFederativa as municipio_unidadeFederativa_sgUnidadeFederativa," +
				" uf.idUnidadeFederativa as municipio_unidadeFederativa_idUnidadeFederativa," +
				" uf.nmUnidadeFederativa as municipio_unidadeFederativa_nmUnidadeFederativa," +
				" mun.nmMunicipio as municipio_nmMunicipio, cp.nmBairro as nmBairro, " +
				" mun.idMunicipio as municipio_idMunicipio,  " +
				" cp.nmLogradouro as nmLogradouro, cp.nrCep as nrCep)");
		sql.addFrom(Cep.class.getName() + " cp left join cp.municipio mun left join mun.unidadeFederativa uf join uf.pais pa ");
		
		sql.addCriteria("pa.id", "=", ReflectionUtils.getNestedBeanPropertyValue(criteria, "municipio.unidadeFederativa.pais.idPais"));
		sql.addCriteria("uf.id", "=", ReflectionUtils.getNestedBeanPropertyValue(criteria, "municipio.unidadeFederativa.idUnidadeFederativa"));
		sql.addCriteria("mun.id", "=", ReflectionUtils.getNestedBeanPropertyValue(criteria, "municipio.idMunicipio"));
		
		sql.addCriteria("upper(cp.nmBairro)", "like", ((String) criteria.get("nmBairro")).toUpperCase());	

		sql.addCriteria("upper(cp.nmLogradouro)", "like", ((String) criteria.get("nmLogradouro")).toUpperCase());	

		sql.addCriteria("upper(cp.nrCep)", "like", (String) criteria.get("nrCep"));
		
		sql.addCriteria("cp.dsTipoLogradouro", "=", (String) criteria.get("dsLogradouro"));

		sql.addOrderBy("uf.sgUnidadeFederativa");
		sql.addOrderBy("mun.nmMunicipio");
		sql.addOrderBy("cp.nmBairro");
		sql.addOrderBy("cp.nmLogradouro");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
		
		List list = rsp.getList();
		list = new AliasToNestedBeanResultTransformer(Cep.class).transformListResult(list);
		rsp.setList(list);
		
		return rsp;
	}

	public List findLookupByCriteria(Map map) {
		String cepCriteria = (String) map.remove("cepCriteria");
		if (cepCriteria != null) map.put("nrCep", cepCriteria);
		return super.findLookupByCriteria(map);
	}

	/**
	 * Busca dados do cep para pesquisa em Lookups retornando apenas
	 * o necessario na forma de Map.<br/>
	 * Este metodo foi implementado para questoes de performance.
	 * @param map
	 * @return
	 */
	public List findDadosLookup(Map map) {
		String cepCriteria = (String) map.remove("cepCriteria");

		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("p.idPais"), "idPais")
		.add(Projections.property("p.nmPais"), "nmPais")
		.add(Projections.property("uf.idUnidadeFederativa"), "idUnidadeFederativa")
		.add(Projections.property("m.idMunicipio"), "idMunicipio")
		.add(Projections.property("m.nmMunicipio"), "nmMunicipio")
		.add(Projections.property("c.nrCep"), "nrCep")
		.add(Projections.property("c.nmBairro"), "nmBairro")
		.add(Projections.property("c.nmLogradouro"), "nmLogradouro")
		.add(Projections.property("c.dsTipoLogradouro"), "dsTipoLogradouro");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
		.setProjection(pl)
		.createAlias("c.municipio", "m")
		.createAlias("m.unidadeFederativa", "uf")
		.createAlias("uf.pais", "p")
		.add(Restrictions.ilike("c.nrCep", cepCriteria))
		.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("municipio", FetchMode.JOIN);
		lazyFindLookup.put("municipio.unidadeFederativa", FetchMode.JOIN);
		lazyFindLookup.put("municipio.unidadeFederativa.pais", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("municipio", FetchMode.JOIN);
		lazyFindPaginated.put("municipio.unidadeFederativa", FetchMode.JOIN);
		lazyFindPaginated.put("municipio.unidadeFederativa.pais", FetchMode.JOIN);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#getPersistentClass()
	 */
	protected Class getPersistentClass() {
		return Cep.class;
	}

	/**
	 * Busca o cep informado baseando-se no pais
	 *
	 * @author José Rodrigo Moraes
	 * @since 23/01/2007
	 *
	 * @param nrCep Número do CEP
	 * @param idPais Identificador do País
	 * @return Lista de ceps
	 */
	public List findCepLookup(PesquisarCepParam param) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("cep");
		
		hql.addLeftOuterJoin(Cep.class.getName(),"cep");
		hql.addLeftOuterJoin("fetch cep.municipio","mun");
		hql.addLeftOuterJoin("fetch mun.unidadeFederativa","uf");
		hql.addLeftOuterJoin("fetch uf.pais","p");
		
		if(param.getNrCep().endsWith("%")) {
		hql.addCriteria("cep.nrCep","like",param.getNrCep());
		}else{
			hql.addCriteria("cep.nrCep","=",param.getNrCep());			
		}
		hql.addCriteria("p.id","=",param.getIdPais());
		hql.addCriteria("mun.id", "=", param.getIdMunicipio());
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	/**
	 * Busca as informações sobre o CEP informado.
	 *
	 * @author Roberto Coral Azambuja
	 * @since 27/04/2009
	 *
	 * @param nrCep Número do CEP
	 * @return Lista de ceps
	 */
	public List<Cep> findCepByNrCep(String nrCep) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("cep");
		hql.addProjection("mun");
		hql.addProjection("uf");
		hql.addProjection("pa");
		
		hql.addLeftOuterJoin(Cep.class.getName(),"cep");
		hql.addLeftOuterJoin("fetch cep.municipio","mun");
		hql.addLeftOuterJoin("fetch mun.unidadeFederativa","uf");
		hql.addLeftOuterJoin("fetch uf.pais","pa");
		hql.addCriteria("cep.nrCep","=",nrCep);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	/**
	 * FindPaginated substituto do método padrão necessário para complemento
	 * do número do CEP caso o país informado seja BRASIL
	 *
	 * @author José Rodrigo Moraes
	 * @since 23/01/2007
	 *
	 * @param param javaBean de critérios de pesquisa (setado na action)
	 * @return
	 */
	public ResultSetPage findPaginatedEspecific(PesquisarCepParam param, FindDefinition findDef) {
		
		SqlTemplate hql = getHqlPadrao(param);
		
		hql.addProjection("cep");
		
		hql.addOrderBy("uf.sgUnidadeFederativa");
		hql.addOrderBy("mun.nmMunicipio");
		hql.addOrderBy("cep.nmBairro");
		hql.addOrderBy("cep.nmLogradouro");
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
	}

	/**
	 * Rowcount substituto do método padrão necessário para complemento 
	 * do número do CEP caso o país informado seja BRASIL
	 *
	 * @author José Rodrigo Moraes
	 * @since 23/01/2007
	 *
	 * @param param javaBean de critérios de pesquisa (setado na action)
	 * @return 
	 */
	public Integer getRowCountEspecific(PesquisarCepParam param) {		
		SqlTemplate hql = getHqlPadrao(param);		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());		
	}

	/**
	 * Query hql padrão
	 * 
	 *
	 * @author José Rodrigo Moraes
	 * @since 23/01/2007
	 *
	 * @param param
	 * @return
	 */
	private SqlTemplate getHqlPadrao(PesquisarCepParam param) {
		SqlTemplate hql = new SqlTemplate();

		hql.addLeftOuterJoin(Cep.class.getName(), "cep");
		hql.addLeftOuterJoin("fetch cep.municipio", "mun");
		hql.addLeftOuterJoin("fetch mun.unidadeFederativa", "uf");
		hql.addLeftOuterJoin("fetch mun.unidadeFederativa.pais");

		if( StringUtils.isNotBlank(param.getNrCep()) ){
			hql.addCriteria("lower(cep.nrCep)","like",param.getNrCep().toLowerCase());
		}

		hql.addCriteria("uf.pais.id","=",param.getIdPais());
		hql.addCriteria("mun.id","=",param.getIdMunicipio());
		hql.addCriteria("uf.id","=",param.getIdUnidadeFederativa());

		if(StringUtils.isNotBlank(param.getDsLogradouro()) ){
			hql.addCriteria("lower(cep.nmLogradouro)", "like", param.getDsLogradouro().toLowerCase());
		}

		if(StringUtils.isNotBlank(param.getDsTipoLogradouro())) {
			hql.addCriteria("lower(cep.dsTipoLogradouro)", "=", param.getDsTipoLogradouro().toLowerCase());
		}

		if(StringUtils.isNotBlank(param.getDsLogradouro()) ){
			hql.addCriteria("lower(cep.nmLogradouro)", "like", param.getDsLogradouro().toLowerCase());
		}

		if(StringUtils.isNotBlank(param.getNmBairro())) {
			hql.addCriteria("lower(cep.nmBairro)", "like", param.getNmBairro().toLowerCase());
		}

		return hql;
	}

}
