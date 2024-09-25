package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;


public class ExecuteWorkflowClienteTpClienteService {
	private ManterClienteService manterClienteService;
	private ClienteService clienteService;
	
	public void executeWorkflow(List<Long> idsCliente, List<String> situacoes) {
		for (int i = 0; i < idsCliente.size(); i++) {
			Cliente cliente = getManterClienteService().findById(idsCliente.get(i));
			String tpSituacao = situacoes.get(i);

			if (ConstantesWorkflow.APROVADO.equals(tpSituacao)) {
				getManterClienteService().executeAprovacaoWorkflowTpCliente(idsCliente.get(i));
				
			} else if (ConstantesWorkflow.REPROVADO.equals(tpSituacao)) {
				cliente.setTpClienteSolicitado(cliente.getTpCliente());
				getClienteService().store(cliente);	
			}
		}
	}

	public ManterClienteService getManterClienteService() {
		return manterClienteService;
	}

	public void setManterClienteService(ManterClienteService manterClienteService) {
		this.manterClienteService = manterClienteService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
}