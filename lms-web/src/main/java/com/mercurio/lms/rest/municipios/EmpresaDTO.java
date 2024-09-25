package com.mercurio.lms.rest.municipios;

import com.mercurio.adsm.rest.BaseDTO;

public class EmpresaDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;

	private Long idEmpresa;
	
	private String sgUnidadeFederativa;
	
	private String nmMunicipio;
	
	private String nmPessoa;
	
	private String nmFantasia;
	
	private String nrIdentificacao;

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}

	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}

	public String getNmMunicipio() {
		return nmMunicipio;
	}

	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
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

	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}
	
}
