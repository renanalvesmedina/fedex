package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.AnexoDoctoServicoLogService;

/**
 * @spring.bean id="lms.expedicao.consultarAnexoDoctoServicoLogAction"
 */
public class ConsultarAnexoDoctoServicoLogAction extends CrudAction {

	public void setService(AnexoDoctoServicoLogService service) {

		this.defaultService = service;
	}
}