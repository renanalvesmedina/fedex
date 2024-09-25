package com.mercurio.lms.vendas.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

public class DadosEmbarquePipelineDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long nrConhecimento;
	private DateTime dhEmissao;
	private String sgFilial;
	
	public Long getNrConhecimento() {
		return nrConhecimento;
	}
	public void setNrConhecimento(Long nrConhecimento) {
		this.nrConhecimento = nrConhecimento;
	}
	public DateTime getDhEmissao() {
		return dhEmissao;
	}
	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public String toFormarttedConhecimento() {
		return sgFilial + " - " + nrConhecimento;
	}
	
}
