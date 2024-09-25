package com.mercurio.lms.configuracoes.model.param;

//FIXME Essa classe é Gambiarra
public class PesquisarCepParam {
	private String nrCep;	
	private String dsLogradouro;
	private String dsTipoLogradouro;
	private String nmBairro;
	private Long idUnidadeFederativa;
	private Long idMunicipio;
	private Long idPais;
	private String sgPais;
	private Boolean onlyActives;
	
	public String getNrCep() {
		return nrCep;
	}
	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	public String getDsLogradouro() {
		return dsLogradouro;
	}
	public void setDsLogradouro(String dsLogradouro) {
		this.dsLogradouro = dsLogradouro;
	}
	public String getDsTipoLogradouro() {
		return dsTipoLogradouro;
	}
	public void setDsTipoLogradouro(String dsTipoLogradouro) {
		this.dsTipoLogradouro = dsTipoLogradouro;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public Long getIdPais() {
		return idPais;
	}
	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}

	public Long getIdUnidadeFederativa() {
		return idUnidadeFederativa;
	}
	public void setIdUnidadeFederativa(Long idUnidadeFederativa) {
		this.idUnidadeFederativa = idUnidadeFederativa;
	}

	public String getNmBairro() {
		return nmBairro;
	}
	public void setNmBairro(String nmBairro) {
		this.nmBairro = nmBairro;
	}

	public void setSgPais(String sgPais) {
		this.sgPais = sgPais;
	}
	public String getSgPais() {
		return sgPais;
	}
	
	public Boolean getOnlyActives() {
		return onlyActives;
	}
	
	public void setOnlyActives(Boolean onlyActives) {
		this.onlyActives = onlyActives;
	}
	
}
