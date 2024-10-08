package com.mercurio.lms.municipios.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.PermissoEmpresaPais;
import com.mercurio.lms.municipios.model.service.PermissoEmpresaPaisService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterPermissosEmpresaPaisesAction"
 */

public class ManterPermissosEmpresaPaisesAction extends CrudAction {
	public void setPermissoEmpresaPais(PermissoEmpresaPaisService permissoEmpresaPaisService) {
		this.defaultService = permissoEmpresaPaisService;
	}
    public void removeById(java.lang.Long id) {
        ((PermissoEmpresaPaisService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((PermissoEmpresaPaisService)defaultService).removeByIds(ids);
    }

    public PermissoEmpresaPais findById(java.lang.Long id) {
    	return ((PermissoEmpresaPaisService)defaultService).findById(id);
    }

}
