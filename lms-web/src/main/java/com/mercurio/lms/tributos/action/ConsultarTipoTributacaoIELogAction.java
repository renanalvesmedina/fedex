package com.mercurio.lms.tributos.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.tributos.model.service.TipoTributacaoIELogService;

/**
 * @spring.bean id="lms.tributos.consultarTipoTributacaoIELogAction"
 */
public class ConsultarTipoTributacaoIELogAction extends CrudAction {

	public void setService(TipoTributacaoIELogService service) {

		this.defaultService = service;
	}
}