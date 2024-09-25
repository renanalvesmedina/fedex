package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import javax.persistence.Transient;

import org.joda.time.TimeOfDay;

public class RotaEmbarque implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idRotaEmbarque;
	private String nomeRota;
	private String siglaRota;
	private TimeOfDay horarioCorte;
	
	@Transient
	private String siglaNomeRota;
	
	public String getSiglaNomeRota() {
		return this.siglaRota + " - " + this.nomeRota;
	}

	public RotaEmbarque(Long id) {
		this.idRotaEmbarque = id;
	}

	public RotaEmbarque() {
	}

	public Long getIdRotaEmbarque() {
		return idRotaEmbarque;
	}

	public void setIdRotaEmbarque(Long idRotaEmbarque) {
		this.idRotaEmbarque = idRotaEmbarque;
	}

	public String getNomeRota() {
		return nomeRota;
	}

	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
	}

	public String getSiglaRota() {
		return siglaRota;
	}

	public void setSiglaRota(String siglaRota) {
		this.siglaRota = siglaRota;
	}

	public TimeOfDay getHorarioCorte() {
		return horarioCorte;
	}

	public void setHorarioCorte(TimeOfDay horarioCorte) {
		this.horarioCorte = horarioCorte;
	}
}
