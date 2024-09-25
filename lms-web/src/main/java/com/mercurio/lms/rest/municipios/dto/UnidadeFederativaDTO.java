package com.mercurio.lms.rest.municipios.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class UnidadeFederativaDTO extends BaseDTO{

	private static final long serialVersionUID = 1L;
	
	private Long idUnidadeFederativa;
	private String sgUnidadeFederativa;
	private String nmUnidadeFederativa;
	
	public UnidadeFederativaDTO(){
		
	}
	
	public UnidadeFederativaDTO(Long idUnidadeFederativa,
			String sgUnidadeFederativa, String nmUnidadeFederativa) {
		super();
		this.idUnidadeFederativa = idUnidadeFederativa;
		this.sgUnidadeFederativa = sgUnidadeFederativa;
		this.nmUnidadeFederativa = nmUnidadeFederativa;
	}
	
	public Long getIdUnidadeFederativa() {
		return idUnidadeFederativa;
	}
	public void setIdUnidadeFederativa(Long idUnidadeFederativa) {
		this.idUnidadeFederativa = idUnidadeFederativa;
	}
	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}
	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}
	public String getNmUnidadeFederativa() {
		return nmUnidadeFederativa;
	}
	public void setNmUnidadeFederativa(String nmUnidadeFederativa) {
		this.nmUnidadeFederativa = nmUnidadeFederativa;
	}
}
