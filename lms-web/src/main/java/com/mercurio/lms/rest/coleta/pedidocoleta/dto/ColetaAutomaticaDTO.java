package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ColetaAutomaticaDTO extends BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tpDiaSemana;
	private String hrChegada;
	private String hrSaida;
	
	public String getTpDiaSemana() {
		return tpDiaSemana;
	}
	public void setTpDiaSemana(String tpDiaSemana) {
		this.tpDiaSemana = tpDiaSemana;
	}
	public String getHrChegada() {
		return hrChegada;
	}
	public void setHrChegada(String hrChegada) {
		this.hrChegada = hrChegada;
	}
	public String getHrSaida() {
		return hrSaida;
	}
	public void setHrSaida(String hrSaida) {
		this.hrSaida = hrSaida;
	}
	
}
