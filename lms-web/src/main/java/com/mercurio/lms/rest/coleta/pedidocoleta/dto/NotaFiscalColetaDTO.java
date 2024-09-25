package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class NotaFiscalColetaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idNotaFiscalColeta;
	private Integer nrNotaFiscal;
	private String nrChave;

	public Long getIdNotaFiscalColeta() {
		return idNotaFiscalColeta;
	}

	public void setIdNotaFiscalColeta(Long idNotaFiscalColeta) {
		this.idNotaFiscalColeta = idNotaFiscalColeta;
	}

	public Integer getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

}
