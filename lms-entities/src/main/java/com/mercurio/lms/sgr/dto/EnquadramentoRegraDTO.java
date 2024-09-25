package com.mercurio.lms.sgr.dto;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.FaixaDeValor;

/**
 * LMS-6850 - Data Transfer Object para resultado do enquadramento de regras do
 * Plano de Gerenciamento de Riscos.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * 
 */
public class EnquadramentoRegraDTO {

	private EnquadramentoRegra enquadramentoRegra;
	private FaixaDeValor faixaDeValor;
	private BigDecimal vlMercadoria;

	public EnquadramentoRegraDTO(EnquadramentoRegra enquadramentoRegra, FaixaDeValor faixaDeValor, BigDecimal vlMercadoria) {
		super();
		this.enquadramentoRegra = enquadramentoRegra;
		this.faixaDeValor = faixaDeValor;
		this.vlMercadoria = vlMercadoria;
	}

	public EnquadramentoRegra getEnquadramentoRegra() {
		return enquadramentoRegra;
	}

	public void setEnquadramentoRegra(EnquadramentoRegra enquadramentoRegra) {
		this.enquadramentoRegra = enquadramentoRegra;
	}

	public FaixaDeValor getFaixaDeValor() {
		return faixaDeValor;
	}

	public void setFaixaDeValor(FaixaDeValor faixaDeValor) {
		this.faixaDeValor = faixaDeValor;
	}

	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}

	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("enquadramentoRegra", enquadramentoRegra)
				.append("faixaDeValor", faixaDeValor)
				.append("vlMercadoria", vlMercadoria)
				.toString();
	}

}
