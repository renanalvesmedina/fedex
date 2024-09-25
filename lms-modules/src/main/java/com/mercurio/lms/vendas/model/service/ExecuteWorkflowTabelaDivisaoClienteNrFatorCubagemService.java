package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe criada para execução de procedimentos após finalização de workflow
 * referentes a tabela "TABELA_DIVISAO_CLIENTE", campo "NR_FATOR_CUBAGEM".
 * Criada classe específica pois poderão existir outros Workflows para a mesma
 * tabela, porém para outros campos.
 *
 */
public class ExecuteWorkflowTabelaDivisaoClienteNrFatorCubagemService {
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;

	public void executeWorkflow(List<Long> idsTabelaDivisaoCliente, List<String> situacoes) {
		for (int i = 0; i < idsTabelaDivisaoCliente.size(); i++) {
			TabelaDivisaoCliente tabelaDivisaoCliente = getTabelaDivisaoClienteService().findById(idsTabelaDivisaoCliente.get(i));
			String situacao = situacoes.get(i);

			if (ConstantesWorkflow.APROVADO.equals(situacao)) {
				tabelaDivisaoCliente.setNrFatorCubagem(tabelaDivisaoCliente.getNrFatorCubagemSolicitado());
			} else if (ConstantesWorkflow.REPROVADO.equals(situacao)) {
				tabelaDivisaoCliente.setNrFatorCubagemSolicitado(tabelaDivisaoCliente.getNrFatorCubagem());
			}
			getTabelaDivisaoClienteService().store(tabelaDivisaoCliente);
		}
	}

	public TabelaDivisaoClienteService getTabelaDivisaoClienteService() {
		return tabelaDivisaoClienteService;
	}

	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
}