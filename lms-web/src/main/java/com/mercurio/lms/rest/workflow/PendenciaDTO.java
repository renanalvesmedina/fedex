package com.mercurio.lms.rest.workflow;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class PendenciaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idPendencia;
	private String dsPendencia;
	private DomainValue tpSituacaoPendencia;

	public Long getIdPendencia() {
		return idPendencia;
	}

	public void setIdPendencia(Long idPendencia) {
		this.idPendencia = idPendencia;
	}

	public String getDsPendencia() {
		return dsPendencia;
	}

	public void setDsPendencia(String dsPendencia) {
		this.dsPendencia = dsPendencia;
	}

	public DomainValue getTpSituacaoPendencia() {
		return tpSituacaoPendencia;
	}

	public void setTpSituacaoPendencia(DomainValue tpSituacaoPendencia) {
		this.tpSituacaoPendencia = tpSituacaoPendencia;
	}

}