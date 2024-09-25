package com.mercurio.lms.rest.tabeladeprecos;

import com.mercurio.adsm.rest.BaseDTO;

public class TarifaMarkupDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idTarifaPreco;
	private String cdTarifaPreco;
	
	public Long getIdTarifaPreco() {
		return idTarifaPreco;
	}
	public void setIdTarifaPreco(Long idTarifaPreco) {
		this.idTarifaPreco = idTarifaPreco;
	}
	public String getCdTarifaPreco() {
		return cdTarifaPreco;
	}
	public void setCdTarifaPreco(String cdTarifaPreco) {
		this.cdTarifaPreco = cdTarifaPreco;
	}

}
