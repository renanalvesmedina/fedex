package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.rest.BaseFilterDTO;

public class RotaMarkupFiltroDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private String textoBusca;
	private Long idTabelaPreco;
	private String tipo;

	public String getTextoBusca() {
		return textoBusca;
	}

	public void setTextoBusca(String textoBusca) {
		this.textoBusca = textoBusca;
	}

	public Long getIdTabelaPreco() {
		return idTabelaPreco;
	}

	public void setIdTabelaPreco(Long idTabelaPreco) {
		this.idTabelaPreco = idTabelaPreco;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
