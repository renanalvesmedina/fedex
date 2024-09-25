package com.mercurio.lms.entrega.util;

public enum EstadoAgendamento {
	AGENDAMENTO_TNT("tnt"),
	AGENDAMENTO_CLIENTE("cliente"), 
	LIBERACAO_AGENDAMENTO("liberacao");

	private String chave;

	EstadoAgendamento(String chave) {
		this.chave = chave;
	}
	
	public String getChave() {
		return chave;
	}
}
