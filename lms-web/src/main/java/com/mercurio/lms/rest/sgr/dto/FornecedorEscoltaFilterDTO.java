package com.mercurio.lms.rest.sgr.dto;

import com.mercurio.adsm.rest.BaseFilterDTO;

public class FornecedorEscoltaFilterDTO extends BaseFilterDTO {

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
