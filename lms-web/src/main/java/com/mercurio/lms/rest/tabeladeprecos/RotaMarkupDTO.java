package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.rest.BaseDTO;

public class RotaMarkupDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idRotaPreco;
	private String descricao;
	
	public Long getIdRotaPreco() {
		return idRotaPreco;
	}
	public void setIdRotaPreco(Long idRotaPreco) {
		this.idRotaPreco = idRotaPreco;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
