package com.mercurio.lms.workflow.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.workflow.model.Substituto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SubstitutoDAO extends BaseCrudDao<Substituto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Substituto.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
        lazyFindById.put("usuarioByIdUsuarioSubstituido",FetchMode.JOIN);
        lazyFindById.put("usuarioByIdUsuarioSubstituto",FetchMode.JOIN);
        lazyFindById.put("perfilSubstituido",FetchMode.JOIN); 
    }
    
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
        lazyFindPaginated.put("usuarioByIdUsuarioSubstituido",FetchMode.JOIN);
        lazyFindPaginated.put("usuarioByIdUsuarioSubstituto",FetchMode.JOIN);
        lazyFindPaginated.put("perfilSubstituido",FetchMode.JOIN);
    }
    
    
    
    
    
    public List findSubstitutoByUsuarioSubstituto(){
    	
    	return null;
    }
    
    /**
     * Pesquisa para saber se existe um outro registro de substituto para o mesmo
     * perfil substituído ou usuário substituído que esteja no mesmo período de substituição
     * @param substituto Substituto a ser inserido
     * @return Lista de registro conflitantes
     */
	public List findSubstitutos(Substituto substituto) {
        
        SqlTemplate sqlTemplate = new SqlTemplate();
        
        /** Resgata do substituto que vem por parametro, o usuario, usuarioSubstituto e o perfil */
        Usuario usuario = substituto.getUsuarioByIdUsuarioSubstituido();
        Perfil perfil = substituto.getPerfilSubstituido();
        Usuario usuarioSubstituto = substituto.getUsuarioByIdUsuarioSubstituto();
        
        StringBuffer joins = new StringBuffer()
            .append(" inner join fetch s.usuarioByIdUsuarioSubstituto ust " )
            .append(" left  join fetch s.usuarioByIdUsuarioSubstituido us " )
            .append(" left  join fetch s.perfilSubstituido ps ");
        
        sqlTemplate.addCriteria("ust.id", "=", usuarioSubstituto.getIdUsuario());
        
        sqlTemplate.addCriteria("s.id", "!=", substituto.getIdSubstituto());
        
        sqlTemplate.addFrom( getPersistentClass().getName() + " s " + joins.toString() );
        
        if( perfil != null ){
            sqlTemplate.addCriteria( "ps.id","=",substituto.getPerfilSubstituido().getIdPerfil());
        } else if( usuario != null ){
            sqlTemplate.addCriteria("us.id","=",substituto.getUsuarioByIdUsuarioSubstituido().getIdUsuario());
        }
        
        /** Compara o período passado com os períodos da base*/
        sqlTemplate.addCustomCriteria(" ( (s.dtSubstituicaoFinal >= ? and s.dtSubstituicaoInicial <= ?) or" +
        		" (s.dtSubstituicaoFinal >= ? and s.dtSubstituicaoInicial <= ?) or" +
        		" (s.dtSubstituicaoFinal < ? and s.dtSubstituicaoInicial > ?))");
        
        sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoInicial());
        sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoInicial());
        sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoFinal());
        sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoFinal());
        sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoFinal());
        sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoInicial());
        
        return this.getAdsmHibernateTemplate().find(sqlTemplate.getSql(), sqlTemplate.getCriteria());

	} 
    
    /**
      * Método sobrescrito para buscar Substitutos onde os campos usuarioByIdUsuarioSubstituido e
      * perfilSubstituido podem ser nulos
      * @param criteria Critérios de pesquisa
      * @param findDef Definições de paginação
      * @return ResultSetPage Resultado da pesquisa
      */
     public ResultSetPage findPaginated(TypedFlatMap criteria) {
         
    	 /** Define os parametros para paginação */
     	 FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
     	
         SqlTemplate sql = mountHql(criteria);
         
         sql.addProjection("s");
         
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
	 * 
	 * @param TypedFlatMap criteria
	 * @return SqlTemplate
	 */
	private SqlTemplate mountHql(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
         
         
         
         StringBuffer joins = new StringBuffer()
             .append(" inner join fetch s.usuarioByIdUsuarioSubstituto ust " )
             .append(" left  join fetch s.usuarioByIdUsuarioSubstituido us " )
             .append(" left  join fetch s.perfilSubstituido ps ");
             
         sql.addFrom( getPersistentClass().getName() + " s " + joins.toString() );

         
//         sql.addCriteria("ps.idPerfil","=", criteria.getLong("perfil.idPerfil"));
         sql.addCriteria("ps.idPerfil","=", criteria.getLong("perfilSubstituido.idPerfil"));
         
         sql.addCriteria("us.idUsuario","=", criteria.getLong("usuarioByIdUsuarioSubstituido.idUsuario"));
         sql.addCriteria("ust.idUsuario","=", criteria.getLong("usuarioByIdUsuarioSubstituto.idUsuario"));
         
         if(criteria.getYearMonthDay("dtSubstituicao") != null && criteria.getYearMonthDay("dtSubstituicao").toString() != ""){
	         sql.addCustomCriteria(" (s.dtSubstituicaoInicial  <= ? and s.dtSubstituicaoFinal >= ?) ");
	         sql.addCriteriaValue(criteria.getYearMonthDay("dtSubstituicao"));
	         sql.addCriteriaValue(criteria.getYearMonthDay("dtSubstituicao"));
         }
          
         sql.addOrderBy("ust.nmUsuario");
         sql.addOrderBy("s.dtSubstituicaoInicial");
		return sql;
	}	
	
	/**
	 * LMS-7781
	 * Valida se o usuário substituído está substituindo outro usuário no período idicado
	 * @param substituto
	 * @return
	 */
	public boolean validateSubstitutoEstaComoSubstitutoDeOutroUsuario(Substituto substituto) {
		SqlTemplate sqlTemplate = new SqlTemplate();

		sqlTemplate.addFrom(getPersistentClass().getName());
		
		sqlTemplate.addCriteria("usuarioByIdUsuarioSubstituto", "=",
				substituto.getUsuarioByIdUsuarioSubstituido());

		sqlTemplate.addCustomCriteria(" ((? between dtSubstituicaoInicial and dtSubstituicaoFinal) or (? between dtSubstituicaoInicial and dtSubstituicaoFinal))");

		sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoInicial());
		sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoFinal());

		List<Substituto> result = getAdsmHibernateTemplate().find(sqlTemplate.getSql(), sqlTemplate.getCriteria());

		return result.size() > 0;
	}

	/**
	 * LMS-7781
	 * Valida se o usuário substituto está ausente no período indicado 
	 * @param substituto
	 * @return
	 */
	public boolean validatePeriodoDeAusenciaSubstituto(Substituto substituto) { 
		SqlTemplate sqlTemplate = new SqlTemplate();

		sqlTemplate.addFrom(getPersistentClass().getName());
		
		sqlTemplate.addCriteria("usuarioByIdUsuarioSubstituido", "=",
				substituto.getUsuarioByIdUsuarioSubstituto());

		sqlTemplate.addCustomCriteria(" ((? between dtSubstituicaoInicial and dtSubstituicaoFinal) or (? between dtSubstituicaoInicial and dtSubstituicaoFinal))");

		sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoInicial());
		sqlTemplate.addCriteriaValue(substituto.getDtSubstituicaoFinal());

		List<Substituto> result = getAdsmHibernateTemplate().find(sqlTemplate.getSql(), sqlTemplate.getCriteria());

		return result.size() > 0;
	}
}