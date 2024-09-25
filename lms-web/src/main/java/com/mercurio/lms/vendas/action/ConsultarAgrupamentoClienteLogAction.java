package com.mercurio.lms.vendas.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.vendas.model.service.AgrupamentoClienteLogService;

/**
 * @spring.bean id="lms.vendas.consultarAgrupamentoClienteLogAction"
 */
public class ConsultarAgrupamentoClienteLogAction extends CrudAction {

	public void setService(AgrupamentoClienteLogService service) {

		this.defaultService = service;
	}
}