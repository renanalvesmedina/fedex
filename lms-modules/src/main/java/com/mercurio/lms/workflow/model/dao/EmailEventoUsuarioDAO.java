package com.mercurio.lms.workflow.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.workflow.model.EmailEventoUsuario;


/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */

public class EmailEventoUsuarioDAO extends BaseCrudDao<EmailEventoUsuario, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */

	protected final Class getPersistentClass() {
		return EmailEventoUsuario.class;
	}
	
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {

		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("email");
		sql.addFrom(
				new StringBuffer()
					.append(getPersistentClass().getName()).append(" email ")
					.append(" join fetch email.eventoWorkflow ew ")
					.append(" join fetch ew.tipoEvento te ")
					.append(" join fetch email.usuario usu ")
					.toString()
				);
			
		/** Filtro de Matrícula  */
        if (StringUtils.isNotBlank((String)(criteria.get("chapaUsuario")))) {
        	sql.addCriteria("usu.nrMatricula","like",(String)criteria.get("chapaUsuario"));        	
        }

		/** Filtro do Número do Evento  */        
        if (StringUtils.isNotBlank((String)(criteria.get("eventoNr")))) {
        	sql.addCriteria("te.nrTipoEvento", "like", (String)criteria.get("eventoNr"));       			        			           
        }
        
        if (StringUtils.isNotBlank((String)(criteria.get("tpSituacao")))) {
        	sql.addCriteria("email.tpSituacao", "like", (String)criteria.get("tpSituacao"));
        }
        
        sql.addOrderBy("te.nrTipoEvento");
        sql.addOrderBy("usu.nrMatricula");
        	
        return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());                
	}
	
	/**
	 * Retorna a lista de EmailEventoUsuario por Evento do Workflow
	 * 
	 * @param Long idEvento
	 * @param Long idFilial
	 * @return List
	 * */
	public List findByEvento(Long idEvento, Long idFilial){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("e");
		sql.addFrom(EmailEventoUsuario.class.getName()+ " e " +
				"join e.filialEmailEventoUsuario as fi " +
				"join fetch e.usuario as us  ");
		sql.addCriteria("e.eventoWorkflow.id","=",idEvento);
		sql.addCriteria("fi.filial.id","=",idFilial);
		return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}
	
	public ResultSetPage findPaginatedEventoUsuario(TypedFlatMap criteria, FindDefinition findDef) {
		String idUsuario = (String) criteria.get("usuario.idUsuario");
		String idEventoWorkflow = (String) criteria.get("eventoWorkflow.idEventoWorkflow");
		String tpSituacao = (String) criteria.get("tpSituacao");
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(
				new StringBuffer()
					.append(getPersistentClass().getName()).append(" email ")
					.append(" join fetch email.eventoWorkflow ew ")
					.append(" join fetch ew.tipoEvento te ")
					.append(" join fetch email.usuario usu ")
					.toString()
				);
			
        if (StringUtils.isNotBlank(idEventoWorkflow)) {
        	sql.addCriteria("ew.idEventoWorkflow", "=", idEventoWorkflow, Long.class);       			        			           
        }
        
        if (StringUtils.isNotBlank(idUsuario)) {
        	sql.addCriteria("usu.idUsuario", "=", idUsuario, Long.class);       			        			           
        }
        
        if (StringUtils.isNotBlank(tpSituacao)) {
        	sql.addCriteria("email.tpSituacao", "=", tpSituacao);
        }
        
        sql.addOrderBy("te.nrTipoEvento");
        sql.addOrderBy("usu.nmUsuario");
        	
        return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());		
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		String idUsuario = (String) criteria.get("usuario.idUsuario");
		String idEventoWorkflow = (String) criteria.get("eventoWorkflow.idEventoWorkflow");
		String tpSituacao = (String) criteria.get("tpSituacao");
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection(" count(email.idEmailEventoUsuario) ");
		sql.addFrom(
				new StringBuffer()
					.append(getPersistentClass().getName()).append(" email ")
					.append(" inner join email.eventoWorkflow ew ")
					.append(" inner join ew.tipoEvento te ")
					.append(" join email.usuario usu ")
					.toString()
				);
			
        if (StringUtils.isNotBlank(idEventoWorkflow)) {
        	sql.addCriteria("ew.idEventoWorkflow", "=", idEventoWorkflow, Long.class);       			        			           
        }
        
        if (StringUtils.isNotBlank(idUsuario)) {
        	sql.addCriteria("usu.idUsuario", "=", idUsuario, Long.class);       			        			           
        }
        
        if (StringUtils.isNotBlank(tpSituacao)) {
        	sql.addCriteria("email.tpSituacao", "=", tpSituacao);
        }
        
        sql.addOrderBy("te.nrTipoEvento");
        	
        Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
        return result.intValue();
	}
	
	public void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuario",FetchMode.JOIN);
		lazyFindById.put("eventoWorkflow",FetchMode.JOIN);		
		lazyFindById.put("eventoWorkflow.tipoEvento",FetchMode.JOIN);			

		super.initFindByIdLazyProperties(lazyFindById);
	}
		
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("usuario", FetchMode.JOIN);    	   	
		lazyFindPaginated.put("eventoWorkflow",FetchMode.JOIN);		
		lazyFindPaginated.put("eventoWorkflow.tipoEvento",FetchMode.JOIN);	
		
		super.initFindPaginatedLazyProperties(lazyFindPaginated);		
    }

    public void removeByFilial(Serializable id) {						
		if(id != null){    		 
    		StringBuilder query = new StringBuilder()
			.append("delete com.mercurio.lms.workflow.model.FilialEmailEventoUsuario ")
			.append("where emailEventoUsuario.idEmailEventoUsuario = :id ");

    		this.getAdsmHibernateTemplate().removeById(query.toString(), id);			
    	}		
    }
}
