package com.mercurio.lms.rest.portaria.contract;

import com.mercurio.adsm.rest.BaseDTO;

public class Portaria extends BaseDTO {

	private String descricao;
	private boolean padrao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isPadrao() {
		return padrao;
	}

	public void setPadrao(boolean padrao) {
		this.padrao = padrao;
	}

}
