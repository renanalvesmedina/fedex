package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe criada para execu��o de procedimentos ap�s finaliza��o de workflow
 * referentes a tabela "TABELA_DIVISAO_CLIENTE", campo "NR_FATOR_DENSIDADE".
 * Criada classe espec�fica pois poder�o existir outros Workflows para a mesma
 * tabela, por�m para outros campos.
 *
 */
public class ExecuteWorkflowTabelaDivisaoClienteNrFatorDensidadeService {
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;

	public void executeWorkflow(List<Long> idsTabelaDivisaoCliente, List<String> situacoes) {
		for (int i = 0; i < idsTabelaDivisaoCliente.size(); i++) {
			TabelaDivisaoCliente tabelaDivisaoCliente = getTabelaDivisaoClienteService().findById(idsTabelaDivisaoCliente.get(i));
			String situacao = situacoes.get(i);

			if (ConstantesWorkflow.APROVADO.equals(situacao)) {
				tabelaDivisaoCliente.setNrFatorDensidade(tabelaDivisaoCliente.getNrFatorDensidadeSolicitado());
			} else if (ConstantesWorkflow.REPROVADO.equals(situacao)) {
				tabelaDivisaoCliente.setNrFatorDensidadeSolicitado(tabelaDivisaoCliente.getNrFatorDensidade());
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