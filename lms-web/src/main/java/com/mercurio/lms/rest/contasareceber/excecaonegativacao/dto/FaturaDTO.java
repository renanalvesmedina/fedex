package com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class FaturaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	private String nrFatura;

	public FaturaDTO() {
	}

	public FaturaDTO(Long idFatura, String nrFatura) {
		super();
		this.setId(idFatura);
		this.nrFatura = nrFatura;
	}

	public String getNrFatura() {
		return nrFatura;
	}
	public void setNrFatura(String nrFatura) {
		this.nrFatura = nrFatura;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
