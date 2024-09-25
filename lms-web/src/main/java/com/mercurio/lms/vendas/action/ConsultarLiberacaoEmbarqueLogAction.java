package com.mercurio.lms.vendas.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.vendas.model.service.LiberacaoEmbarqueLogService;

/**
 * @spring.bean id="lms.vendas.consultarLiberacaoEmbarqueLogAction"
 */
public class ConsultarLiberacaoEmbarqueLogAction extends CrudAction {

	public void setService(LiberacaoEmbarqueLogService service) {

		this.defaultService = service;
	}
}