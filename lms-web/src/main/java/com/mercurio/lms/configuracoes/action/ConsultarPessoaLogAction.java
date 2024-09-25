package com.mercurio.lms.configuracoes.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.PessoaLogService;

/**
 * @spring.bean id="lms.configuracoes.consultarPessoaLogAction"
 */
public class ConsultarPessoaLogAction extends CrudAction {

	public void setService(PessoaLogService service) {

		this.defaultService = service;
	}
}