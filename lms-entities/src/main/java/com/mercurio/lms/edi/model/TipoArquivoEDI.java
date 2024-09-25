package com.mercurio.lms.edi.model;

import java.io.Serializable;

public class TipoArquivoEDI implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long 	idTipoArquivoEDI;
	
	private String 	nmTipoArquivoEDI;
	
	private String 	extTipoArquivoEDI;

	public Long getIdTipoArquivoEDI() {
		return idTipoArquivoEDI;
	}

	public void setIdTipoArquivoEDI(Long idTipoArquivoEDI) {
		this.idTipoArquivoEDI = idTipoArquivoEDI;
	}

	public String getNmTipoArquivoEDI() {
		return nmTipoArquivoEDI;
	}

	public void setNmTipoArquivoEDI(String nmTipoArquivoEDI) {
		this.nmTipoArquivoEDI = nmTipoArquivoEDI;
	}

	public String getExtTipoArquivoEDI() {
		return extTipoArquivoEDI;
	}

	public void setExtTipoArquivoEDI(String extTipoArquivoEDI) {
		this.extTipoArquivoEDI = extTipoArquivoEDI;
	}	
		
}
