package com.mercurio.lms.contasreceber.action;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.contasreceber.model.service.CalcularJurosDiarioService;

/**
 * @spring.bean id="lms.contasreceber.calcularJurosDiarioAutomaticoAction"
 */

@Assynchronous
public class CalcularJurosDiarioAutomaticoAction extends CrudAction {
	
	private CalcularJurosDiarioService calcularJurosDiarioService;

	@AssynchronousMethod( name="contasreceber.CalcularJurosDiario",
			type = BatchType.BATCH_SERVICE,
							feedback = BatchFeedbackType.ON_ERROR)
	public void executeCalcularJurosDiario() {
		calcularJurosDiarioService.executeCalcularJurosDiario();
	}
	
	public void setCalcularJurosDiarioService(
			CalcularJurosDiarioService calcularJurosDiarioService) {
		this.calcularJurosDiarioService = calcularJurosDiarioService;
	}
	
	
}