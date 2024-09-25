package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class RotaColetaEntregaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Short nrRota;
	private String dsRota;

	public Short getNrRota() {
		return nrRota;
	}

	public void setNrRota(Short nrRota) {
		this.nrRota = nrRota;
	}

	public String getDsRota() {
		return dsRota;
	}

	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}

}
