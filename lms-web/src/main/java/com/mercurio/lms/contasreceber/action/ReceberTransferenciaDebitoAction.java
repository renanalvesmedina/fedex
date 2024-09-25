package com.mercurio.lms.contasreceber.action;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.contasreceber.model.service.ReceberTransferenciaDebitoService;

/**
 * @spring.bean id="lms.contasreceber.receberTransferenciaDebitoAction"
 */

@Assynchronous
public class ReceberTransferenciaDebitoAction extends CrudAction {
	
	private ReceberTransferenciaDebitoService receberTransferenciaDebitoService;

	@AssynchronousMethod( name="contasreceber.ReceberTransferenciaDebito",
							type = BatchType.BATCH_SERVICE,
							feedback = BatchFeedbackType.ON_ERROR)
	public void executeReceberTransferenciaDebito() {
		receberTransferenciaDebitoService.storeReceberAutomaticamente();
	}
	
	public void setReceberTransferenciaDebitoService(
			ReceberTransferenciaDebitoService receberTransferenciaDebitoService) {
		this.receberTransferenciaDebitoService = receberTransferenciaDebitoService;
	}
	
	
}