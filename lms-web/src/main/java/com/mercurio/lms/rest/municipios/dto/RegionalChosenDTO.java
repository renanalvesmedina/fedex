package com.mercurio.lms.rest.municipios.dto;

public class RegionalChosenDTO {
	private Long idRegional;
	private String dsRegional;
	
	public RegionalChosenDTO() {}
	
	public RegionalChosenDTO(Long idRegional, String dsRegional) {
		this.idRegional = idRegional;
		this.dsRegional = dsRegional;
	}
	public Long getIdRegional() {
		return idRegional;
	}
	public void setIdRegional(Long idRegional) {
		this.idRegional = idRegional;
	}

	public String getDsRegional() {
		return dsRegional;
	}

	public void setDsRegional(String dsRegional) {
		this.dsRegional = dsRegional;
	}
}
