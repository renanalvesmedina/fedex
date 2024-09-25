package com.mercurio.lms.configuracoes.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaLogService;

/**
 * @spring.bean id="lms.configuracoes.consultarEnderecoPessoaLogAction"
 */
public class ConsultarEnderecoPessoaLogAction extends CrudAction {

	public void setService(EnderecoPessoaLogService service) {

		this.defaultService = service;
	}
}