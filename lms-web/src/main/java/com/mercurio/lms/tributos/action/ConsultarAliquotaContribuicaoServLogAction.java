package com.mercurio.lms.tributos.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.tributos.model.service.AliquotaContribuicaoServLogService;

/**
 * @spring.bean id="lms.tributos.consultarAliquotaContribuicaoServLogAction"
 */
public class ConsultarAliquotaContribuicaoServLogAction extends CrudAction {

	public void setService(AliquotaContribuicaoServLogService service) {

		this.defaultService = service;
	}
}