package com.mercurio.lms.tabelaprecos.model.service;

import java.util.List;

import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public class ExecuteWorkflowTabelaPrecoEfetivacaoService {
	private TabelaPrecoService tabelaPrecoService;

	public void executeWorkflow(List<Long> idsTabelaPreco, List<String> situacoes) {
		for (int i = 0; i < idsTabelaPreco.size(); i++) {
			if (ConstantesWorkflow.REPROVADO.equals(situacoes.get(i)) || ConstantesWorkflow.CANCELADO.equals(situacoes.get(i))) {
				tabelaPrecoService.cancelPendenciaEfetivacao(idsTabelaPreco.get(i));
			} else if (ConstantesWorkflow.APROVADO.equals(situacoes.get(i))) {
				tabelaPrecoService.executeEfetivacao(idsTabelaPreco.get(i));
			}
		}
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

}