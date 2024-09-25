package com.mercurio.lms.seguros.model;

import java.math.BigDecimal;

public class ApoliceSeguroBuilder {

	public static ApoliceSeguroBuilder newApoliceSeguro() {
		return new ApoliceSeguroBuilder();
	}

	private ApoliceSeguro apolice;

	private ApoliceSeguroBuilder() {
		apolice = new ApoliceSeguro();
	}

	public ApoliceSeguroBuilder numero(String numero) {
		apolice.setNrApolice(numero);
		return this;
	}

	public ApoliceSeguroBuilder limiteApolice(double valor) {
		apolice.setVlLimiteApolice(BigDecimal.valueOf(valor));
		return this;
	}

	public ApoliceSeguroBuilder tipo(long idTipoSeguro) {
		TipoSeguro tipoSeguro = new TipoSeguro();
		tipoSeguro.setIdTipoSeguro(idTipoSeguro);
		apolice.setTipoSeguro(tipoSeguro);
		return this;
	}

	public ApoliceSeguroBuilder seguradora(long idSeguradora) {
		Seguradora seguradora = new Seguradora();
		seguradora.setIdSeguradora(idSeguradora);
		apolice.setSeguradora(seguradora);
		return this;
	}

	public ApoliceSeguroBuilder limiteControleCarga(double valor) {
		apolice.setVlLimiteControleCarga(BigDecimal.valueOf(valor));
		return this;
	}

	public ApoliceSeguro build() {
		return apolice;
	}

}
