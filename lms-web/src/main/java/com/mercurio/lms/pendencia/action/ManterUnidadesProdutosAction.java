package com.mercurio.lms.pendencia.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.pendencia.model.UnidadeProduto;
import com.mercurio.lms.pendencia.model.service.UnidadeProdutoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.pendencia.manterUnidadesProdutosAction"
 */

public class ManterUnidadesProdutosAction extends CrudAction {
	public void setUnidadeProduto(UnidadeProdutoService unidadeProdutoService) {
		this.defaultService = unidadeProdutoService;
	}
    public void removeById(java.lang.Long id) {
        ((UnidadeProdutoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((UnidadeProdutoService)defaultService).removeByIds(ids);
    }

    public UnidadeProduto findById(java.lang.Long id) {
    	return ((UnidadeProdutoService)defaultService).findById(id);
    }

}
