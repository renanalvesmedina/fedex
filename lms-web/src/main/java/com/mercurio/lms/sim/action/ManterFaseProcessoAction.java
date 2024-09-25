package com.mercurio.lms.sim.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.service.FaseProcessoService;

public class ManterFaseProcessoAction extends CrudAction {
	
	public void setFaseProcesso(FaseProcessoService faseProcessoService) {
		this.defaultService = faseProcessoService;
	}
	
	public void removeById(java.lang.Long id) {
        ((FaseProcessoService)defaultService).removeById(id);
    }
    
    public Serializable store(FaseProcesso bean) {
        return ((FaseProcessoService)defaultService).store(bean);
    }

	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((FaseProcessoService)defaultService).removeByIds(ids);
    }

    public FaseProcesso findById(java.lang.Long id) {
    	return ((FaseProcessoService)defaultService).findById(id);
    }

}
