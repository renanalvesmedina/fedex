package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.Observavel;

public abstract class AgenteObservavel extends Observavel implements AgenteChaveProgressao {
	
	@Override
	public void incluiValor(int linha, int coluna, String valor) {
		this.executaIncluiValor(linha, coluna, valor);
	}

	abstract void executaIncluiValor(int linha, int coluna, String valor);
	
	@Override
	public void encerraLinha() {
		if (this.estahCompleto()) {
			this.atualiza();
		}
	}
	
}
