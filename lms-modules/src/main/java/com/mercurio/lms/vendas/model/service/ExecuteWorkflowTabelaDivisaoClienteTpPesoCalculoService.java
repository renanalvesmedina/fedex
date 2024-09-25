package com.mercurio.lms.vendas.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe criada para execução de procedimentos após finalização de workflow
 * referentes a tabela "TABELA_DIVISAO_CLIENTE", campo "TP_PESO_CALCULO".
 * Criada classe específica pois poderão existir outros Workflows para a mesma
 * tabela, porém para outros campos.
 *
 */
public class ExecuteWorkflowTabelaDivisaoClienteTpPesoCalculoService {
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	
	private static final Map<String, String> TP_PESO_CALCULOSOLICITADO = new HashMap<String, String>();
	
	static {
		TP_PESO_CALCULOSOLICITADO.put("C", "Cubado");
		TP_PESO_CALCULOSOLICITADO.put("D", "Declarado");
		TP_PESO_CALCULOSOLICITADO.put("F", "Aferido");
		TP_PESO_CALCULOSOLICITADO.put("G", "Cubado Declarado");
	}	
	

	public void executeWorkflow(List<Long> idsTabelaDivisaoCliente, List<String> situacoes) {
		for (int i = 0; i < idsTabelaDivisaoCliente.size(); i++) {
			TabelaDivisaoCliente tabelaDivisaoCliente = getTabelaDivisaoClienteService().findById(idsTabelaDivisaoCliente.get(i));
			String tpPesoCalculo = situacoes.get(i);

			if (ConstantesWorkflow.APROVADO.equals(tpPesoCalculo)) {
				tabelaDivisaoCliente.setTpPesoCalculo(tabelaDivisaoCliente.getTpPesoCalculoSolicitado());
				
				if(validateTpPesoCalculoSolicitado(tabelaDivisaoCliente.getTpPesoCalculoSolicitado().getValue())){
					tabelaDivisaoCliente.setNrLimiteMetragemCubica(null);
					tabelaDivisaoCliente.setNrLimiteQuantVolume(null);
				}
			} else if (ConstantesWorkflow.REPROVADO.equals(tpPesoCalculo)) {
				tabelaDivisaoCliente.setTpPesoCalculoSolicitado(tabelaDivisaoCliente.getTpPesoCalculo());
			}
			getTabelaDivisaoClienteService().store(tabelaDivisaoCliente);
		}
	}
	
	private boolean validateTpPesoCalculoSolicitado(String tpPesoCalculoSolicitado){
		return TP_PESO_CALCULOSOLICITADO.containsKey(tpPesoCalculoSolicitado);
	}

	public TabelaDivisaoClienteService getTabelaDivisaoClienteService() {
		return tabelaDivisaoClienteService;
	}

	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
}