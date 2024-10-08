package com.mercurio.lms.municipios.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.PostoPassagem;
import com.mercurio.lms.municipios.model.service.PostoPassagemService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterPostosPassagemAction"
 */

public class ManterPostosPassagemAction extends CrudAction {
	public void setPostoPassagem(PostoPassagemService postoPassagemService) {
		this.defaultService = postoPassagemService;
	}
    public void removeById(java.lang.Long id) {
        ((PostoPassagemService)defaultService).removeById(id);
    }

    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((PostoPassagemService)defaultService).removeByIds(ids);
    }

    public PostoPassagem findById(java.lang.Long id) {
    	return ((PostoPassagemService)defaultService).findById(id);
    }
	

}
