package com.mercurio.lms.contasreceber.action;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.contasreceber.model.service.FaturamentoAutomaticoService;

/**
 * @spring.bean id="lms.contasreceber.faturamentoAutomaticoAction"
 */

@Assynchronous
public class FaturamentoAutomaticoAction extends CrudAction {
	
	private FaturamentoAutomaticoService faturamentoAutomaticoService;

	@AssynchronousMethod( name = "contasreceber.FaturamentoAutomatico",
			type = BatchType.BATCH_SERVICE,
							feedback = BatchFeedbackType.ON_ERROR)
	public void executeFaturamentoAutomatico() {
		faturamentoAutomaticoService.executeFaturamentoAutomatico();
	}
	
	public void setFaturamentoAutomaticoService(
			FaturamentoAutomaticoService faturamentoAutomaticoService) {
		this.faturamentoAutomaticoService = faturamentoAutomaticoService;
	}
	
	
}