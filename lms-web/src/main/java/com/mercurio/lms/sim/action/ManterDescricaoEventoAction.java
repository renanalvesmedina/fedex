package com.mercurio.lms.sim.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.sim.model.DescricaoEvento;
import com.mercurio.lms.sim.model.service.DescricaoEventoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sim.manterDescricaoEventoAction"
 */

public class ManterDescricaoEventoAction extends CrudAction {
	public void setService(DescricaoEventoService descricaoEventoService) {
		this.defaultService = descricaoEventoService;
	}
    public void removeById(java.lang.Long id) {
        ((DescricaoEventoService)defaultService).removeById(id);
    }
    
    public Serializable store(DescricaoEvento bean) {
        return ((DescricaoEventoService)defaultService).store(bean);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((DescricaoEventoService)defaultService).removeByIds(ids);
    }

    public DescricaoEvento findById(java.lang.Long id) {
    	return ((DescricaoEventoService)defaultService).findById(id);
    }
}
