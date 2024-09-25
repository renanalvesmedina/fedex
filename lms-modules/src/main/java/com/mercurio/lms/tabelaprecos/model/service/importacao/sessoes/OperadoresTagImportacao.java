package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.OperadorChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.OperadorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.OperadorPrecoFrete;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsPrecoFrete;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsRota;


public class OperadoresTagImportacao {

	private static final String CHAVE_PRECO_FRETE = "PRECOFRETE";
	private static final String CHAVE_FAIXA_PROGRESSIVA = "FAIXAPROGRESSIVA";
	private static final String CHAVE_IDENT_PROGRESSAO = "CHAVEPROGRESSAO";
	

	private Map<Integer, OperadorTagImportacao> operadoresPorColuna = new HashMap<Integer, OperadorTagImportacao>();
	private Map<String, OperadorTagImportacao> operadoresPorChave = new HashMap<String, OperadorTagImportacao>();

	private OperadorFaixaProgressiva operadorFaixaProgressiva;
	private OperadorPrecoFrete operadorPrecoFrete;
	private OperadorChaveProgressao operadorChaveProgressao;


	OperadoresTagImportacao(){

	}

	public OperadorTagImportacao operadorPara(String tag, int coluna) {
		if (this.operadoresPorColuna.containsKey(coluna)) {
			return this.operadoresPorColuna.get(coluna);
		}

		String chave = this.preparaChave(tag);
		if (this.operadoresPorChave.containsKey(chave)) {
			OperadorTagImportacao operador = this.operadoresPorChave.get(chave);
			this.operadoresPorColuna.put(coluna, operador);
			return operador;
		}
		OperadorTagImportacao operador = this.preparaOperador(chave);
		if (operador == null) {
			throw new IllegalStateException();
		}
		this.operadoresPorChave.put(chave, operador);
		this.operadoresPorColuna.put(coluna, operador);
		return operador;
	}

	private OperadorTagImportacao preparaOperador(String chave) {
		if (chave.equals(CHAVE_PRECO_FRETE)) {
			return this.preparaPrecoFrete();
		}
		if (chave.equals(CHAVE_FAIXA_PROGRESSIVA)) {
			return this.preparaFaixaProgressiva();
		}
		if (chave.equals(CHAVE_IDENT_PROGRESSAO)) {
			return this.preparaChaveProgressao();
		}
		return null;
	}

	private OperadorTagImportacao preparaChaveProgressao() {
		this.operadorChaveProgressao = new OperadorChaveProgressao();
		if (this.operadorPrecoFrete != null) {
			this.operadorChaveProgressao.incluiObservador(this.operadorPrecoFrete);
		}
		if (this.operadorFaixaProgressiva != null) {
			this.operadorChaveProgressao.incluiObservador(this.operadorFaixaProgressiva);
		}
		return this.operadorChaveProgressao;
	}

	private OperadorTagImportacao preparaFaixaProgressiva() {
		this.operadorFaixaProgressiva = new OperadorFaixaProgressiva();
		if (this.operadorChaveProgressao != null) {
			this.operadorChaveProgressao.incluiObservador(this.operadorFaixaProgressiva);
		}
		return this.operadorFaixaProgressiva;
	}

	private OperadorTagImportacao preparaPrecoFrete() {
		this.operadorPrecoFrete = new OperadorPrecoFrete();
		if (this.operadorChaveProgressao != null) {
			this.operadorChaveProgressao.incluiObservador(this.operadorPrecoFrete);
		}
		return this.operadorPrecoFrete;
	}

	private String preparaChave(String tag) {
		if (TagsPrecoFrete.ehTagPrecoFrete(tag)) {
			return CHAVE_PRECO_FRETE;
		}
		if (TagsFaixaProgressiva.ehTagFaixaProgressiva(tag)) {
			return CHAVE_FAIXA_PROGRESSIVA;
		}
		if (TagsRota.ehTagRota(tag) || TagsImportacaoTabelaPreco.TAG_COD_TARIFA.equals(tag) || TagsImportacaoTabelaPreco.TAG_TARIFA_X_ROTA.equals(tag)) {
			return CHAVE_IDENT_PROGRESSAO;
		}
		return "";
	}


	public OperadorTagImportacao operadorDe(int coluna) {
		if (this.operadoresPorColuna.containsKey(coluna)) {
			return this.operadoresPorColuna.get(coluna);
		}
		throw new IllegalArgumentException(String.format("Operador não encontrado para coluna %d.", coluna));
	}


	public Map<TipoComponente, List<ComponenteImportacao>> resultadoImportacao() {
		Map<TipoComponente, List<ComponenteImportacao>> resultado = new HashMap<ComponenteImportacao.TipoComponente, List<ComponenteImportacao>>();
		for (OperadorTagImportacao operador : this.operadoresPorChave.values()) {
			if(operador.tipoOperador() != null && CollectionUtils.isNotEmpty(operador.resultadoImportacao())){
				resultado.put(operador.tipoOperador(), operador.resultadoImportacao());
			}
		}
		return resultado;
	}

	public void validaTags() {
		for (OperadorTagImportacao operador : this.operadoresPorChave.values()) {
			operador.validaTags();
		}
		if (!this.operadoresPorChave.containsKey(CHAVE_IDENT_PROGRESSAO)) {
			throw new ImportacaoException(0, 0, "É necessário informar tags de rota ou tag de código de tarifa ao importar uma tabela.");
		}

	}

	public void novaLinha() {
		for (OperadorTagImportacao operador : this.operadoresPorChave.values()) {
			operador.encerraLinha();
		}
		for (OperadorTagImportacao operador : this.operadoresPorChave.values()) {
			operador.reinicia();
		}
	}
}
