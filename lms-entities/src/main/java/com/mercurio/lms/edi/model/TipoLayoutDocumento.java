package com.mercurio.lms.edi.model;

import java.io.Serializable;

public class TipoLayoutDocumento implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idTipoLayoutDocumento;
	
	private String dsTipoLayoutDocumento;

	public Long getIdTipoLayoutDocumento() {
		return idTipoLayoutDocumento;
	}

	public void setIdTipoLayoutDocumento(Long idTipoLayoutDocumento) {
		this.idTipoLayoutDocumento = idTipoLayoutDocumento;
	}

	public String getDsTipoLayoutDocumento() {
		return dsTipoLayoutDocumento;
	}

	public void setDsTipoLayoutDocumento(String dsTipoLayoutDocumento) {
		this.dsTipoLayoutDocumento = dsTipoLayoutDocumento;
	}
	
}
