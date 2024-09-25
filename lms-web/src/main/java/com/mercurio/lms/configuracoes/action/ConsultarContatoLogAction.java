package com.mercurio.lms.configuracoes.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.ContatoLogService;

/**
 * @spring.bean id="lms.configuracoes.consultarContatoLogAction"
 */
public class ConsultarContatoLogAction extends CrudAction {

	public void setService(ContatoLogService service) {

		this.defaultService = service;
	}
}