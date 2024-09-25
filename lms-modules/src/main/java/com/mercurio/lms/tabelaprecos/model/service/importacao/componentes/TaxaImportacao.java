package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.math.BigDecimal;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class TaxaImportacao implements ComponenteImportacao {

	private ValorImportacao valorTaxaImportacao;
	private ValorImportacao pesoTaxadoTaxaImportacao;
	private ValorImportacao valorExcedenteTaxaImportacao;

	public TaxaImportacao(ValorImportacao valor, ValorImportacao pesoTaxado, ValorImportacao valorExcedente) {
		this.valorTaxaImportacao = valor;
		this.pesoTaxadoTaxaImportacao = pesoTaxado;
		this.valorExcedenteTaxaImportacao = valorExcedente;
	}

	@Override
	public String valorTagPrincipal() {
		this.valida();
		return this.valorTaxaImportacao.tag();
	}

	@Override
	public TipoComponente tipo() {
		return TipoComponente.TAXA;
	}

	private void valida()  {
		if (this.valorTaxaImportacao == null) {
			ValorImportacao valorNaoNulo = this.valorNaoNulo(this.pesoTaxadoTaxaImportacao, this.valorExcedenteTaxaImportacao);
			throw new ImportacaoException(valorNaoNulo.colunaItem, valorNaoNulo.linhaItem, String.format("Não foi encontrada a tag principal da tag '%s'.", valorNaoNulo.tag()));
		}
	}

	private ValorImportacao valorNaoNulo(ValorImportacao ... valores) {
		for (ValorImportacao valorImportacao : valores) {
			if (valorImportacao != null) {
				return valorImportacao;
			}
		}
		return null;
	}

	public BigDecimal pesoTaxado() {
		if (this.pesoTaxadoTaxaImportacao == null) {
			return null;
		}
		return this.pesoTaxadoTaxaImportacao.valorBigDecimal(BigDecimal.ZERO);
	}

	public BigDecimal valorExcedente() {
		if (this.valorExcedenteTaxaImportacao == null) {
			return null;
		}
		return this.valorExcedenteTaxaImportacao.valorBigDecimal(BigDecimal.ZERO);
	}

	public BigDecimal valor() {
		this.valida();
		return this.valorTaxaImportacao.valorBigDecimal();
	}

	@Override
	public TagImportacao tagPrincipal() {
		this.valida();
		return this.valorTaxaImportacao.tagReferancia();
	}

}
