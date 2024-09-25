package com.mercurio.lms.rest.contratacaoveiculos.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class MeioTransporteSuggestDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private String nrIdentificador;
	private String nrFrota;

	private Long idModeloMeioTransporte;
	private String dsModeloMeioTransporte;

	private Long idMarcaMeioTransporte;
	private String dsMarcaMeioTransporte;
	
	public Long getIdMeioTransporte() {
		return getId();
	}

	public void setIdMeioTransporte(Long idMeioTransporte) {
		setId(idMeioTransporte);
	}

	public String getNrIdentificador() {
		return nrIdentificador;
	}

	public void setNrIdentificador(String nrIdentificador) {
		this.nrIdentificador = nrIdentificador;
	}

	public String getNrFrota() {
		return nrFrota;
	}

	public void setNrFrota(String nrFrota) {
		this.nrFrota = nrFrota;
	}

	public Long getIdModeloMeioTransporte() {
		return idModeloMeioTransporte;
	}

	public void setIdModeloMeioTransporte(Long idModeloMeioTransporte) {
		this.idModeloMeioTransporte = idModeloMeioTransporte;
	}

	public String getDsModeloMeioTransporte() {
		return dsModeloMeioTransporte;
	}

	public void setDsModeloMeioTransporte(String dsModeloMeioTransporte) {
		this.dsModeloMeioTransporte = dsModeloMeioTransporte;
	}

	public Long getIdMarcaMeioTransporte() {
		return idMarcaMeioTransporte;
	}

	public void setIdMarcaMeioTransporte(Long idMarcaMeioTransporte) {
		this.idMarcaMeioTransporte = idMarcaMeioTransporte;
	}

	public String getDsMarcaMeioTransporte() {
		return dsMarcaMeioTransporte;
	}

	public void setDsMarcaMeioTransporte(String dsMarcaMeioTransporte) {
		this.dsMarcaMeioTransporte = dsMarcaMeioTransporte;
	}
}