package com.mercurio.lms.rest.coleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class OcorrenciaColetaDTO extends BaseDTO{
	private static final long serialVersionUID = 1L;
	
	private String dsOcorrencia;
	private Short codigo;

	public OcorrenciaColetaDTO(Long id, Short codigo, String dsOcorrencia) {
		this.setId(id);
		this.setCodigo(codigo);
		this.setDsOcorrencia(new StringBuilder(codigo.toString()).append(" ").append(dsOcorrencia).toString());
	}

	public String getDsOcorrencia() {
		return dsOcorrencia;
	}

	public void setDsOcorrencia(String dsOcorrencia) {
		this.dsOcorrencia = dsOcorrencia;
	}

	public Short getCodigo() {
		return codigo;
	}

	public void setCodigo(Short codigo) {
		this.codigo = codigo;
	}
}
