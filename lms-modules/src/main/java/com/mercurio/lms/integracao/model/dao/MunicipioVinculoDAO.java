package com.mercurio.lms.integracao.model.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.integracao.model.MunicipioVinculo;
import com.mercurio.lms.municipios.model.Municipio;

/** 
 * @spring.bean 
 */
public class MunicipioVinculoDAO extends BaseCrudDao<MunicipioVinculo, Long> {

	
	@Override
	protected Class getPersistentClass() {
		return MunicipioVinculo.class;
	}
	 
	
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipioLms", FetchMode.JOIN);
		fetchModes.put("municipioLms.unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("municipioLms.unidadeFederativa.pais", FetchMode.JOIN);
		
		fetchModes.put("municipioCorporativo", FetchMode.JOIN);
		fetchModes.put("municipioCorporativo.pais", FetchMode.JOIN);
	} 
	
	/**
     * Consulta visita para grid
     * @param idVisita
     * @return
     */
    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) { 	
    	SqlTemplate sql = montaSqlPaginated((TypedFlatMap) criteria);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	SqlTemplate sql = montaSqlPaginated(criteria);
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;    
    }
    
    public Municipio findMunicipioLmsByIdIntegracao(String cep) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("ml");
    	sql.addFrom(MunicipioVinculo.class.getName() + " mv inner join mv.municipioLms ml "+
    												   " inner join mv.municipioCorporativo mc ");
    	sql.addCriteria("mc.nrCep", "=", cep);
    	
    	return (Municipio)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
    }
    
    public List<Municipio>  findListMunicipioLmsByIdIntegracao(String cep) {
    	
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("ml");
    	sql.addFrom(MunicipioVinculo.class.getName() + " mv inner join mv.municipioLms ml "+
    	" inner join mv.municipioCorporativo mc ");
    	
    	sql.addCriteria("mc.nrCep", "=", cep);
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
    }
    
    public List findCepMunicipioSOMByIdMunicipio(Long idMunicipio) {
    	StringBuilder sql = new StringBuilder()
		.append("select mc.nrCep")
        .append(" from MunicipioVinculo mv")
	    .append("      join mv.municipioCorporativo mc")
	    .append("      join mv.municipioLms ml")
	    .append(" where ml.id = :idMunicipio");

		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), "idMunicipio", idMunicipio);
    }
    
    public Municipio findMunicipioLmsByIdMunicipio(String idMunicipio) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("ml");
    	sql.addFrom(MunicipioVinculo.class.getName() + " mv inner join mv.municipioLms ml "+
    												   " inner join mv.municipioCorporativo mc ");
    	sql.addCriteria("mc.idMunicipio", "=", idMunicipio);
    	
    	return (Municipio)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
    }
    
    private SqlTemplate montaSqlPaginated(TypedFlatMap parametros){
 	   SqlTemplate sql = new SqlTemplate();
 	   
 	   sql.addProjection("new Map(mv.idMunicipioVinculo", "idMunicipioVinculo");
 	   
 	   sql.addProjection("ml.idMunicipio", "idMunicipioLms");
 	   sql.addProjection("ml.nmMunicipio", "nmMunicipioLms");
 	   sql.addProjection("ml.nrCep", "nrCepLms");
 	   
 	   sql.addProjection("mc.idMunicipio", "idMunicipioCorp");
	   sql.addProjection("mc.nmMunicipio", "nmMunicipioCorp");
	   sql.addProjection("mc.nrCep", "nrCepCorp)");
 	   
	   sql.addFrom(MunicipioVinculo.class.getName() + " mv inner join mv.municipioLms ml " +
	   												      " inner join ml.unidadeFederativa uf " +
	   												          " inner join uf.pais pl " +
													  " left outer join mv.municipioCorporativo mc " +
													      " left outer join mc.pais pc ");
	   
	   sql.addCriteria("ml.idMunicipio", "=", parametros.getLong("municipioLms.idMunicipio"));
	   sql.addCriteria("uf.idUnidadeFederativa", "=", parametros.getLong("municipioLms.unidadeFederativa.idUnidadeFederativa"));
	   sql.addCriteria("pl.idPais", "=", parametros.getLong("municipioLms.unidadeFederativa.pais.idPais"));
	   
	   sql.addCriteria("mc.idMunicipio", "=", parametros.getLong("municipioCorporativo.idMunicipio"));
	   sql.addCriteria("mc.sgUnidadeFederativa", "like", parametros.getString("municipioCorporativo.sgUnidadeFederativa").toUpperCase());
	   sql.addCriteria("pc.idPais", "=", parametros.getLong("municipioCorporativo.pais.idPais"));
	   
	   sql.addOrderBy("ml.nmMunicipio");
 	   return sql;
    }
}
