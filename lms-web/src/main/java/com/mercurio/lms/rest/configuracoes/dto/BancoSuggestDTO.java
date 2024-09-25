package com.mercurio.lms.rest.configuracoes.dto;
 
public class BancoSuggestDTO { 

	private Long idBanco;
	
	private Short nrBanco;
	
	private String nmBanco;

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public Short getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(Short nrBanco) {
		this.nrBanco = nrBanco;
	}

	public String getNmBanco() {
		return nmBanco;
	}

	public void setNmBanco(String nmBanco) {
		this.nmBanco = nmBanco;
	}

} 
