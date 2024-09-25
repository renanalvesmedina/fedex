package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class TelefoneEnderecoDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String nrTelefone;
	private String nrDdd;

	public String getNrTelefone() {
		return nrTelefone;
	}

	public void setNrTelefone(String nrTelefone) {
		this.nrTelefone = nrTelefone;
	}

}
