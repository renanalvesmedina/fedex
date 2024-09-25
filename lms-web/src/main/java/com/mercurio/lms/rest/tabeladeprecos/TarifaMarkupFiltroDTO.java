package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.rest.BaseFilterDTO;

public class TarifaMarkupFiltroDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private String cdTarifaPreco;
	private Long idTabelaPreco;
	private String tipo;

	public String getCdTarifaPreco() {
		return cdTarifaPreco;
	}

	public void setCdTarifaPreco(String cdTarifaPreco) {
		this.cdTarifaPreco = cdTarifaPreco;
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
