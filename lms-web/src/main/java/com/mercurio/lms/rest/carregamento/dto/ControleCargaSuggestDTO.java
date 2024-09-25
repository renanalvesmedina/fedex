package com.mercurio.lms.rest.carregamento.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class ControleCargaSuggestDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private Long nrControleCarga;
	private DateTime dhGeracao;

	private String sgFilial;

	public Long getIdControleCarga() {
		return getId();
	}

	public void setIdControleCarga(Long idControleCarga) {
		setId(idControleCarga);
	}

	public Long getNrControleCarga() {
		return nrControleCarga;
	}

	public void setNrControleCarga(Long nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public String getSgFilial() {
		return sgFilial;
	}

	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
}