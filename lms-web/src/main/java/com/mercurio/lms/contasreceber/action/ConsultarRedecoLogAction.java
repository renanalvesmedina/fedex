package com.mercurio.lms.contasreceber.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.contasreceber.model.service.RedecoLogService;

/**
 * @spring.bean id="lms.contasreceber.consultarRedecoLogAction"
 */
public class ConsultarRedecoLogAction extends CrudAction {

	public void setService(RedecoLogService service) {

		this.defaultService = service;
	}
}