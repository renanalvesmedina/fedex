package com.mercurio.lms.rest.municipios.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class RotaColetaEntregaSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Short nrRota;
	private String dsRota;
	private Integer nrKm;

	public RotaColetaEntregaSuggestDTO() {

	}

	public Long getIdRotaColetaEntrega() {
		return getId();
	}

	public void setIdRotaColetaEntrega(Long idRotaColetaEntrega) {
		setId(idRotaColetaEntrega);
	}

	public Short getNrRota() {
		return nrRota;
	}

	public void setNrRota(Short nrRota) {
		this.nrRota = nrRota;
	}

	public String getDsRota() {
		return dsRota;
	}

	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}

	public Integer getNrKm() {
		return nrKm;
	}

	public void setNrKm(Integer nrKm) {
		this.nrKm = nrKm;
	}

}