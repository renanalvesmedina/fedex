package com.mercurio.lms.edi.dto;

import java.io.Serializable;

public class ClienteLayoutEDIDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String email;
	
	private String nmPasta;

	public ClienteLayoutEDIDTO(String email, String nmPasta) {
		this.email = email;
		this.nmPasta = nmPasta;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNmPasta() {
		return nmPasta;
	}

	public void setNmPasta(String nmPasta) {
		this.nmPasta = nmPasta;
	}
}
