package com.mercurio.lms.expedicao.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.MotivoCancelamento;
import com.mercurio.lms.expedicao.model.service.MotivoCancelamentoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.manterMotivosCancelamentoConhecimentoAction"
 */

public class ManterMotivosCancelamentoConhecimentoAction extends CrudAction {
	public void setMotivoCancelamento(MotivoCancelamentoService motivoCancelamentoService) {
		this.defaultService = motivoCancelamentoService;
	}
    public void removeById(java.lang.Long id) {
        ((MotivoCancelamentoService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((MotivoCancelamentoService)defaultService).removeByIds(ids);
    }

    public MotivoCancelamento findById(java.lang.Long id) {
    	return ((MotivoCancelamentoService)defaultService).findById(id);
    }

}
