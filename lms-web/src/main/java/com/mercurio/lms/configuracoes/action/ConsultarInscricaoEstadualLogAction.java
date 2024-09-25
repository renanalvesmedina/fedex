package com.mercurio.lms.configuracoes.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualLogService;

/**
 * @spring.bean id="lms.configuracoes.consultarInscricaoEstadualLogAction"
 */
public class ConsultarInscricaoEstadualLogAction extends CrudAction {

	public void setService(InscricaoEstadualLogService service) {

		this.defaultService = service;
	}
}