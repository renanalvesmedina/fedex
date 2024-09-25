package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.batch.AdsmBatchController;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.GrupoJob;
import com.mercurio.lms.configuracoes.model.dao.GrupoJobDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.grupoJobService"
 */
public class GrupoJobService extends CrudService<GrupoJob, Long> {

	private AdsmBatchController controller;
	
	public void setController(AdsmBatchController controller) {
		this.controller = controller;
	}

	/**
	 * Recupera uma instância de <code>GrupoJob</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public GrupoJob findById(java.lang.Long id) {
        return (GrupoJob) super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	
    	if (!this.validateIdGrupo(id.toString())) {
    		throw new BusinessException("LMS-27080");
    	}
    	
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id. Remove apenas os ids
	 * que não estão associados a nenhum processo.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	
    	//TODO: Fazer a validacao para delecao pela grid....
    	List removeIds = new ArrayList();
    	boolean todosRemovidos = true;
    	
    	for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			if (validateIdGrupo(id.toString())) {
				removeIds.add(id);
			} else {
				todosRemovidos = false;
			}
		}
    	
    	//Remove apenas os ids que não estão associados a nenhum processo.... 
		super.removeByIds(removeIds);
			 
		//Caso existam processo que nao foram removidos...
		if (!todosRemovidos) throw new BusinessException("LMS-27081");
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(GrupoJob bean) {
    	
    	
    	
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setGrupoJobDAO(GrupoJobDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private GrupoJobDAO getGrupoJobDAO() {
        return (GrupoJobDAO) getDao();
    }
    
    /**
     * Valida o nome do grupo a ser criado. 
     * 
     * @param idGrupoJob
     * @return
     */
    private boolean validateIdGrupo(String idGrupoJob) {

    	String[] groupNames;
    	
		groupNames = controller.getJobGroupNames();
		
		for (int i = 0; i < groupNames.length; i++) {
			String groupName = groupNames[i];
			if (groupName.equals(idGrupoJob)) {
				return false;
			}
		}	
    	
    	return true;
    }
    
    /**
     * Realiza a pesquisa tendo como parametro o nome do <code>Grupo</code>
     * 
     * @param nmGrupo
     * @return
     */
    public List findByNomeGrupo(String nmGrupo) {
    	return this.getGrupoJobDAO().findByNomeGrupo(nmGrupo);
    }
    
}