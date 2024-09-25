package com.mercurio.lms.municipios.model;

public class UnidadeFederativaBuilder {

	public static UnidadeFederativaBuilder newUnidadeFederativa() {
		return new UnidadeFederativaBuilder();
	}

	private UnidadeFederativa uf;

	private UnidadeFederativaBuilder() {
		uf = new UnidadeFederativa();
	}

	public UnidadeFederativaBuilder id(long id) {
		uf.setIdUnidadeFederativa(id);
		return this;
	}

	public UnidadeFederativaBuilder sigla(String sigla) {
		uf.setSgUnidadeFederativa(sigla);
		return this;
	}

	public UnidadeFederativaBuilder nome(String nome) {
		uf.setNmUnidadeFederativa(nome);
		return this;
	}

	public UnidadeFederativaBuilder pais(Pais pais) {
		uf.setPais(pais);
		return this;
	}

	public UnidadeFederativa build() {
		return uf;
	}

	public static UnidadeFederativa RS() {
		return buildUnidadeFederativa(1L, "RS", "Rio Grande do Sul");
	}

	public static UnidadeFederativa SC() {
		return buildUnidadeFederativa(2L, "SC", "Santa Catarina");
	}

	public static UnidadeFederativa PR() {
		return buildUnidadeFederativa(3L, "PR", "Paraná");
	}

	public static UnidadeFederativa SP() {
		return buildUnidadeFederativa(4L, "SP", "São Paulo");
	}

	public static UnidadeFederativa RJ() {
		return buildUnidadeFederativa(5L, "RJ", "Rio de Janeiro");
	}

	public static UnidadeFederativa MG() {
		return buildUnidadeFederativa(6L, "MG", "Minas Gerais");
	}

	private static UnidadeFederativa buildUnidadeFederativa(long id, String sigla, String nome) {
		return newUnidadeFederativa()
				.id(id)
				.sigla(sigla)
				.nome(nome)
				.pais(PaisBuilder.BRASIL())
				.build();
	}

}
