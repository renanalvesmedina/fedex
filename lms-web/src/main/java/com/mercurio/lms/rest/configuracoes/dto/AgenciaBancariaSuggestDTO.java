package com.mercurio.lms.rest.configuracoes.dto;


public class AgenciaBancariaSuggestDTO {
	private Long idAgenciaBancaria;
	
	private Short nrAgenciaBancaria;
	
	private String nmAgenciaBancaria;

	public Long getIdAgenciaBancaria() {
		return idAgenciaBancaria;
	}

	public void setIdAgenciaBancaria(Long idAgenciaBancaria) {
		this.idAgenciaBancaria = idAgenciaBancaria;
	}

	public Short getNrAgenciaBancaria() {
		return nrAgenciaBancaria;
	}

	public void setNrAgenciaBancaria(Short nrAgenciaBancaria) {
		this.nrAgenciaBancaria = nrAgenciaBancaria;
	}

	public String getNmAgenciaBancaria() {
		return nmAgenciaBancaria;
	}

	public void setNmAgenciaBancaria(String nmAgenciaBancaria) {
		this.nmAgenciaBancaria = nmAgenciaBancaria;
	}
}
