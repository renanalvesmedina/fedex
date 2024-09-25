package com.mercurio.lms.configuracoes.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.ContaBancariaLogService;

/**
 * @spring.bean id="lms.configuracoes.consultarContaBancariaLogAction"
 */
public class ConsultarContaBancariaLogAction extends CrudAction {

	public void setService(ContaBancariaLogService service) {

		this.defaultService = service;
	}
}