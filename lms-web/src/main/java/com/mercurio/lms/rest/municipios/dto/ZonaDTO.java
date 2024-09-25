package com.mercurio.lms.rest.municipios.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class ZonaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idZona;
	private String dsZona;

	public ZonaDTO(){
	}
	
	public ZonaDTO(Long idZona, String dsZona) {
		super();
		this.idZona = idZona;
		this.dsZona = dsZona;
	}



	public Long getIdZona() {
		return idZona;
	}

	public void setIdZona(Long idZona) {
		this.idZona = idZona;
	}

	public String getDsZona() {
		return dsZona;
	}

	public void setDsZona(String nmZona) {
		this.dsZona = nmZona;
	}
	
	}
