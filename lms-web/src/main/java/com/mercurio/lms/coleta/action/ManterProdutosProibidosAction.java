package com.mercurio.lms.coleta.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.coleta.model.ProdutoProibido;
import com.mercurio.lms.coleta.model.service.ProdutoProibidoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.manterProdutosProibidosAction"
 */

public class ManterProdutosProibidosAction extends CrudAction {
	public void setProdutoProibido(ProdutoProibidoService produtoProibidoService) {
		this.defaultService = produtoProibidoService;
	}
    public void removeById(java.lang.Long id) {
        ((ProdutoProibidoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((ProdutoProibidoService)defaultService).removeByIds(ids);
    }

    public ProdutoProibido findById(java.lang.Long id) {
    	return ((ProdutoProibidoService)defaultService).findById(id);
    }

}
