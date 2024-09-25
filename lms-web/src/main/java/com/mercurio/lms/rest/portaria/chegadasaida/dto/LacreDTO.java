package com.mercurio.lms.rest.portaria.chegadasaida.dto;

import java.io.Serializable;

public class LacreDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idLacreControleCarga;
	private String nrLacres;
	
	public LacreDTO() {
	}

	public Long getIdLacreControleCarga() {
		return idLacreControleCarga;
	}

	public void setIdLacreControleCarga(Long idLacreControleCarga) {
		this.idLacreControleCarga = idLacreControleCarga;
	}

	public String getNrLacres() {
		return nrLacres;
	}

	public void setNrLacres(String nrLacres) {
		this.nrLacres = nrLacres;
	}

}
