package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoLogService;

/**
 * @spring.bean id="lms.expedicao.consultarNaturezaProdutoLogAction"
 */
public class ConsultarNaturezaProdutoLogAction extends CrudAction {

	public void setService(NaturezaProdutoLogService service) {

		this.defaultService = service;
	}
}