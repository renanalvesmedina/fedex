package com.mercurio.lms.sgr.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.util.CompareUtils;

public class FaixaDeValorBuilder {

	public static FaixaDeValorBuilder newFaixaDeValor() {
		return new FaixaDeValorBuilder();
	}

	private FaixaDeValor faixa;

	private FaixaDeValorBuilder() {
		faixa = new FaixaDeValor();
		faixa.setBlExclusivaAeroporto(false);
	}

	public FaixaDeValorBuilder limite(double minimo) {
		return limite(minimo, Double.NaN);
	}

	public FaixaDeValorBuilder limite(double minimo, double maximo) {
		if (!Double.isNaN(maximo) && CompareUtils.ge(minimo, maximo)) {
			throw new IllegalStateException("Limites mínimo/máximo inválidos para faixa de valor.");
		}
		faixa.setVlLimiteMinimo(BigDecimal.valueOf(minimo));
		faixa.setVlLimiteMaximo(Double.isNaN(maximo) ? null : BigDecimal.valueOf(maximo));
		return this;
	}

	public FaixaDeValorBuilder requerLiberacaoCemop() {
		faixa.setBlRequerLiberacaoCemop(true);
		return this;
	}

	public FaixaDeValorBuilder exclusivaAeroporto() {
		faixa.setBlExclusivaAeroporto(true);
		return this;
	}

	public FaixaDeValorBuilder naturezaImpedida(NaturezaProduto naturezaProduto) {
		return naturezaImpedida(naturezaProduto, Double.NaN);
	}

	public FaixaDeValorBuilder naturezaImpedida(NaturezaProduto naturezaProduto, double limitePermitido) {
		if (faixa.getNaturezasImpedidas() == null) {
			faixa.setNaturezasImpedidas(new ArrayList<FaixaValorNaturezaImpedida>());
		} else {
			for (FaixaValorNaturezaImpedida naturezaImpedida : faixa.getNaturezasImpedidas()) {
				if (naturezaProduto.equals(naturezaImpedida.getNaturezaProduto())) {
					throw new IllegalStateException("Natureza de produto impedida já incluída na faixa de valor.");
				}
			}
		}
		FaixaValorNaturezaImpedida naturezaImpedida = new FaixaValorNaturezaImpedida();
		naturezaImpedida.setNaturezaProduto(naturezaProduto);
		naturezaImpedida.setVlLimitePermitido(Double.isNaN(limitePermitido) ? null : BigDecimal.valueOf(limitePermitido));
		faixa.getNaturezasImpedidas().add(naturezaImpedida);
		return this;
	}

	public FaixaDeValorBuilder exclusivaCliente() {
		faixa.setBlExclusivaCliente(true);
		return this;
	}

	public FaixaDeValor build() {
		return faixa;
	}

}
