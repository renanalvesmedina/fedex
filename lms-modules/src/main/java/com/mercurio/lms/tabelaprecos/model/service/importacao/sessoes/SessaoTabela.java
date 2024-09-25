package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes;

import java.util.List;
import java.util.Map;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoExceptionBloqueante;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;

public class SessaoTabela implements SessaoArquivoImportacao {

	private OperadoresTagImportacao operadores = new OperadoresTagImportacao();

	public void incluiTag(int linha, int coluna, String tag) {
		try {
			OperadorTagImportacao operador = operadores.operadorPara(tag, coluna);
			operador.incluiTag(linha, coluna, tag);
		} catch (IllegalStateException e) {
			throw new ImportacaoExceptionBloqueante(coluna, linha, String.format("O valor '%s' não é uma tag válida.", tag));
		}
	}

	public void incluiValor(int linha, int coluna, String valor) {
		OperadorTagImportacao operador = operadores.operadorDe(coluna);
		operador.incluiValor(linha, coluna, valor);
	}

	public void incluiFaixa(int linha, int coluna, String valor) {
		OperadorTagImportacao operador = operadores.operadorDe(coluna);
		operador.incluiFaixa(linha, coluna, valor);
	}

	@Override
	public Map<TipoComponente, List<ComponenteImportacao>> resultadoImportacao() {
		Map<TipoComponente, List<ComponenteImportacao>> resultado = operadores.resultadoImportacao();
		this.operadores = null;
		return resultado;
	}

	public void validaLinhaTags() {
		this.operadores.validaTags();

	}

	public void novaLinha() {
		this.operadores.novaLinha();
	}


}






