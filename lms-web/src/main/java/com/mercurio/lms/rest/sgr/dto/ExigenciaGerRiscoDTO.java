package com.mercurio.lms.rest.sgr.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class ExigenciaGerRiscoDTO extends BaseDTO {
	
	private static final long serialVersionUID = 1L;

	String dsResumida;

	public String getDsResumida() {
		return dsResumida;
	}

	public void setDsResumida(String dsResumida) {
		this.dsResumida = dsResumida;
	}
}
