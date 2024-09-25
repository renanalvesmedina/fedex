package com.mercurio.lms.tributos.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.tributos.model.service.TipoTributacaoIcmsLogService;

/**
 * @spring.bean id="lms.tributos.consultarTipoTributacaoIcmsLogAction"
 */
public class ConsultarTipoTributacaoIcmsLogAction extends CrudAction {

	public void setService(TipoTributacaoIcmsLogService service) {

		this.defaultService = service;
	}
}