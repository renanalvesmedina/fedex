package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.agentes;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;

public interface AgenteChaveProgressao {

	boolean respondePor(String tag);

	void informaTag(TagImportacao tag);
	
	TipoAgente tipo();
	
	ChaveProgressao chave();

	void reseta();

	boolean estahCompleto();

	void incluiValor(int linha, int coluna, String valor);

	void validaTags();

	int linha();

	int coluna();

	void encerraLinha();
	

}
