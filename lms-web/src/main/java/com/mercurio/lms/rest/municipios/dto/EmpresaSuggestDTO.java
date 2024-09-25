package com.mercurio.lms.rest.municipios.dto;

public class EmpresaSuggestDTO {
	private Long idEmpresa;
	private String nrIdentificacao;
	private String nmPessoa;
	private String nmFantasia;
	private String nmMunicipio;
	private String sgUnidadeFederativa;
	
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getNrIdentificacao() {
		return nrIdentificacao;
	}
	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	public String getNmFantasia() {
		return nmFantasia;
	}
	public void setNmFantasia(String nmFantasia) {
		this.nmFantasia = nmFantasia;
	}
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}
	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}
}
