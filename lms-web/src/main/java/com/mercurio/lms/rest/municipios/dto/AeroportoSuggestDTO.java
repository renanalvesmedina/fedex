package com.mercurio.lms.rest.municipios.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class AeroportoSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String sgAeroporto;
	private String nmAeroporto;

	public AeroportoSuggestDTO() {
	}

	public AeroportoSuggestDTO(Long idAeroporto, String sgAeroporto, String nmAeroporto) {
		super();
		setId(idAeroporto);
		this.sgAeroporto = sgAeroporto;
		this.nmAeroporto = nmAeroporto;
	}

	public Long getIdAeroporto() {
		return getId();
	}

	public void setIdAeroporto(Long idAeroporto) {
		setId(idAeroporto);
	}

	public String getSgAeroporto() {
		return sgAeroporto;
	}

	public void setSgAeroporto(String sgAeroporto) {
		this.sgAeroporto = sgAeroporto;
	}

	public String getNmAeroporto() {
		return nmAeroporto;
	}

	public void setNmAeroporto(String nmAeroporto) {
		this.nmAeroporto = nmAeroporto;
	}

}
