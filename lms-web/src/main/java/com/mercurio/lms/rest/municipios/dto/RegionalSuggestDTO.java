package com.mercurio.lms.rest.municipios.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class RegionalSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String sgRegional;
	private String dsRegional;
	
	public RegionalSuggestDTO() {
	}

	public RegionalSuggestDTO(Long idRegional, String sgRegional, String dsRegional) {
		super();
		setId(idRegional);
		this.sgRegional = sgRegional;
		this.dsRegional = dsRegional;
	}

	public Long getIdRegional() {
		return getId();
	}

	public void setIdRegional(Long idRegional) {
		setId(idRegional);
	}

	public String getSgRegional() {
		return sgRegional;
	}

	public void setSgRegional(String sgRegional) {
		this.sgRegional = sgRegional;
	}

	public String getDsRegional() {
		return dsRegional;
	}

	public void setDsRegional(String dsRegional) {
		this.dsRegional = dsRegional;
	}

}
