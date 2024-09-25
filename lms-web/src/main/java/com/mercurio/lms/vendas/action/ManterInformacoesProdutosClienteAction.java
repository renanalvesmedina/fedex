package com.mercurio.lms.vendas.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.vendas.model.ProdutoCliente;
import com.mercurio.lms.vendas.model.service.ProdutoClienteService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterInformacoesProdutosClienteAction"
 */

public class ManterInformacoesProdutosClienteAction extends CrudAction {
	public void setProdutoCliente(ProdutoClienteService produtoClienteService) {
		this.defaultService = produtoClienteService;
	}
    public void removeById(java.lang.Long id) {
        ((ProdutoClienteService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((ProdutoClienteService)defaultService).removeByIds(ids);
    }

    public ProdutoCliente findById(java.lang.Long id) {
    	return ((ProdutoClienteService)defaultService).findById(id);
    }

}
