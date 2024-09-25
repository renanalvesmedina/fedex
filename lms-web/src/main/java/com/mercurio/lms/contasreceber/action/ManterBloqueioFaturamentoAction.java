package com.mercurio.lms.contasreceber.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.contasreceber.model.service.BloqueioFaturamentoService;
import com.mercurio.lms.contasreceber.model.service.MotivoOcorrenciaService;

/**
 * @spring.bean id="lms.contasreceber.manterBloqueioFaturamentoAction"
 */
public class ManterBloqueioFaturamentoAction extends CrudAction {
	private MotivoOcorrenciaService motivoOcorrenciaService;

	public void setService(BloqueioFaturamentoService service) {

		this.defaultService = service;
	}

    public List findMotivoOcorrenciaBloqueio(Map criteria) {
    	return motivoOcorrenciaService.findMotivoOcorrenciaDeBloqueio();
    }

	public void setMotivoOcorrenciaService(MotivoOcorrenciaService motivoOcorrenciaService) {
		this.motivoOcorrenciaService = motivoOcorrenciaService;
	}

}