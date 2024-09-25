package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public class ExecuteWorkflowClienteFilialOperacionalService {
	private ClienteService clienteService;

	public void executeWorkflow(List<Long> idsCliente, List<String> situacoes) {
		for (int i = 0; i < idsCliente.size(); i++) {
			Long idCliente = idsCliente.get(i);
			String tpSituacao = situacoes.get(i);

			if (ConstantesWorkflow.APROVADO.equals(tpSituacao)) {
				clienteService.executeAprovacaoWKFilialOperacional(idCliente);
			} else if (ConstantesWorkflow.REPROVADO.equals(tpSituacao)) {
				clienteService.executeReprovacaoWKFilialOperacional(idCliente);
			}
		}
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
}