package com.mercurio.lms.tabelaprecos.model.service.importacao.componentes;

import java.math.BigDecimal;

import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;

public class GeneralidadeImportacao implements ComponenteImportacao {

	private ValorImportacao valorGeneralidade;
	private ValorImportacao pesoMinimoGeneralidade;
	private ValorImportacao valorMinimoGeneralidade;

	public GeneralidadeImportacao(ValorImportacao valor, ValorImportacao pesoMinimo, ValorImportacao valorMinimo) {
		this.valorGeneralidade = valor;
		this.pesoMinimoGeneralidade = pesoMinimo;
		this.valorMinimoGeneralidade = valorMinimo;
	}

	@Override
	public String valorTagPrincipal() {
		valida();
		return this.valorGeneralidade.tag();
	}

	@Override
	public TipoComponente tipo() {
		return TipoComponente.GENERALIDADE;
	}

	public BigDecimal valor() {
		valida();
		return this.valorGeneralidade.valorBigDecimal(BigDecimal.ZERO);
	}

	private void valida()  {
		if (this.valorGeneralidade == null) {
			ValorImportacao valorNaoNulo = this.valorNaoNulo(this.valorMinimoGeneralidade, this.pesoMinimoGeneralidade);
			throw new ImportacaoException(valorNaoNulo.colunaItem, valorNaoNulo.linhaItem, String.format("Não foi encontrada a tag principal da tag %s.", valorNaoNulo.tag()));
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

	public BigDecimal valorMinimo() {
		if (this.valorMinimoGeneralidade == null) {
			return null;
		}
		return this.valorMinimoGeneralidade.valorBigDecimal(BigDecimal.ZERO);
	}

	public BigDecimal pesoMinimo() {
		if (this.pesoMinimoGeneralidade == null) {
			return null;
		}
		return this.pesoMinimoGeneralidade.valorBigDecimal(BigDecimal.ZERO);
	}

	@Override
	public TagImportacao tagPrincipal() {
		this.valida();
		return this.valorGeneralidade.tagReferancia();
	}

}
