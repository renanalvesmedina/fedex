package com.mercurio.lms.pendencia.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.pendencia.model.FaseOcorrencia;
import com.mercurio.lms.pendencia.model.service.FaseOcorrenciaService;

public class ManterFaseOcorrenciaAction extends CrudAction {

	public void setFaseOcorrencia(FaseOcorrenciaService faseOcorrenciaService) {
		this.defaultService = faseOcorrenciaService;
	}
	
	public void removeById(java.lang.Long id) {
        ((FaseOcorrenciaService)defaultService).removeById(id);
    }
    
    public Serializable store(FaseOcorrencia bean) {
        return ((FaseOcorrenciaService)defaultService).store(bean);
    }

	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((FaseOcorrenciaService)defaultService).removeByIds(ids);
    }

    public FaseOcorrencia findById(java.lang.Long id) {
    	return ((FaseOcorrenciaService)defaultService).findById(id);
    }
    
    public FaseOcorrencia findByIdCustom(java.lang.Long id) {
    	return ((FaseOcorrenciaService)defaultService).findById(id);
    }
    
}
