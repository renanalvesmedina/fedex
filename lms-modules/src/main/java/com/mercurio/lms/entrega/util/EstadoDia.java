package com.mercurio.lms.entrega.util;

public enum EstadoDia {
	DIA_UTIL("diaUtil"), 
	FIM_DE_SEMANA("fimDeSemana"), 
	FERIADO("feriado"), 
	DIA_PROJETADO("diaProjetado");

	private String chave;

	EstadoDia(String chave) {
		this.chave = chave;
	}

	public String getChave() {
		return chave;
	}
	
}
