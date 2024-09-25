package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.DensidadeLogService;

/**
 * @spring.bean id="lms.expedicao.consultarDensidadeLogAction"
 */
public class ConsultarDensidadeLogAction extends CrudAction {

	public void setService(DensidadeLogService service) {

		this.defaultService = service;
	}
}