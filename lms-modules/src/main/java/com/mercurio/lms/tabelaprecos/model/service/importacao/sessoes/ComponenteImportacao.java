package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;



public interface ComponenteImportacao {

	public enum TipoComponente {
		PRECO_FRETE, FAIXA_PROGRESSIVA, GENERALIDADE, TAXA, SERVICO_ADICIONAL, GENERICO
	};

	String valorTagPrincipal();

	TipoComponente tipo();

	TagImportacao tagPrincipal();

}
