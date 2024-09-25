package com.mercurio.lms.rest.municipios.dto;

import java.io.Serializable;

public class PaisSuggestFilterDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String value;
	
	private Long idZona;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getIdZona() {
		return idZona;
	}

	public void setIdZona(Long idZona) {
		this.idZona = idZona;
	}
	
}
