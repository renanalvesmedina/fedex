package com.mercurio.lms.entrega.util;

public enum EstadoBloqueio {
	BLOQUEIO("bloqueio"), LIBERACAO_BLOQUEIO("liberacao");

	private String chave;

	EstadoBloqueio(String chave) {
		this.chave = chave;
	}

	public String getChave() {
		return chave;
	}

}
