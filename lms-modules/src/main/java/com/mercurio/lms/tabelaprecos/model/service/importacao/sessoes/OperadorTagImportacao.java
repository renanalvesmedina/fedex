package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes;

import java.util.List;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;


public interface OperadorTagImportacao {

	boolean incluiTag(int linha, int coluna, String valor);

	boolean incluiFaixa(int linha, int coluna, String valor);

	boolean incluiValor(int linha, int coluna, String valor);

	List<ComponenteImportacao> resultadoImportacao();

	TipoComponente tipoOperador();

	void validaTags();
	
	void encerraLinha();

	void reinicia();



}
