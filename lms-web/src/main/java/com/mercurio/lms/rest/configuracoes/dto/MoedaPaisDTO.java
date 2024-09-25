package com.mercurio.lms.rest.configuracoes.dto;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.MoedaDTO;

public class MoedaPaisDTO extends BaseDTO {
	private static final long serialVersionUID = 7107956235457178669L;

	private Long idMoedaPais;
	private MoedaDTO moeda;

	public Long getIdMoedaPais() {
		return idMoedaPais;
	}

	public void setIdMoedaPais(Long idMoedaPais) {
		this.idMoedaPais = idMoedaPais;
	}

	public MoedaDTO getMoeda() {
		return moeda;
	}

	public void setMoeda(MoedaDTO moeda) {
		this.moeda = moeda;
	}
}