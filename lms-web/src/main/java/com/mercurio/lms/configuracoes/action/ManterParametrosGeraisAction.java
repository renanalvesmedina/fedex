package com.mercurio.lms.configuracoes.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.manterParametrosGeraisAction"
 */

public class ManterParametrosGeraisAction extends CrudAction {
	public void setParametroGeral(ParametroGeralService parametroGeralService) {
		this.defaultService = parametroGeralService;
	}
    public void removeById(java.lang.Long id) {
        ((ParametroGeralService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((ParametroGeralService)defaultService).removeByIds(ids);
    }

    public ParametroGeral findById(java.lang.Long id) {
    	return ((ParametroGeralService)defaultService).findById(id);
    }

}
