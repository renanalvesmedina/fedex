package com.mercurio.lms.vol.model.dao;



import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolTiposUso;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolTiposUsoDAO extends BaseCrudDao<VolTiposUso, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolTiposUso.class;
    }
    
    public Integer getRowCountTiposUso(TypedFlatMap criteria){
       SqlTemplate sql = this.createHQLTiposUso(criteria);
  	   return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }
    
    public ResultSetPage findPaginatedTiposUso(TypedFlatMap criteria, FindDefinition fd){
    	SqlTemplate sql = this.createHQLTiposUso(criteria);
    	
    	StringBuffer projecao = new StringBuffer()
    		.append("new Map(" )
    		.append("tp.idTiposUso as idTiposUso, ")
    		.append("tp.dsNome as dsNome) ");
    	
    	sql.addProjection(projecao.toString());
    	sql.addOrderBy("tp.dsNome");
    	 
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), fd.getCurrentPage(), fd.getPageSize(),
 			   sql.getCriteria());
    }
    
    public SqlTemplate createHQLTiposUso(TypedFlatMap criteria){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(VolTiposUso.class.getName(), "tp");
    	sql.addCriteria("lower(tp.dsNome)", "LIKE", criteria.getString("dsNome").toLowerCase());
    	
    	return sql;
    }
    
}