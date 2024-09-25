package com.mercurio.lms.contasreceber.action;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.contasreceber.model.service.FaturamentoAutomaticoService;
import com.mercurio.lms.contasreceber.model.service.HistoricoBoletoService;

/**
 * @spring.bean id="lms.contasreceber.faturamentoAutomaticoAction"
 */

@Assynchronous
public class EnvioBoletosBradescoAction extends CrudAction {
	
	private HistoricoBoletoService historicoBoletoService;

	@AssynchronousMethod( name = "HistoricoBoletoService.gererateBoletoBradesco",
			type = BatchType.BATCH_SERVICE,
							feedback = BatchFeedbackType.ON_ERROR)
	public void executeEnvioBoletosBradesco() {
		historicoBoletoService.generateEnvioBoletoBradesco();
	}

	public void setHistoricoBoletoService(
			HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}
	
	
}