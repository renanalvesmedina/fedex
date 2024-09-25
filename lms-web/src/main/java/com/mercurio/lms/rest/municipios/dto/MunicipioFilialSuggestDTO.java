package com.mercurio.lms.rest.municipios.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class MunicipioFilialSuggestDTO extends BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nmMunicipio;
	private String sgUnidadeFederativa;
	private String sgFilial;
	private Long idEmpresa;
	private Long idMunicipioFilial;
	private Long idMunicipio;
	private Long idFilial;
	private String nmFantasia;
	
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}
	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Long getIdMunicipioFilial() {
		return idMunicipioFilial;
	}
	public void setIdMunicipioFilial(Long idMunicipioFilial) {
		this.idMunicipioFilial = idMunicipioFilial;
	}
	public Long getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public Long getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
	public String getNmFantasia() {
		return nmFantasia;
	}
	public void setNmFantasia(String nmFantasia) {
		this.nmFantasia = nmFantasia;
	}

}
