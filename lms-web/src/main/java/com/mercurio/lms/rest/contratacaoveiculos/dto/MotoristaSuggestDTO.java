package com.mercurio.lms.rest.contratacaoveiculos.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class MotoristaSuggestDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String nrIdentificacao;
	private String nmPessoa;

	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}

}
