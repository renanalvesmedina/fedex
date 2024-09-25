package com.mercurio.lms.vendas.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.vendas.model.service.ObservacaoConhecimentoLogService;

/**
 * @spring.bean id="lms.vendas.consultarObservacaoConhecimentoLogAction"
 */
public class ConsultarObservacaoConhecimentoLogAction extends CrudAction {

	public void setService(ObservacaoConhecimentoLogService service) {

		this.defaultService = service;
	}
}