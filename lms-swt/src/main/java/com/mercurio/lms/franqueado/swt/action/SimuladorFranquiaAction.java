package com.mercurio.lms.franqueado.swt.action;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.model.service.ProcessamentoCalculoFranqueadosService;

public class SimuladorFranquiaAction {

	private ProcessamentoCalculoFranqueadosService processamentoCalculoFranqueadosService;
	
	public void simularFranquia(TypedFlatMap parameters) throws Exception {
		processamentoCalculoFranqueadosService.executarProcessamentoCalculoFranqueadosSimulacao(parameters);
	}

	public void setProcessamentoCalculoFranqueadosService(ProcessamentoCalculoFranqueadosService processamentoCalculoFranqueadosService) {
		this.processamentoCalculoFranqueadosService = processamentoCalculoFranqueadosService;
	}
	
	

}
