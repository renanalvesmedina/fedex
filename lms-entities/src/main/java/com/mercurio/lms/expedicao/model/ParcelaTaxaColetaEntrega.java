package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;

public class ParcelaTaxaColetaEntrega extends ParcelaServico {
	private static final long serialVersionUID = 1L;
	private ValorTaxa valorTaxa;
	private BigDecimal vlDistancia;

	public ParcelaTaxaColetaEntrega(ParcelaPreco parcelaPreco,
			BigDecimal vlUnitarioParcela, BigDecimal vlBrutoParcela,
			ValorTaxa valorTaxa, BigDecimal vlDistancia) {
		super(parcelaPreco, vlUnitarioParcela, vlBrutoParcela);
		this.valorTaxa = valorTaxa;
		this.vlDistancia = vlDistancia;
	}

	public ValorTaxa getValorTaxa() {
		return valorTaxa;
	}

	public void setValorTaxa(ValorTaxa valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	public BigDecimal getVlDistancia() {
		return vlDistancia;
	}

	public void setVlDistancia(BigDecimal vlDistancia) {
		this.vlDistancia = vlDistancia;
	}

}
