package com.mercurio.lms.configuracoes.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.FilialUsuario;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialUsuarioDAO extends BaseCrudDao<FilialUsuario, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FilialUsuario.class;
    }
    
 	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuario",FetchMode.JOIN);
		lazyFindById.put("filial",FetchMode.JOIN);
	}
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("usuario",FetchMode.JOIN);
		lazyFindPaginated.put("filial",FetchMode.JOIN);
	}
	
	public void removeByUsuario(Long id) {
		getAdsmHibernateTemplate().removeById("delete from " + getPersistentClass().getName() + " as fu where fu.empresaUsuario.id in ( (select e from "+EmpresaUsuario.class.getName()+" as e where e.usuario = :id) )", id);
		getAdsmHibernateTemplate().flush();
	}

	public void removeByIdEmpresaUsuario(Long id) {
		getAdsmHibernateTemplate().removeById("delete from " + getPersistentClass().getName() + " as fu where fu.empresaUsuario.id in ( (select e from "+EmpresaUsuario.class.getName()+" as e where e.idEmpresaUsuario = :id) )", id);
		getAdsmHibernateTemplate().flush();
	}
	
	public void removeByUsuarios(List ids) {
		getAdsmHibernateTemplate().removeByIds("delete from " + getPersistentClass().getName() + " as fu where fu.empresaUsuario.id in ( (select e from "+EmpresaUsuario.class.getName()+" as e where e.usuario = :id) )", ids);
		getAdsmHibernateTemplate().flush();
	}

    public List findByIdUsuario(Long id){
		StringBuilder sb = new StringBuilder(80);
		sb.append("from ").append(getPersistentClass().getName()).append( " as p ");		 
		sb.append("join fetch p.empresaUsuario as empresaUsuario ");		
		sb.append("join fetch empresaUsuario.usuario as user ");		
		sb.append("join fetch p.filial as filial ");
		sb.append("WHERE user.idUsuario = ?");
		
    	return getAdsmHibernateTemplate().find(sb.toString(),id); 
    }
    
    /**
     * Retorna o objeto do tipo FilialUsuario cadastrado correspondente aos parametros recebidos
     * @param idEmpresaUsuario
     * @param idFilial
     * @return FilialUsuario
     */
    public FilialUsuario findByIdFilialEmpresaUsuario(Long idEmpresaUsuario, Long idFilial){
		StringBuilder sb = new StringBuilder(80);
		sb.append("from ").append(getPersistentClass().getName()).append( " as p ");		 
		sb.append("join fetch p.empresaUsuario as empresaUsuario ");		
		sb.append("join fetch p.filial as filial ");
		sb.append("WHERE empresaUsuario.idEmpresaUsuario = ? AND filial.idFilial = ? ");
		
		Object[] restricao = { idEmpresaUsuario, idFilial };
		
    	return (FilialUsuario)getAdsmHibernateTemplate().findUniqueResult( sb.toString(), restricao ); 
    }
    
    public List findByIdEmpresaUsuario(Long id){
		StringBuilder sb = new StringBuilder(80);
		sb.append("from ").append(getPersistentClass().getName()).append( " as p ");		 
		sb.append("join fetch p.empresaUsuario as empresaUsuario ");		
		sb.append("join fetch p.filial as filial ");
		sb.append("WHERE empresaUsuario.idEmpresaUsuario = ?");
		
    	return getAdsmHibernateTemplate().find(sb.toString(),id); 
    }
    
    public List findByIdEmpresaUsuarioSemFetch(Long id){
		StringBuilder sb = new StringBuilder(80);
		sb.append(" select p.idFilialUsuario from ").append(getPersistentClass().getName() ).append(" as p ");		 
		sb.append("join p.empresaUsuario as empresaUsuario ");		
		sb.append("join p.filial as filial ");
		sb.append("WHERE empresaUsuario.idEmpresaUsuario = ?");
		
    	return getAdsmHibernateTemplate().find(sb.toString(),id); 
    }
    
    public void storeWorkFlowForAll(List filiaisUsuarios) {

        if (filiaisUsuarios == null || filiaisUsuarios.size() < 1) return;

        

        for (Iterator it = filiaisUsuarios.iterator(); it.hasNext(); ) {

                      FilialUsuario filialUsuarioTmp = (FilialUsuario)it.next();

                      FilialUsuario filialUsuarioToUpdate = (FilialUsuario)getAdsmHibernateTemplate().load(getPersistentClass(), filialUsuarioTmp.getIdFilialUsuario());

                      

                      filialUsuarioToUpdate.setBlAprovaWorkflow( filialUsuarioTmp.getBlAprovaWorkflow() );

                      getAdsmHibernateTemplate().update(filialUsuarioToUpdate);

        }

}



	
}