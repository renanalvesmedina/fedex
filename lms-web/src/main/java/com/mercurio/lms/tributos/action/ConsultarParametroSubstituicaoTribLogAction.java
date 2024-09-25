package com.mercurio.lms.tributos.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.tributos.model.service.ParametroSubstituicaoTribLogService;

/**
 * @spring.bean id="lms.tributos.consultarParametroSubstituicaoTribLogAction"
 */
public class ConsultarParametroSubstituicaoTribLogAction extends CrudAction {

	public void setService(ParametroSubstituicaoTribLogService service) {

		this.defaultService = service;
	}
}