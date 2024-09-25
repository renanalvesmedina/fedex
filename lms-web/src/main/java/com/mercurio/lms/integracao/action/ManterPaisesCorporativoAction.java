package com.mercurio.lms.integracao.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.integracao.model.PaisCorporativo;
import com.mercurio.lms.integracao.model.service.PaisCorporativoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * 
 * @spring.bean id="lms.integracao.manterPaisesCorporativoAction"
 */
public class ManterPaisesCorporativoAction extends CrudAction{
	
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

    public PaisCorporativo findById(java.lang.Long id) {
    	return getService().findById(id);
    }
	
    
	///// GETTERS E SETTERS /////
	public void setService(PaisCorporativoService paisCorporativoService) {
		this.defaultService = paisCorporativoService;
	}
	
	private PaisCorporativoService getService() {
		return ((PaisCorporativoService)defaultService);
	}


}
