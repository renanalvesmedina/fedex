package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsImportacaoTabelaPreco;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.tags.TagsFaixaProgressiva;

import java.util.regex.Pattern;

public class ItemValorFaixaProgressivaImportacao {

	private ValorImportacao valorItemValorFaixaProgressiva;
	private ValorImportacao valorIndicadorTpCalculoFaixaProgressiva;
	private ValorImportacao pesoMinimoItemValorFaixaProgressiva;
	private ValorImportacao fatorMultiplicacaoItemValorFaixaProgressiva;
	private ValorImportacao valorTaxaFixaProgressiva;
	private ValorImportacao valorKmExtra;

	public ValorImportacao valor() {
		return valorItemValorFaixaProgressiva;
	}

	public ValorImportacao pesoMinimo() {
		return pesoMinimoItemValorFaixaProgressiva;
	}

	public ValorImportacao fatorMultiplicacao() {
		return fatorMultiplicacaoItemValorFaixaProgressiva;
	}

	public void incluiValor(ValorImportacao valor) {
		if (TagsFaixaProgressiva.tagEhValorFaixa(valor.tag()) || TagsFaixaProgressiva.tagEhCodigoProdutoEspecifico(valor.tag())) {
			this.valorItemValorFaixaProgressiva = valor;
		}
		if (TagsFaixaProgressiva.tagEhPesoMinimo(valor.tag())) {
			this.pesoMinimoItemValorFaixaProgressiva = valor;
		}
		if (TagsFaixaProgressiva.tagEhFatorMultiplicacao(valor.tag())) {
			this.fatorMultiplicacaoItemValorFaixaProgressiva = valor;
		}
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_VALOR_TAXA_FIXA_PROGRESSIVA, valor.tag())) {
			valorTaxaFixaProgressiva = valor;
		}
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_VALOR_KM_EXTRA, valor.tag())) {
			valorKmExtra = valor;
		}
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_TIPO_IND_CALC, valor.tag())) {
			valorIndicadorTpCalculoFaixaProgressiva = valor;
		}
	}

	public int linha() {
		return this.valorItemValorFaixaProgressiva.linha();
	}

	public boolean jaContemValor(ValorImportacao valor) {
		if (TagsFaixaProgressiva.tagEhValorFaixa(valor.tag()) || TagsFaixaProgressiva.tagEhCodigoProdutoEspecifico(valor.tag()) ) {
			return this.valorItemValorFaixaProgressiva != null;
		}
		if (TagsFaixaProgressiva.tagEhPesoMinimo(valor.tag())) {
			return this.pesoMinimoItemValorFaixaProgressiva != null;
		}
		if (TagsFaixaProgressiva.tagEhFatorMultiplicacao(valor.tag())) {
			return this.fatorMultiplicacaoItemValorFaixaProgressiva != null;
		}
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_VALOR_TAXA_FIXA_PROGRESSIVA, valor.tag())) {
			return valorTaxaFixaProgressiva != null;
		}
		if (Pattern.matches(TagsImportacaoTabelaPreco.FORMATO_TAG_VALOR_KM_EXTRA, valor.tag())) {
			return valorKmExtra != null;
		}
		if (TagsFaixaProgressiva.tagEhTipoIndicadorCalculo(valor.tag())) {
			return valorIndicadorTpCalculoFaixaProgressiva != null;
		}
		return false;
		
	}

	public ValorImportacao valorTaxaFixaProgressiva() {
		return this.valorTaxaFixaProgressiva;
	}

	public ValorImportacao valorKmExtra() {
		return this.valorKmExtra;
	}

	public ValorImportacao indicadorTipoCalculo() {
		return this.valorIndicadorTpCalculoFaixaProgressiva;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!ItemValorFaixaProgressivaImportacao.class.isAssignableFrom(o.getClass())) {
			return false;
		}
		ItemValorFaixaProgressivaImportacao item = (ItemValorFaixaProgressivaImportacao) o;
		return new EqualsBuilder().append(this.valorItemValorFaixaProgressiva, item.valorItemValorFaixaProgressiva)
				.append(this.pesoMinimoItemValorFaixaProgressiva, item.pesoMinimoItemValorFaixaProgressiva)
				.append(this.fatorMultiplicacaoItemValorFaixaProgressiva, item.fatorMultiplicacaoItemValorFaixaProgressiva)
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.valorItemValorFaixaProgressiva)
			.append(this.pesoMinimoItemValorFaixaProgressiva)
			.append(this.fatorMultiplicacaoItemValorFaixaProgressiva)
			.toHashCode();
	}
}
