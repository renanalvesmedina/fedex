package com.mercurio.lms.rest.tabeladeprecos.dto;

import java.io.Serializable;

public class TabelaPrecoDTO implements Serializable{
	private Long idTabelaPreco;
	private String tabelaPreco;

	public TabelaPrecoDTO() {
	}

	public Long getIdTabelaPreco() {
		return idTabelaPreco;
	}
	public void setIdTabelaPreco(Long idTabelaPreco) {
		this.idTabelaPreco = idTabelaPreco;
	}
	public String getTabelaPreco() {
		return tabelaPreco;
	}
	public void setTabelaPreco(String tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}
}
