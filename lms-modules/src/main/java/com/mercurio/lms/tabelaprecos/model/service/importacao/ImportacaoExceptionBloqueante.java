package com.mercurio.lms.tabelaprecos.model.service.importacao;

import com.mercurio.lms.tabelaprecos.model.service.importacao.excessao.MensagemErroImportacao;

public class ImportacaoExceptionBloqueante extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final MensagemErroImportacao mensagem;
	
	public ImportacaoExceptionBloqueante(int coluna, int linha, String mensagem) {
		this.mensagem = new MensagemErroImportacao(linha, coluna, mensagem);
	}

	@Override
	public String toString() {
		return this.mensagem.textoMensagem();
	}
	
	@Override
	public String getMessage() {
        return this.toString();
    }

}
