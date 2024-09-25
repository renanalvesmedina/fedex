package com.mercurio.lms.rest.municipios.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class PaisDTO extends BaseDTO{
	
	private static final long serialVersionUID = 1L;
	
	private Long idPais;
	private Integer cdIso;
	private String sgPais;
	private String nmPais;
	
	public PaisDTO() {
	}
	
	public PaisDTO(Long idPais, Integer cdIso, String sgPais, String nmPais) {
		super();
		this.idPais = idPais;
		this.cdIso = cdIso;
		this.sgPais = sgPais;
		this.nmPais = nmPais;
	}

	public Long getIdPais() {
		return idPais;
	}
	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}
	public Integer getCdIso() {
		return cdIso;
	}
	public void setCdIso(Integer cdIso) {
		this.cdIso = cdIso;
	}
	public String getSgPais() {
		return sgPais;
	}
	public void setSgPais(String sgPais) {
		this.sgPais = sgPais;
	}
	public String getNmPais() {
		return nmPais;
	}
	public void setNmPais(String nmPais) {
		this.nmPais = nmPais;
	}
}
