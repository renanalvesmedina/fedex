package com.mercurio.lms.configuracoes.model.dao;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.RegionalUsuario;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegionalUsuarioDAO extends BaseCrudDao<RegionalUsuario, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RegionalUsuario.class;
    }

	public void removeByUsuario(Long id) {		
		getAdsmHibernateTemplate().removeById("delete from " + getPersistentClass().getName() + " as ru where ru.empresaUsuario.id in ( (select e from EmpresaUsuario as e where e.usuario = :id) )", id);				
		getAdsmHibernateTemplate().flush();
	}

	public void removeByIdEmpresaUsuario(Long id) {		
		getAdsmHibernateTemplate().removeById("delete from " + getPersistentClass().getName() + " as ru where ru.empresaUsuario.id in ( (select e from EmpresaUsuario as e where e.idEmpresaUsuario = :id) )", id);
		getAdsmHibernateTemplate().flush();
	}
	
    public void removeByUsuarios(List ids) {
		getAdsmHibernateTemplate().removeByIds("delete from " + getPersistentClass().getName() +  " as ru where ru.empresaUsuario.id in ( (select e from EmpresaUsuario as e where e.usuario = :id) )", ids);
		getAdsmHibernateTemplate().flush();
	}
    
    public List findByIdUsuario(Long id){
		final String hql = "from " + getPersistentClass().getName() + " as p "+
							"join fetch p.empresaUsuario as empresaUsuario "+
							"join fetch empresaUsuario.usuario as user "+
							"join fetch p.regional as regional "+
							"WHERE user.idUsuario = ?";
		
    	return getAdsmHibernateTemplate().find(hql.toString(), id); 
    }
   
    /**
     * Retorna um objeto do tipo RegionalUsuario armazenado com os parametros recebidos.
     * 
     * @param idEmpresaUsuario
     * @param idRegional
     * @return RegionalUsuario
     */
    public RegionalUsuario findByIdRegionalEmpresaUsuario(Long idEmpresaUsuario, Long idRegional){
		final String hql = "from " + getPersistentClass().getName() + " as p "+
							"join fetch p.empresaUsuario as empresaUsuario "+
							"join fetch p.regional as regional "+	
							"WHERE empresaUsuario.idEmpresaUsuario = ? AND regional.idRegional = ? ";
		Object[] restricao = { idEmpresaUsuario, idRegional };
    	return (RegionalUsuario)getAdsmHibernateTemplate().findUniqueResult( hql, restricao ); 
    }
    
    public List findByIdEmpresaUsuario(Long id){
		final String hql = "from " + getPersistentClass().getName() + " as p "+
							"join fetch p.empresaUsuario as empresaUsuario "+
							"join fetch p.regional as regional "+		
							"WHERE empresaUsuario.idEmpresaUsuario = ?";
		
    	return getAdsmHibernateTemplate().find(hql, id); 
    }
    
    public List findByIdEmpresaUsuarioSemFetch(Long id){
		final String hql = "select p.idRegionalUsuario from " + getPersistentClass().getName() + " as p "+
							"join p.empresaUsuario as empresaUsuario "+
							"join p.regional as regional "+
							"WHERE empresaUsuario.idEmpresaUsuario = ?";
		
    	return getAdsmHibernateTemplate().find(hql, id); 
    }
    
    public void storeWorkFlowForAll(List<RegionalUsuario> regionaisUsuarios) {

		if (regionaisUsuarios == null || regionaisUsuarios.size() < 1) return;             
		for (Iterator it = regionaisUsuarios.iterator(); it.hasNext(); ) {
			RegionalUsuario regionaisUsuarioTmp = (RegionalUsuario)it.next();
			RegionalUsuario regionaisUsuarioToUpdate = (RegionalUsuario)getAdsmHibernateTemplate().load(getPersistentClass(), regionaisUsuarioTmp.getIdRegionalUsuario());
			regionaisUsuarioToUpdate.setBlAprovaWorkflow( regionaisUsuarioTmp.getBlAprovaWorkflow() );
			getAdsmHibernateTemplate().update(regionaisUsuarioToUpdate);
		}

    }



}
