package com.mercurio.lms.rest.carregamento.dto;

public class ControleCargaSuggestFilterDTO {
	private String value;
	private Long idEmpresa;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
}
