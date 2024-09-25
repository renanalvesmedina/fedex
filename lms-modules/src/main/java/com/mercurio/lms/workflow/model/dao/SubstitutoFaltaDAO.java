package com.mercurio.lms.workflow.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.workflow.model.SubstitutoFalta;
import com.mercurio.lms.workflow.model.SubstitutoFaltaAcao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SubstitutoFaltaDAO extends BaseCrudDao<SubstitutoFalta, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SubstitutoFalta.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
        lazyFindById.put("usuario",FetchMode.JOIN);
        lazyFindById.put("perfil",FetchMode.JOIN); 
    }
    
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
        lazyFindPaginated.put("usuario",FetchMode.JOIN);
        lazyFindPaginated.put("perfil",FetchMode.JOIN);
    }
    
    /**
     * Método sobrescrito para buscar SubstitutosFalta onde os campos usuario e
     * perfil podem ser nulos
     * @param criteria Critérios de pesquisa
     * @param findDef Definições de paginação
     * @return ResultSetPage Resultado da pesquisa
     */
    public ResultSetPage findPaginated(Map criteria) {
        
   	 /** Define os parametros para paginação */
    	 FindDefinition findDef = FindDefinition.createFindDefinition(criteria);    	
    	
        SqlTemplate sql = mountHql(criteria);        
        
        sql.addProjection("sf");
        
        ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), 
                                                                     findDef.getCurrentPage(), 
                                                                     findDef.getPageSize(),
                                                                     sql.getCriteria());
        return rsp;
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	SqlTemplate sql = mountHql(criteria);
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }    

	/**
	 * @author Mickaël Jalbert
	 * @since 23/09/2006
	 * @param Map criteria
	 * @return SqlTemplate
	 */
	private SqlTemplate mountHql(Map criteria) {
		SqlTemplate sql = new SqlTemplate();
        
        StringBuffer joins = new StringBuffer()
            .append(" inner join fetch sf.integrante i " )
            .append(" left  join fetch sf.usuario ust " )
            .append(" left  join fetch sf.perfil pst ");
            
        sql.addFrom( getPersistentClass().getName() + " sf " + joins.toString() );
        
        TypedFlatMap tfm = (TypedFlatMap)criteria;
        Long idPerfil = tfm.getLong("perfil.idPerfil");
        Long idUsuario = tfm.getLong("usuario.idUsuario");
        
        sql.addCriteria("pst.idPerfil", "=", idPerfil);
        sql.addCriteria("ust.idUsuario", "=", idUsuario);
        
        Long idComite = tfm.getLong("integrante.comite.idComite");
        Long idIntegrante = tfm.getLong("integrante.idIntegrante");
        sql.addCriteria("i.idIntegrante","=", idIntegrante);
        sql.addCriteria("sf.tpSituacao","=", criteria.get("tpSituacao") );
        sql.addCriteria("i.comite.idComite","=", idComite);
        
        sql.addOrderBy("ust.nmUsuario");
        sql.addOrderBy("sf.perfil.dsPerfil");
		return sql;
	}
    
    public List findSubstitutoFaltaByIntegrante(Long idIntegrante, Long idAcao){
        SqlTemplate sql = new SqlTemplate();
        SqlTemplate sqlExist = new SqlTemplate();
        
        sqlExist.addProjection(" sfa ");
        
        sqlExist.addFrom( SubstitutoFaltaAcao.class.getName()," sfa ");
        
        sqlExist.addJoin("sf.idSubstitutoFalta","sfa.substitutoFalta.id");
        
        sqlExist.addCriteria("sfa.acao.id","=", idAcao);        
        
        
        sql.addProjection(" sf ");

        sql.addFrom( getPersistentClass().getName() + " sf inner join fetch sf.integrante i " +
        		"left outer join fetch sf.usuario " +
				"left outer join fetch sf.perfil ");       
        //sqlExist
        sql.addCustomCriteria(" not exists("+sqlExist.getSql()+")");
        sql.addCriteriaValue(sqlExist.getCriteria()[0]);
        sql.addCriteria("i.idIntegrante","=", idIntegrante);

        return getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());   	
    }
    
    
    /**
     * Método responsável por buscar todos SubstituoFalta relacionados com o integraante em questão
     * @param idIntegrante
     * @return List list 
     */
    public List findSubstitutoFaltaByIntegrante(Long idIntegrante){
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection(
    			new StringBuffer()
    				.append("sf")
    				.toString()
    		);
    	
    	sql.addFrom( getPersistentClass().getName() + " sf inner join fetch sf.integrante i " +
        		"left outer join fetch sf.usuario " +
				"left outer join fetch sf.perfil ");      
    	
    	sql.addCriteria("i.idIntegrante", "=", idIntegrante);
    	
    	sql.addCriteria("sf.tpSituacao", "=", "A");
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
}