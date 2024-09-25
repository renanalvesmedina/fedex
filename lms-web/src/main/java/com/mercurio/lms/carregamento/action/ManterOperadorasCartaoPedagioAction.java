package com.mercurio.lms.carregamento.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio;
import com.mercurio.lms.carregamento.model.service.OperadoraCartaoPedagioService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.manterOperadorasCartaoPedagioAction"
 */

public class ManterOperadorasCartaoPedagioAction extends CrudAction {
	public void setOperadoraCartaoPedagio(OperadoraCartaoPedagioService operadoraCartaoPedagioService) {
		this.defaultService = operadoraCartaoPedagioService;
	}
    public void removeById(java.lang.Long id) {
        ((OperadoraCartaoPedagioService)defaultService).removeOperadoraCartaoPedagioById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((OperadoraCartaoPedagioService)defaultService).removeOperadoraCartaoPedagioByIds(ids);
    }

    public OperadoraCartaoPedagio findById(java.lang.Long id) {
    	return ((OperadoraCartaoPedagioService)defaultService).findById(id);
    }

}
