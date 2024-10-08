package com.mercurio.lms.seguros.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.seguros.model.TipoSinistro;
import com.mercurio.lms.seguros.model.service.TipoSinistroService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguros.manterTiposSinistroAction"
 */

public class ManterTiposSinistroAction extends CrudAction {
	public void setTipoSinistro(TipoSinistroService tipoSinistroService) {
		this.defaultService = tipoSinistroService;
	}
    public void removeById(java.lang.Long id) {
        ((TipoSinistroService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((TipoSinistroService)defaultService).removeByIds(ids);
    }

    public TipoSinistro findById(java.lang.Long id) {
    	return ((TipoSinistroService)defaultService).findById(id);
    }

}
