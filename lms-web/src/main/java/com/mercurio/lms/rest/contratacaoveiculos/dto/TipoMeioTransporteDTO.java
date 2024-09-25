package com.mercurio.lms.rest.contratacaoveiculos.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class TipoMeioTransporteDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long idTipoMeioTransporte;

	private String dsTipoMeioTransporte;
	private DomainValue tpSituacao;

	public Long getIdTipoMeioTransporte() {
		return idTipoMeioTransporte;
	}

	public void setIdTipoMeioTransporte(Long idTipoMeioTransporte) {
		this.idTipoMeioTransporte = idTipoMeioTransporte;
	}

	public String getDsTipoMeioTransporte() {
		return dsTipoMeioTransporte;
	}

	public void setDsTipoMeioTransporte(String dsTipoMeioTransporte) {
		this.dsTipoMeioTransporte = dsTipoMeioTransporte;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
}