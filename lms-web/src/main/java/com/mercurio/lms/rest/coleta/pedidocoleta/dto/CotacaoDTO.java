package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class CotacaoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String sgfilialByIdFilialOrigem;
	private Integer nrCotacao;

	public String getSgfilialByIdFilialOrigem() {
		return sgfilialByIdFilialOrigem;
	}

	public void setSgfilialByIdFilialOrigem(String sgfilialByIdFilialOrigem) {
		this.sgfilialByIdFilialOrigem = sgfilialByIdFilialOrigem;
	}

	public Integer getNrCotacao() {
		return nrCotacao;
	}

	public void setNrCotacao(Integer nrCotacao) {
		this.nrCotacao = nrCotacao;
	}

}
