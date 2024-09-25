package com.mercurio.lms.tributos.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.tributos.model.service.AliquotaInssPessoaFisicaLogService;

/**
 * @spring.bean id="lms.tributos.consultarAliquotaInssPessoaFisicaLogAction"
 */
public class ConsultarAliquotaInssPessoaFisicaLogAction extends CrudAction {

	public void setService(AliquotaInssPessoaFisicaLogService service) {

		this.defaultService = service;
	}
}