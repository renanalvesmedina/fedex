package com.mercurio.lms.rest.expedicao.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class AwbOcorrenciaPendenciaDTO extends BaseDTO{
	private static final long serialVersionUID = 1L;
	
	private Long idAwb;
	private Long idCiaAerea;
	private Long idOcorrenciaPendencia;
	private Long idUsuarioOcorrencia;
	private DateTime dhOcorrenciaPendencia;
	private String ocorrencia;
	private String tpOcorrencia;
	private String usuarioOcorrencia;
	
	public Long getIdAwb() {
		return idAwb;
	}
	public void setIdAwb(Long idAwb) {
		this.idAwb = idAwb;
	}
	public Long getIdCiaAerea() {
		return idCiaAerea;
	}
	public void setIdCiaAerea(Long idCiaAerea) {
		this.idCiaAerea = idCiaAerea;
	}
	public Long getIdOcorrenciaPendencia() {
		return idOcorrenciaPendencia;
	}
	public void setIdOcorrenciaPendencia(Long idOcorrenciaPendencia) {
		this.idOcorrenciaPendencia = idOcorrenciaPendencia;
	}
	public Long getIdUsuarioOcorrencia() {
		return idUsuarioOcorrencia;
	}
	public void setIdUsuarioOcorrencia(Long idUsuarioOcorrencia) {
		this.idUsuarioOcorrencia = idUsuarioOcorrencia;
	}
	public DateTime getDhOcorrenciaPendencia() {
		return dhOcorrenciaPendencia;
	}
	public void setDhOcorrenciaPendencia(DateTime dhOcorrenciaPendencia) {
		this.dhOcorrenciaPendencia = dhOcorrenciaPendencia;
	}
	public String getOcorrencia() {
		return ocorrencia;
	}
	public void setOcorrencia(String ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
	public String getTpOcorrencia() {
		return tpOcorrencia;
	}
	public void setTpOcorrencia(String tpOcorrencia) {
		this.tpOcorrencia = tpOcorrencia;
	}
	public String getUsuarioOcorrencia() {
		return usuarioOcorrencia;
	}
	public void setUsuarioOcorrencia(String usuarioOcorrencia) {
		this.usuarioOcorrencia = usuarioOcorrencia;
	}
}
