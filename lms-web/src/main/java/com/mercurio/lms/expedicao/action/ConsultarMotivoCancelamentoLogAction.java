package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.MotivoCancelamentoLogService;

/**
 * @spring.bean id="lms.expedicao.consultarMotivoCancelamentoLogAction"
 */
public class ConsultarMotivoCancelamentoLogAction extends CrudAction {

	public void setService(MotivoCancelamentoLogService service) {

		this.defaultService = service;
	}
}