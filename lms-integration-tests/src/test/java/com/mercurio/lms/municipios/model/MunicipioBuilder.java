package com.mercurio.lms.municipios.model;

public class MunicipioBuilder {

	private Municipio municipio;

	private MunicipioBuilder() {
		municipio = new Municipio();
	}

	public static MunicipioBuilder newMunicipio() {
		return new MunicipioBuilder();
	}

	public MunicipioBuilder id(long idMunicipio) {
		municipio.setIdMunicipio(idMunicipio);
		return this;
	}

	public MunicipioBuilder nome(String nome) {
		municipio.setNmMunicipio(nome);
		return this;
	}

	public MunicipioBuilder unidadeFederativa(UnidadeFederativa uf) {
		municipio.setUnidadeFederativa(uf);
		return this;
	}

	public Municipio build() {
		return municipio;
	}

	public static Municipio PORTO_ALEGRE() {
		return buildMunicipio(2114L, "Porto Alegre", UnidadeFederativaBuilder.RS());
	}

	public static Municipio SAO_PAULO() {
		return buildMunicipio(2570L, "São Paulo", UnidadeFederativaBuilder.SP());
	}

	public static Municipio CANOAS() {
		return buildMunicipio(220L, "Canoas", UnidadeFederativaBuilder.RS());
	}

	public static Municipio CURITIBA() {
		return buildMunicipio(542L, "Curitiba", UnidadeFederativaBuilder.PR());
	}

	public static Municipio UBERLANDIA() {
		return buildMunicipio(671L, "Uberlândia", UnidadeFederativaBuilder.MG());
	}

	private static Municipio buildMunicipio(long id, String nome, UnidadeFederativa uf) {
		return newMunicipio()
				.id(id)
				.nome(nome)
				.unidadeFederativa(uf)
				.build();
	}

}
