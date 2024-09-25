package com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class TipoDescontoRfcDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idTipoDescontoRfc;
	private String dsTipoDescontoRfc;

	public Long getIdTipoDescontoRfc() {
		return idTipoDescontoRfc;
	}

	public void setIdTipoDescontoRfc(Long idTipoDescontoRfc) {
		this.idTipoDescontoRfc = idTipoDescontoRfc;
	}

	public String getDsTipoDescontoRfc() {
		return dsTipoDescontoRfc;
	}

	public void setDsTipoDescontoRfc(String dsTipoDescontoRfc) {
		this.dsTipoDescontoRfc = dsTipoDescontoRfc;
	}

}
