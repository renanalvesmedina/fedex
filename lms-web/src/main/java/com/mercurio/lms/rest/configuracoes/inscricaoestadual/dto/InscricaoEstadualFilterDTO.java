package com.mercurio.lms.rest.configuracoes.inscricaoestadual.dto;

import com.mercurio.adsm.rest.BaseFilterDTO;

public class InscricaoEstadualFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private Long idPessoa;

	public Long getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}
}
