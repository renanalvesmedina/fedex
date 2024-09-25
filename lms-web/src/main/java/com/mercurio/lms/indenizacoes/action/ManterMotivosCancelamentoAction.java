package com.mercurio.lms.indenizacoes.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.MotivoCancelamentoService;
import com.mercurio.lms.indenizacoes.model.MotivoCancelamentoRim;
import com.mercurio.lms.indenizacoes.model.service.MotivoCancelamentoRimService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.indenizacoes.manterMotivosCancelamentoAction"
 */

public class ManterMotivosCancelamentoAction extends CrudAction {
	
	MotivoCancelamentoService motivoCancelamentoService;

	public void setMotivoCancelamentoService(MotivoCancelamentoService motivoCancelamentoService) {
		this.motivoCancelamentoService = motivoCancelamentoService;
	}

	public void setMotivoCancelamentoRim(MotivoCancelamentoRimService motivoCancelamentoRimService) {
		this.defaultService = motivoCancelamentoRimService;
	}

	public void removeById(java.lang.Long id) {
        ((MotivoCancelamentoRimService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((MotivoCancelamentoRimService)defaultService).removeByIds(ids);
    }

    public MotivoCancelamentoRim findById(java.lang.Long id) {
    	return ((MotivoCancelamentoRimService)defaultService).findById(id);
    }

    public List findAllAtivo() {
		return motivoCancelamentoService.findAtivosLookup();
	}
}
