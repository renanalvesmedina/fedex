package com.mercurio.lms.municipios.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.MotivoParada;
import com.mercurio.lms.municipios.model.service.MotivoParadaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterMotivosParadaAction"
 */

public class ManterMotivosParadaAction extends CrudAction {
	public void setMotivoParada(MotivoParadaService motivoParadaService) {
		this.defaultService = motivoParadaService;
	}
    public void removeById(java.lang.Long id) {
        ((MotivoParadaService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((MotivoParadaService)defaultService).removeByIds(ids);
    }

    public MotivoParada findById(java.lang.Long id) {
    	return ((MotivoParadaService)defaultService).findById(id);
    }
    
    public Serializable store(MotivoParada bean) {
    	return ((MotivoParadaService)defaultService).store(bean);
    }

}
