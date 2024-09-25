package com.mercurio.lms.vendas.model.service;

import java.util.List;

public class ExecuteWorkflowCotacaoDimensoesAeronaveService {
	private CotacaoService cotacaoService;

	public void executeWorkflow(List<Long> idsCotacoes, List<String> situacoes) {
		for (int i = 0; i < idsCotacoes.size(); i++) {
			cotacaoService.executeWorkflowDimensoesAeronave(idsCotacoes.get(i), situacoes.get(i));
		}
	}

	public CotacaoService getCotacaoService() {
		return cotacaoService;
	}

	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}
}
