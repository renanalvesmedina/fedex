package com.mercurio.lms.integracao.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.integracao.model.MunicipioCorporativo;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MunicipioCorporativoDAO extends BaseCrudDao<MunicipioCorporativo, Long>{

	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("pais", FetchMode.JOIN);			
	}
	
	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("pais", FetchMode.JOIN);			
	}
	
	public List findLookupMunicipio(TypedFlatMap criteria){
		return super.findLookupByCriteria(criteria);
	}
	
	
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		return super.findPaginated(criteria, findDef);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return super.getRowCount(criteria);
	}
	
    /**
     * Comando SQL de consulta municipios
     * @param idVisita
     * @return
     */
    private SqlTemplate montaQueryPaginated(TypedFlatMap parametros){
	   SqlTemplate sql = new SqlTemplate();
	   
	   //visita
	   sql.addProjection("new Map(m.idMunicipio", "idMunicipio");
       sql.addProjection("m.nmMunicipio", "nmMunicipio");
	   sql.addProjection("m.nrCep", "nrCep");
	   sql.addProjection("m.cdIbge", "cdIbge");
	   sql.addProjection("m.sgUnidadeFederativa", "sgUnidadeFederativa");
	   
	   sql.addProjection("p.idPais", "idPais");
	   sql.addProjection("p.nmPais", "nmPais)");
	   
	   sql.addFrom(MunicipioCorporativo.class.getName() + " m inner join v.pais p ");
			   								   		
	   sql.addCriteria("m.nmMunicipio", "like", parametros.getString("nmMunicipio"));
	   sql.addCriteria("m.nrCep", "=", parametros.getString("nrCep"));
	   sql.addCriteria("m.cdIbge", "=", parametros.getInteger("cdIbge"));
	   sql.addCriteria("m.sgUnidadeFederativa", "=", parametros.getInteger("sgUnidadeFederativa"));
	   
	   sql.addOrderBy("m.nmMunicipio");
	   return sql;
   }

	
	@Override
	protected Class getPersistentClass() {
		return MunicipioCorporativo.class;
	}

}
