package com.mercurio.lms.tabelaprecos.model.service.importacao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.tabelaprecos.model.service.importacao.excessao.MensagemErroImportacao;

public class ImportacaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final List<MensagemErroImportacao> mensagens;
	
	public ImportacaoException(int coluna, int linha, String mensagem) {
		this.mensagens = new ArrayList<MensagemErroImportacao>();
		this.mensagens.add(new MensagemErroImportacao(linha, coluna, mensagem));
	}

	public ImportacaoException(List<MensagemErroImportacao> mensagens) {
		this.mensagens = mensagens;
	}

	public void incluiMensagem(int coluna, int linha, String mensagem) {
		this.mensagens.add(new MensagemErroImportacao(linha, coluna, mensagem));
	}
	
	@Override
	public String getMessage() {
		List<String> mensagens = this.mensagens();
		StringBuilder messageBuilder = new StringBuilder();
		for (String string : mensagens) {
			if (messageBuilder.length() > 0) {
				messageBuilder.append("\n");
			}
			messageBuilder.append(string);
		}
        return messageBuilder.toString();
    }
	
	public List<String> mensagens() {
		List<String> list = new ArrayList<String>();
		for (MensagemErroImportacao mensagem : this.mensagens) {
			list.add(mensagem.textoMensagem());
		}
		return list;
	}

}
