package com.mercurio.lms.rest.entrega.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class ManifestoEntregaSuggestDTO extends BaseDTO{
	private static final long serialVersionUID = 1L;
	
	private Long idManifestoEntrega;
	private String sgFilial;
	private DateTime dhEmissao;
	private Integer nrManifestoEntrega;
	
	public Long getIdManifestoEntrega() {
		return idManifestoEntrega;
	}
	public void setIdManifestoEntrega(Long idManifestoEntrega) {
		this.idManifestoEntrega = idManifestoEntrega;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public DateTime getDhEmissao() {
		return dhEmissao;
	}
	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}
	public Integer getNrManifestoEntrega() {
		return nrManifestoEntrega;
	}
	public void setNrManifestoEntrega(Integer nrManifestoEntrega) {
		this.nrManifestoEntrega = nrManifestoEntrega;
	}
	
}
