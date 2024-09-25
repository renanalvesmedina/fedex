package com.mercurio.lms.configuracoes.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.ObservacaoICMSPessoaLogService;

/**
 * @spring.bean id="lms.configuracoes.consultarObservacaoICMSPessoaLogAction"
 */
public class ConsultarObservacaoICMSPessoaLogAction extends CrudAction {

	public void setService(ObservacaoICMSPessoaLogService service) {

		this.defaultService = service;
	}
}