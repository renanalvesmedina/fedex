package com.mercurio.lms.municipios.model;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

public class PaisBuilder {

	public static PaisBuilder newPais() {
		return new PaisBuilder();
	}
	
	private Pais pais;

	private PaisBuilder() {
		pais = new Pais();
	}

	public PaisBuilder id(long id) {
		pais.setIdPais(id);
		return this;
	}

	public PaisBuilder sigla(String sigla) {
		pais.setSgPais(sigla);
		return this;
	}

	public PaisBuilder nome(String nome) {
		pais.setNmPais(new VarcharI18n(nome));
		return this;
	}
	
	public Pais build() {
		return pais;
	}

	public static Pais BRASIL() {
		return buildPais(30L, "BRA", "Brasil");
	}

	public static Pais ESTADOS_UNIDOS() {
		return buildPais(61L, "USA", "Estados Unidos");
	}

	private static Pais buildPais(long id, String sigla, String nome) {
		return newPais()
				.id(id)
				.sigla(sigla)
				.nome(nome)
				.build();
	}

}
