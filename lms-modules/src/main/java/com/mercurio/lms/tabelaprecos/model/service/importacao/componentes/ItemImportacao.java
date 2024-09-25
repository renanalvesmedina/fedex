package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;


public abstract class ItemImportacao {

	Integer colunaItem;
	Integer linhaItem;

	public ItemImportacao(Integer linha, Integer coluna) {
		this.colunaItem = coluna;
		this.linhaItem = linha;
	}

	public Integer coluna() {
		return this.colunaItem;
	}

	public Integer linha() {
		return this.linhaItem;
	}

}
