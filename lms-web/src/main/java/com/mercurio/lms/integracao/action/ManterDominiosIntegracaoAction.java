package com.mercurio.lms.integracao.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;

import com.mercurio.lms.integracao.model.DominioNomeIntegracao;
import com.mercurio.lms.integracao.model.service.DominioNomeIntegracaoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * 
 * @spring.bean id="lms.integracao.manterDominiosIntegracaoAction"
 */
public class ManterDominiosIntegracaoAction extends CrudAction {

	public Serializable store(DominioNomeIntegracao bean){
		return getService().store(bean);
	}
	
	public void removeById(java.lang.Long id) {
        getService().removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getService().removeByIds(ids);
    }

    public DominioNomeIntegracao findById(java.lang.Long id) {
    	return getService().findById(id);
    }
	
    
	///// GETTERS E SETTERS /////
	public void setService(DominioNomeIntegracaoService dominioNomeIntegracaoService) {
		this.defaultService = dominioNomeIntegracaoService;
	}
	
	private DominioNomeIntegracaoService getService() {
		return ((DominioNomeIntegracaoService)defaultService);
	}
}
