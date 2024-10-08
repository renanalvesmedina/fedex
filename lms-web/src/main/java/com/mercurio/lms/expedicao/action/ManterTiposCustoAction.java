package com.mercurio.lms.expedicao.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.TipoCusto;
import com.mercurio.lms.expedicao.model.service.TipoCustoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.manterTiposCustoAction"
 */

public class ManterTiposCustoAction extends CrudAction {
	public void setTipoCusto(TipoCustoService tipoCustoService) {
		this.defaultService = tipoCustoService;
	}
    public void removeById(java.lang.Long id) {
        ((TipoCustoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((TipoCustoService)defaultService).removeByIds(ids);
    }

    public TipoCusto findById(java.lang.Long id) {
    	return ((TipoCustoService)defaultService).findById(id);
    }

}
