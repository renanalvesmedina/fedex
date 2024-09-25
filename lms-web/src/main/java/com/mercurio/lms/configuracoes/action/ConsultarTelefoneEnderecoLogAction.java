package com.mercurio.lms.configuracoes.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoLogService;

/**
 * @spring.bean id="lms.configuracoes.consultarTelefoneEnderecoLogAction"
 */
public class ConsultarTelefoneEnderecoLogAction extends CrudAction {

	public void setService(TelefoneEnderecoLogService service) {

		this.defaultService = service;
	}
}