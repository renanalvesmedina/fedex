package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.OperadorTagImportacao;

public abstract class OperadorObservavel extends Observavel implements OperadorTagImportacao {

	protected abstract boolean executaIncluiValor(int linha, int coluna, String valor);

	@Override
	public boolean incluiFaixa(int linha, int coluna, String tag) {
		return true;
	}

	@Override
	public boolean incluiValor(int linha, int coluna, String valor) {
		return this.executaIncluiValor(linha, coluna, valor);
	}

	protected abstract boolean estahCompleto();

	protected abstract ChaveProgressao retornaChave();

	@Override
	public void encerraLinha() {
		this.executaEncerraLinha();
		if (this.estahCompleto()) {
			this.atualiza();
		}
	}

	protected abstract void executaEncerraLinha();
	
}
