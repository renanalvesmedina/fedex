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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.grupoJobService"
 */
public class GrupoJobService extends CrudService<GrupoJob, Long> {

	private AdsmBatchController controller;
	
	public void setController(AdsmBatchController controller) {
		this.controller = controller;
	}

	/**
	 * Recupera uma inst�ncia de <code>GrupoJob</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public GrupoJob findById(java.lang.Long id) {
        return (GrupoJob) super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	
    	if (!this.validateIdGrupo(id.toString())) {
    		throw new BusinessException("LMS-27080");
    	}
    	
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id. Remove apenas os ids
	 * que n�o est�o associados a nenhum processo.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
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
    	
    	//Remove apenas os ids que n�o est�o associados a nenhum processo.... 
		super.removeByIds(removeIds);
			 
		//Caso existam processo que nao foram removidos...
		if (!todosRemovidos) throw new BusinessException("LMS-27081");
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(GrupoJob bean) {
    	
    	
    	
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setGrupoJobDAO(GrupoJobDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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