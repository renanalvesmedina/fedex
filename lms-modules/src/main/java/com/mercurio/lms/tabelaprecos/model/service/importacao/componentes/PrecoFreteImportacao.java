package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsPrecoFrete;

public class PrecoFreteImportacao {

	private ChaveProgressao chavePrecoFrete;
	private ValorImportacao pesoMinimoPrecoFrete;
	private ValorImportacao valorFretePrecoFrete;

	public boolean informaChave(ChaveProgressao chaveProgressao){
		this.chavePrecoFrete = chaveProgressao;
		return true;
	}

	public boolean incluiValor(ValorImportacao valorImportacao){
		if(!TagsPrecoFrete.ehTagPrecoFrete(valorImportacao.tag())){
			throw new ImportacaoException(valorImportacao.linha(), valorImportacao.coluna(), String.format("A tag %s não é uma tag de proço frete válida", valorImportacao.tag()));
		}
		if(Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_VALOR_PRECO_FRETE, valorImportacao.tag())){
			this.valorFretePrecoFrete = valorImportacao;
		}
		if(Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_PESO_MINIMO_PRECO_FRETE, valorImportacao.tag())){
			this.pesoMinimoPrecoFrete = valorImportacao;
		}
		return true;
	}

	public ChaveProgressao chave(){
		return this.chavePrecoFrete;
	}

	public BigDecimal pesoMinimo(){
		if (this.pesoMinimoPrecoFrete == null) {
			return null;
		}
		return this.pesoMinimoPrecoFrete.valorBigDecimal();
	}

	public BigDecimal valorFrete(){
		return this.valorFretePrecoFrete.valorBigDecimal();
	}

	public int linha() {
		if (this.valorFretePrecoFrete != null) {
			return this.valorFretePrecoFrete.linha();
		}
		if (this.pesoMinimoPrecoFrete != null) {
			return this.pesoMinimoPrecoFrete.linha();
		}
		return 0;
	}

	public boolean estahCompleto() {
		return this.chaveEstaCompleta() && ValorImportacao.estahInformado(this.valorFretePrecoFrete) && ValorImportacao.estahInformado(this.pesoMinimoPrecoFrete);
	}

	private boolean chaveEstaCompleta() {
		return this.chavePrecoFrete != null && this.chavePrecoFrete.estahCompleta();
	}

}
