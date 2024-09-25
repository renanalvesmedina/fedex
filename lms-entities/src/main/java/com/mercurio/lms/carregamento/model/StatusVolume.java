package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

public class StatusVolume implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idStatusVolume;
	private Long codigoStatus;
	private String nomeStatus;
	private String descricaoStatus;
	
	public StatusVolume(Long id) {
		this.idStatusVolume = id;
	}

	public StatusVolume() {
	}

	public Long getIdStatusVolume() {
		return idStatusVolume;
	}

	public void setIdStatusVolume(Long idStatusVolume) {
		this.idStatusVolume = idStatusVolume;
	}

	public Long getCodigoStatus() {
		return codigoStatus;
	}

	public void setCodigoStatus(Long codigoStatus) {
		this.codigoStatus = codigoStatus;
	}

	public String getNomeStatus() {
		return nomeStatus;
	}

	public void setNomeStatus(String nomeStatus) {
		this.nomeStatus = nomeStatus;
	}

	public String getDescricaoStatus() {
		return descricaoStatus;
	}

	public void setDescricaoStatus(String descricaoStatus) {
		this.descricaoStatus = descricaoStatus;
	}

}
