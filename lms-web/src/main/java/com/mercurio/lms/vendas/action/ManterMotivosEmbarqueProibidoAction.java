package com.mercurio.lms.vendas.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.vendas.model.MotivoProibidoEmbarque;
import com.mercurio.lms.vendas.model.service.MotivoProibidoEmbarqueService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterMotivosEmbarqueProibidoAction"
 */

public class ManterMotivosEmbarqueProibidoAction extends CrudAction {
	public void setMotivoProibidoEmbarque(MotivoProibidoEmbarqueService motivoProibidoEmbarqueService) {
		this.defaultService = motivoProibidoEmbarqueService;
	}
    public void removeById(java.lang.Long id) {
        ((MotivoProibidoEmbarqueService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((MotivoProibidoEmbarqueService)defaultService).removeByIds(ids);
    }

    public MotivoProibidoEmbarque findById(java.lang.Long id) {
    	return ((MotivoProibidoEmbarqueService)defaultService).findById(id);
    }

}
