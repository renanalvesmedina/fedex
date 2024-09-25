package com.mercurio.lms.tributos.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.tributos.model.service.AliquotaIssMunicipioServLogService;

/**
 * @spring.bean id="lms.tributos.consultarAliquotaIssMunicipioServLogAction"
 */
public class ConsultarAliquotaIssMunicipioServLogAction extends CrudAction {

	public void setService(AliquotaIssMunicipioServLogService service) {

		this.defaultService = service;
	}
}