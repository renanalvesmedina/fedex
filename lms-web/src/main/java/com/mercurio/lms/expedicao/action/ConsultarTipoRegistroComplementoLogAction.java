package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.TipoRegistroComplementoLogService;

/**
 * @spring.bean id="lms.expedicao.consultarTipoRegistroComplementoLogAction"
 */
public class ConsultarTipoRegistroComplementoLogAction extends CrudAction {

	public void setService(TipoRegistroComplementoLogService service) {

		this.defaultService = service;
	}
}