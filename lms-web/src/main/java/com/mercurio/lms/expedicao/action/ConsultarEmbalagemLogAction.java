package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.EmbalagemLogService;

/**
 * @spring.bean id="lms.expedicao.consultarEmbalagemLogAction"
 */
public class ConsultarEmbalagemLogAction extends CrudAction {

	public void setService(EmbalagemLogService service) {

		this.defaultService = service;
	}
}