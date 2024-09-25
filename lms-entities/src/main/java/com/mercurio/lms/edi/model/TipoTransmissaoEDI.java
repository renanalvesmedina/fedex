package com.mercurio.lms.edi.model;

import java.io.Serializable;

public class TipoTransmissaoEDI implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idTipoTransmissaoEDI;
	
	private String nome;

	public Long getIdTipoTransmissaoEDI() {
		return idTipoTransmissaoEDI;
	}

	public void setIdTipoTransmissaoEDI(Long idTipoTransmissaoEDI) {
		this.idTipoTransmissaoEDI = idTipoTransmissaoEDI;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
