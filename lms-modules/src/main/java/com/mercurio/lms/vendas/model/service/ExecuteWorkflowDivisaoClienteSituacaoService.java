package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe criada para execu��o de procedimentos ap�s finaliza��o de workflow
 * referentes a tabela "DIVISAO_CLIENTE", campo "TP_SITUACAO".
 * Criada classe espec�fica pois poder�o existir outros Workflows para a mesma
 * tabela, por�m para outros campos.
 *
 */
public class ExecuteWorkflowDivisaoClienteSituacaoService {
	private DivisaoClienteService divisaoClienteService;

	public void executeWorkflow(List<Long> idsDivisaoCliente, List<String> situacoes) {
		for (int i = 0; i < idsDivisaoCliente.size(); i++) {
			DivisaoCliente divisaoCliente = getDivisaoClienteService().findById(idsDivisaoCliente.get(i));
			String tpSituacao = situacoes.get(i);

			if (ConstantesWorkflow.APROVADO.equals(tpSituacao)) {
				divisaoCliente.setTpSituacao(divisaoCliente.getTpSituacaoSolicitada());
			} else if (ConstantesWorkflow.REPROVADO.equals(tpSituacao)) {
				divisaoCliente.setTpSituacaoSolicitada(divisaoCliente.getTpSituacao());
			}
			getDivisaoClienteService().store(divisaoCliente);
		}
	}

	public DivisaoClienteService getDivisaoClienteService() {
		return divisaoClienteService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

}