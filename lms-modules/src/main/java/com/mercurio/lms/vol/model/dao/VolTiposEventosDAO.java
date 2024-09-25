package com.mercurio.lms.vol.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolTiposEventos;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolTiposEventosDAO extends BaseCrudDao<VolTiposEventos, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolTiposEventos.class;
    }
    
    public ResultSetPage findPaginatedEventosOperacao(TypedFlatMap criteria, FindDefinition fd) {
    	SqlTemplate sql = this.createHbmEventosOperacao(criteria);
    	
    	StringBuffer projecao = new StringBuffer()
    		.append("new Map (" )
    		.append("tpe.idTipoEvento as idTipoEvento, ")
    		.append("tpe.nmCodigo as nmCodigo, ")
    		.append("tpe.dsNome as dsNome, ")
    		.append("tpe.tpTipoEvento as tpTipoEvento )" );
    	
    	sql.addOrderBy("tpe.nmCodigo");
    	sql.addProjection(projecao.toString());
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), fd.getCurrentPage(), fd.getPageSize(),
  			   sql.getCriteria());
    	
    }
    
    public Integer getRowCountEventosOperacao(TypedFlatMap criteria){
    	SqlTemplate sql = this.createHbmEventosOperacao(criteria);
   	   return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }
    
    private SqlTemplate createHbmEventosOperacao(TypedFlatMap criteria){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(VolTiposEventos.class.getName(), "tpe");
    	sql.addCriteria("lower(tpe.dsNome)", "LIKE", criteria.getString("nmEvento").toLowerCase());
    	sql.addCriteria("tpe.tpTipoEvento", "=", criteria.getString("evento"));
    	return sql;
    }
    



}