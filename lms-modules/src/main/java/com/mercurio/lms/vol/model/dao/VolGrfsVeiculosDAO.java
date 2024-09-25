package com.mercurio.lms.vol.model.dao;

import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vol.model.VolGrfsVeiculos;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolGrfsVeiculosDAO extends BaseCrudDao<VolGrfsVeiculos, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolGrfsVeiculos.class;
    }

    private SqlTemplate mountSQL() {
    	SqlTemplate sql = new SqlTemplate();
	   	
	   	StringBuffer sb = new StringBuffer();
		sb.append("VolGruposFrotas gf ");
		sb.append("inner join gf.volGrfsVeiculos v ");
		sb.append("inner join v.meioTransporte mt ");
		
		sql.addFrom(sb.toString());
		return sql;
    }
    
    public Boolean getRowCountGruposFrota(Long idGrupoFrota,Long idMeioTransporte, boolean isDiferente) {
	   	SqlTemplate sql = mountSQL();
	   	
	   	if (isDiferente) {
	   		sql.addCriteria("gf.idGrupoFrota","<>",idGrupoFrota);
	   	} else {
	   		sql.addCriteria("gf.idGrupoFrota","=",idGrupoFrota);
    	}
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		
	    Integer result = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria()); 
	    return (result.intValue()> 0 ? Boolean.TRUE:Boolean.FALSE);	
	}

    public Map findGruposFrotaByIdMeioTransporte(Long idMeioTransporte) {
	   	SqlTemplate sql = mountSQL();
	   	
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		
		StringBuffer projecao = new StringBuffer()
		   .append("new map(")
		   .append("gf.idGrupoFrota as idGrupoFrota, ")
		   .append("v.idGruVeic as idGruVeic) ");
		
		sql.addProjection(projecao.toString());
		
	     
	    return (Map)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria()); 
	}
}