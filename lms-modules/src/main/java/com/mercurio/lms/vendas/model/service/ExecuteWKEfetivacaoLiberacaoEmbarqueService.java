package com.mercurio.lms.vendas.model.service;

import java.util.List;


public class ExecuteWKEfetivacaoLiberacaoEmbarqueService {
	private LiberacaoEmbarqueService liberacaoEmbarqueService;
	
	public void executeWorkflow(List<Long> idsLiberacaoEmbarque, List<String> situacoes) {
		for (int i = 0; i < idsLiberacaoEmbarque.size(); i++) {
			liberacaoEmbarqueService.executeEfetivar(idsLiberacaoEmbarque.get(i), situacoes.get(i));
		}
	}

	public void setLiberacaoEmbarqueService(LiberacaoEmbarqueService liberacaoEmbarqueService) {
		this.liberacaoEmbarqueService = liberacaoEmbarqueService;
	}
}
