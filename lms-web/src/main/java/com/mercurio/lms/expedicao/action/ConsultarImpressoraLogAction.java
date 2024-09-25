package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.ImpressoraLogService;

/**
 * @spring.bean id="lms.expedicao.consultarImpressoraLogAction"
 */
public class ConsultarImpressoraLogAction extends CrudAction {

	public void setService(ImpressoraLogService service) {

		this.defaultService = service;
	}
}