package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.vendas.model.DivisaoProduto;
import com.mercurio.lms.vendas.model.service.DivisaoProdutoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterProdutosDivisaoAction"
 */

public class ManterProdutosDivisaoAction extends CrudAction {
	private UsuarioService usuarioService;
	
	public void setDivisaoProduto(DivisaoProdutoService divisaoProdutoService) {
		this.defaultService = divisaoProdutoService;
	}
    public void removeById(java.lang.Long id) {
        ((DivisaoProdutoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((DivisaoProdutoService)defaultService).removeByIds(ids);
    }

    public DivisaoProduto findById(java.lang.Long id) {
    	return ((DivisaoProdutoService)defaultService).findById(id);
    }
    
    
    public UsuarioService getUsuarioService(){
    	return this.usuarioService;
    }
    public void setUsuarioService(UsuarioService usuarioService){
    	this.usuarioService = usuarioService;
    }
    
    public Serializable store(DivisaoProduto bean) {
   	 return ((DivisaoProdutoService)defaultService).store(bean);	 
	}
   
    /*
	 * Chama verifica��o de permiss�es do usu�rio sobre uma filial / regional
	 * */
	public Boolean validatePermissao(Long idFilial) {
   	return getUsuarioService().validateAcessoFilialRegionalUsuario(idFilial);
   }

}
