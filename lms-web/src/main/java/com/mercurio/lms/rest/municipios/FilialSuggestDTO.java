package com.mercurio.lms.rest.municipios;

import com.mercurio.adsm.rest.BaseDTO;

public class FilialSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String nmFilial;
	private String sgFilial;
	private String nmEmpresa;
	private Long idEmpresa;
	private Boolean isMatriz;

	public FilialSuggestDTO() {
	}

	public FilialSuggestDTO(Long idFilial, String nmFilial, String sgFilial) {
		super();
		this.setId(idFilial);
		this.nmFilial = nmFilial;
		this.sgFilial = sgFilial;
	}

	public FilialSuggestDTO(Long idFilial, String nmFilial, String sgFilial, String nmEmpresa, Long idEmpresa) {
		super();
		this.setId(idFilial);
		this.nmFilial = nmFilial;
		this.sgFilial = sgFilial;
		this.nmEmpresa = nmEmpresa;
		this.idEmpresa = idEmpresa;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}

	public Long getIdFilial() {
		return getId();
	}

	public void setIdFilial(Long idFilial) {
		setId(idFilial);
	}

	public String getNmFilial() {
		return nmFilial;
	}

	public void setNmFilial(String nmFilial) {
		this.nmFilial = nmFilial;
	}

	public String getNmEmpresa() {
		return nmEmpresa;
	}

	public void setNmEmpresa(String nmEmpresa) {
		this.nmEmpresa = nmEmpresa;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Boolean getIsMatriz() {
		return isMatriz;
	}

	public void setIsMatriz(Boolean isMatriz) {
		this.isMatriz = isMatriz;
	}

}
