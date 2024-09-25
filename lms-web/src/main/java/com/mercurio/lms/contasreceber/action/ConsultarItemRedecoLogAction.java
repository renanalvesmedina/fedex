package com.mercurio.lms.contasreceber.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.contasreceber.model.service.ItemRedecoLogService;

/**
 * @spring.bean id="lms.contasreceber.consultarItemRedecoLogAction"
 */
public class ConsultarItemRedecoLogAction extends CrudAction {

	public void setService(ItemRedecoLogService service) {

		this.defaultService = service;
	}
}