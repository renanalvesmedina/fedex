package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.DivisaoClienteNaturezaProduto;
import com.mercurio.lms.vendas.model.service.DivisaoClienteNaturezaProdutoService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterDivisaoClienteNaturezaProdutoAction"
 */
public class ManterDivisaoClienteNaturezaProdutoAction extends CrudAction {
	private UsuarioService usuarioService;

	public DivisaoClienteNaturezaProduto findById(java.lang.Long id) {
    	return getDivisaoClienteNaturezaProdutoService().findById(id);
    }

	public Integer getRowCountNaturezaProduto(TypedFlatMap criteria) {
		return getDivisaoClienteNaturezaProdutoService().getRowCountNaturezaProduto(criteria);
	}
	public ResultSetPage findPaginatedNaturezaProduto(TypedFlatMap criteria) {	
		return getDivisaoClienteNaturezaProdutoService().findPaginatedNaturezaProduto(criteria);
	}

    public Boolean validatePermissao(Long idFilial) {
		return usuarioService.validateAcessoFilialRegionalUsuario(idFilial);
    }

    public Serializable store(DivisaoClienteNaturezaProduto bean) {
      	return getDivisaoClienteNaturezaProdutoService().store(bean);
   	}

    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getDivisaoClienteNaturezaProdutoService().removeByIds(ids);
    }

    public void removeById(java.lang.Long id) {
    	getDivisaoClienteNaturezaProdutoService().removeById(id);
    }

    public void setDivisaoClienteNaturezaProduto(DivisaoClienteNaturezaProdutoService divisaoClienteNaturezaProdutoService) {
		this.defaultService = divisaoClienteNaturezaProdutoService;
	}
    public DivisaoClienteNaturezaProdutoService getDivisaoClienteNaturezaProdutoService() {
    	return ((DivisaoClienteNaturezaProdutoService)defaultService);
    }
    public void setUsuarioService(UsuarioService usuarioService){
    	this.usuarioService = usuarioService;
    }
}
