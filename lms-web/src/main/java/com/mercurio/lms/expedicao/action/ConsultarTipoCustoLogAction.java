package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.TipoCustoLogService;

/**
 * @spring.bean id="lms.expedicao.consultarTipoCustoLogAction"
 */
public class ConsultarTipoCustoLogAction extends CrudAction {

	public void setService(TipoCustoLogService service) {

		this.defaultService = service;
	}
}