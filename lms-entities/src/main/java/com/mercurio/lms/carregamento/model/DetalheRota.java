package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

public class DetalheRota implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idDetalheRota;
	private Long idRotaEmbarque;
	private String siglaRota;
	private String nomeRota;
	
	public DetalheRota(Long id) {
		this.idDetalheRota = id;
	}

	public DetalheRota() {
	}

	public Long getIdDetalheRota() {
		return idDetalheRota;
	}

	public void setIdDetalheRota(Long idDetalheRota) {
		this.idDetalheRota = idDetalheRota;
	}

	public Long getIdRotaEmbarque() {
		return idRotaEmbarque;
	}

	public void setIdRotaEmbarque(Long idRotaEmbarque) {
		this.idRotaEmbarque = idRotaEmbarque;
	}

	public String getSiglaRota() {
		return siglaRota;
	}

	public void setSiglaRota(String siglaRota) {
		this.siglaRota = siglaRota;
	}

	public String getNomeRota() {
		return nomeRota;
}

	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
	}
}
