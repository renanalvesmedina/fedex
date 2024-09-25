package com.mercurio.lms.tabelaprecos.model.service;

import java.util.List;

import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public class ExecuteWorkflowTabelaPrecoService {
	private TabelaPrecoService tabelaPrecoService;

	public void executeWorkflow(List<Long> idsTabelaPreco, List<String> situacoes) {
		for (int i = 0; i < idsTabelaPreco.size(); i++) {
			if (ConstantesWorkflow.REPROVADO.equals(situacoes.get(i)) || ConstantesWorkflow.CANCELADO.equals(situacoes.get(i))) {
				tabelaPrecoService.cancelPendencia(idsTabelaPreco.get(i));
			}
		}
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

}