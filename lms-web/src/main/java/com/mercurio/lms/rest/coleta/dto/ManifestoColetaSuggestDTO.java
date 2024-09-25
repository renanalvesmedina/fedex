package com.mercurio.lms.rest.coleta.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class ManifestoColetaSuggestDTO extends BaseDTO{
	private static final long serialVersionUID = 1L;
	
	private Long idManifestoColeta;
	private Integer nrManifesto;
	private DateTime dhGeracao;
	private String sgFilial;
	
	public Long getIdManifestoColeta() {
		return idManifestoColeta;
	}
	public void setIdManifestoColeta(Long idManifestoColeta) {
		this.idManifestoColeta = idManifestoColeta;
	}
	public Integer getNrManifesto() {
		return nrManifesto;
	}
	public void setNrManifesto(Integer nrManifesto) {
		this.nrManifesto = nrManifesto;
	}
	public DateTime getDhGeracao() {
		return dhGeracao;
	}
	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
}
