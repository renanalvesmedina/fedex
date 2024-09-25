package com.mercurio.lms.vol.model.dao;


import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolContatos;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ContatosVolDAO extends BaseCrudDao<VolContatos, Long>{
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolContatos.class;
    }
    
    
    public ResultSetPage findPaginatedContatos(TypedFlatMap criteria, FindDefinition fd) {
    	SqlTemplate sql = this.createHQLContatos(criteria);
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new map (" )
    		.append("con.idContato as idContato, ")
    		.append("con.nmContato as nmContato, ")
    		.append("pes.nmPessoa as nmPessoa, ")
    		.append("pes.nrIdentificacao as nrIdentificacao, ")
    		.append("pes.tpIdentificacao as tpIdentificacao, ")
    		.append("con.dsEmail as dsEmail, ")
    		.append("con.dsDepartamento as dsDepartamento, ")
    		.append("con.dsFuncao as dsFuncao, ")
    		.append("con.obContato as obContato, ")
			.append("con.blAtivo as blAtivo) ");
    	
    	sql.addProjection(projecao.toString());
    	sql.addOrderBy("con.nmContato"); 
    	  	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), fd.getCurrentPage(), fd.getPageSize(),
  			   sql.getCriteria()); 
    }
    
    public Integer getRowCountContatos(TypedFlatMap criteria){
       SqlTemplate sql = this.createHQLContatos( criteria ); 
   	   return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
     }
    
    private SqlTemplate createHQLContatos ( TypedFlatMap criteria ){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom( VolContatos.class.getName() + " as con " +
    			"INNER JOIN con.pessoa as pes " +
    			"INNER JOIN con.filial fil " );
    	sql.addCriteria("fil.idFilial", "=", criteria.getLong("filial.idFilial"));
    	
    	if ( criteria.getBoolean("cnpjParcial") ){	
    		String cnpj = criteria.getString("cnpj").substring(0, 8) + "%"; 
	    	criteria.put("cnpj", cnpj);
    		sql.addCriteria("pes.nrIdentificacao", "LIKE", criteria.getString("cnpj"));
    	} else {
    		sql.addCriteria("pes.idPessoa", "=", criteria.getLong("pessoa.idPessoa"));
    	}
    	if (criteria.getBoolean("ativo") != null) {
    		sql.addCriteria("con.blAtivo", "=", criteria.getBoolean("ativo"));
    	}
    	
    	sql.addCriteria("lower(con.nmContato)", "LIKE", criteria.getString("nmContato").toLowerCase());
    	sql.addCriteria("lower(con.dsEmail)", "LIKE", criteria.getString("dsEmail").toLowerCase());
    	
    	return sql;
    }
    
    /**
     * retorna ContatoVol pelo nome do contato, levando em consideração a empresa do contato
     * @param criteria
     * @return
     */
    public List findContatoVolByNomeContato(TypedFlatMap criteria){
    	SqlTemplate sql = this.createHQLNomeContato(criteria);
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new map (" )
    		.append("con.idContato as idContato, ")
    		.append("con.nmContato as nmContato, ")
    		.append("pes.nmPessoa as nmPessoa ) ");
    	
    	sql.addProjection(projecao.toString());
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    public SqlTemplate createHQLNomeContato(TypedFlatMap criteria){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom( VolContatos.class.getName() + " as con " +
    			"INNER JOIN con.pessoa as pes " ); 		
    	sql.addCriteria("pes.idPessoa", "=", criteria.getLong("idPessoa"));	
    	sql.addCriteria("lower(con.nmContato)", "=", criteria.getString("nmContato").toLowerCase().trim());
    	
    	return sql;
    	
    }
    
    /**
     * retorna VolContato pelo e-mail exato do contato
     * @param criteria
     * @return
     */
    public List findContatoVolByEmailContato(TypedFlatMap criteria){
    	SqlTemplate sql = this.createHQLContatoByEmailContato(criteria);
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new map (" )
    		.append("con.idContato as idContato, ")
    		.append("con.nmContato as nmContato, ")
    		.append("con.dsEmail as dsEmail) ");
    		
    	sql.addProjection(projecao.toString());
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    } 
    
    
    public SqlTemplate createHQLContatoByEmailContato(TypedFlatMap criteria){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom( VolContatos.class.getName() + " as con ");		
    	sql.addCriteria("lower(con.dsEmail)", "=", criteria.getString("dsEmail").toLowerCase().trim());
	    sql.addCriteria("con.pessoa", "=", criteria.getLong("pessoa.idPessoa") );
    	
    	return sql;
    	
    }
    
    
    /**
     * retorna ContatoVol pelo e-mail parcial do contato
     * @param criteria
     * @return
     */
    public List findContatoVolByEmailParcial(TypedFlatMap criteria){
    	SqlTemplate sql = this.createHQLContatoByEmailParcial(criteria);
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new map (" )
    		.append("con.idContato as idContato, ")
    		.append("con.nmContato as nmContato, ")
    		.append("con.dsEmail as dsEmail, ")
    		.append("pes.idPessoa as idPessoa, ")
    		.append("pes.nmPessoa as nmPessoa, ")
    		.append("pes.nrIdentificacao as nrIdentificacao ) "); 
    		
    		
    	sql.addProjection(projecao.toString());
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    } 
    
    
   
    public SqlTemplate createHQLContatoByEmailParcial(TypedFlatMap criteria){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom( VolContatos.class.getName() + " as con " +
    			"INNER JOIN con.pessoa as pes " );		
    	sql.addCriteria("lower(con.dsEmail)", "LIKE", criteria.getString("dsEmail").toLowerCase() );
	    sql.addCriteria("con.pessoa.id", "=", criteria.getLong("pessoa.idPessoa") );
    	
    	return sql;
    }
    
    public List findNomeContatoByEmail(String emailContato){
    	SqlTemplate sql = this.createHQLContatoByEmail(emailContato);
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new map (" )
    		.append("con.nmContato as nmContato ) ");
    	sql.addProjection(projecao.toString());
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    public SqlTemplate createHQLContatoByEmail(String emailContato){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom( VolContatos.class.getName() + " as con " +
    			"INNER JOIN con.pessoa as pes " );		
    	sql.addCriteria("lower(con.dsEmail)", "=", emailContato );
	 
    	return sql;
    }

}
