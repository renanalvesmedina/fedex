package com.mercurio.lms.rest.coleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class OcorrenciaEntregaDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	
	private String dsOcorrenciaEntrega;
	private Short cdOcorrenciaEntrega;
	
	public OcorrenciaEntregaDTO() {
	}
	
	public OcorrenciaEntregaDTO(Long id, Short cdOcorrenciaEntrega, String dsOcorrenciaEntrega) {
		this.setId(id);
		this.setCdOcorrenciaEntrega(cdOcorrenciaEntrega);
		this.setDsOcorrenciaEntrega(new StringBuilder(cdOcorrenciaEntrega.toString()).append(" ").append(dsOcorrenciaEntrega).toString());
	}
	
	public String getDsOcorrenciaEntrega() {
		return dsOcorrenciaEntrega;
	}
	public void setDsOcorrenciaEntrega(String dsOcorrenciaEntrega) {
		this.dsOcorrenciaEntrega = dsOcorrenciaEntrega;
	}
	public Short getCdOcorrenciaEntrega() {
		return cdOcorrenciaEntrega;
	}
	public void setCdOcorrenciaEntrega(Short cdOcorrenciaEntrega) {
		this.cdOcorrenciaEntrega = cdOcorrenciaEntrega;
	}
}
