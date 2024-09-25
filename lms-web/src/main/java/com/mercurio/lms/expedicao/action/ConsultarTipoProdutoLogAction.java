package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.TipoProdutoLogService;

/**
 * @spring.bean id="lms.expedicao.consultarTipoProdutoLogAction"
 */
public class ConsultarTipoProdutoLogAction extends CrudAction {

	public void setService(TipoProdutoLogService service) {

		this.defaultService = service;
	}
}