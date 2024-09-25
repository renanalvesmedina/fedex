package com.mercurio.lms.tabelaprecos.model.service.importacao.excessao;

import java.io.Serializable;

public class MensagemErroImportacao implements Serializable {

	private static final long serialVersionUID = 1949737365983233587L;

	private int linha;
	private int coluna;
	private String mensagem;
	
	public MensagemErroImportacao(int linha, int coluna, String mensagem) {
		this.coluna = coluna;
		this.linha = linha;
		this.mensagem = mensagem;
	}

	public String textoMensagem() {
		String colunaExcel = this.converteColuna(this.coluna + 1);
		String celulaExcel = colunaExcel + (this.linha + 1);
		return String.format("%s: %s", celulaExcel, this.mensagem);
	}

	private String converteColuna(int col) {
		return new ErroImportacaoUtils().converteColuna(col);
	}
	
}
