package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.InformacaoDocServicoLogService;

/**
 * @spring.bean id="lms.expedicao.consultarInformacaoDocServicoLogAction"
 */
public class ConsultarInformacaoDocServicoLogAction extends CrudAction {

	public void setService(InformacaoDocServicoLogService service) {

		this.defaultService = service;
	}
}