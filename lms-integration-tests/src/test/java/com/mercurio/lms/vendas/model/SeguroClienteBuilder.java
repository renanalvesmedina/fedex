package com.mercurio.lms.vendas.model;

import java.math.BigDecimal;

import com.mercurio.lms.seguros.model.Seguradora;
import com.mercurio.lms.seguros.model.TipoSeguro;

public class SeguroClienteBuilder {

	public static SeguroClienteBuilder newSeguroCliente() {
		return new SeguroClienteBuilder();
	}

	private SeguroCliente seguro;

	private SeguroClienteBuilder() {
		seguro = new SeguroCliente();
	}

	public SeguroClienteBuilder limiteApolice(double valor) {
		seguro.setVlLimite(BigDecimal.valueOf(valor));
		return this;
	}

	public SeguroClienteBuilder numero(String numero) {
		seguro.setDsApolice(numero);
		return this;
	}

	public SeguroClienteBuilder tipo(long idTipoSeguro) {
		TipoSeguro tipoSeguro = new TipoSeguro();
		tipoSeguro.setIdTipoSeguro(idTipoSeguro);
		seguro.setTipoSeguro(tipoSeguro);
		return this;
	}

	public SeguroClienteBuilder seguradora(long idSeguradora) {
		Seguradora seguradora = new Seguradora();
		seguradora.setIdSeguradora(idSeguradora);
		seguro.setSeguradora(seguradora);
		return this;
	}

	public SeguroClienteBuilder limiteControleCarga(double valor) {
		seguro.setVlLimiteControleCarga(BigDecimal.valueOf(valor));
		return this;
	}

	public SeguroCliente build() {
		return seguro;
	}

}
