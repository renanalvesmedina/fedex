package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes;

public interface ChaveProgressao extends Comparable<ChaveProgressao> {
	
	public enum TipoChaveProgressao { 
		ROTA, TARIFA, TARIFA_X_ROTA 
	};

	boolean valida();

	boolean estahCompleta();

	int linha();

	TipoChaveProgressao tipo();

	int coluna();

}
