package com.mercurio.lms.municipios.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class MunicipioDTO extends BaseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idMunicipio;
	private String nmMunicipio;
	
	public MunicipioDTO(){
		
	}
	 
	public MunicipioDTO(Long idMunicipio, String nmMunicipio) {
		super();
		this.idMunicipio = idMunicipio;
		this.nmMunicipio = nmMunicipio;
	}
	
	public Long getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	 
	 
}
