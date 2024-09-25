package com.mercurio.lms.rest.entrega.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class EntregaNotaFiscalDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idEntregaNotaFiscal;
	private Long idNotaFiscal;
	private Long idDoctoServico;
	private Long idManifesto;
	private String tpManifesto;
	private Long idOcorrenciaEntrega;
	private Long idOcorrenciaEntregaAnterior;
	private Integer qtVolumesEntregues;
	private DateTime dhOcorrencia;
	private String dhOcorrenciaString;
	private String dsOcorrenciaEntrega;
	private Short cdOcorrenciaEntrega;
	private String usuario;
	
	public Long getIdEntregaNotaFiscal() {
		return idEntregaNotaFiscal;
	}
	public void setIdEntregaNotaFiscal(Long idEntregaNotaFiscal) {
		this.idEntregaNotaFiscal = idEntregaNotaFiscal;
	}
	public Integer getQtVolumesEntregues() {
		return qtVolumesEntregues;
	}
	public void setQtVolumesEntregues(Integer qtVolumesEntregues) {
		this.qtVolumesEntregues = qtVolumesEntregues;
	}
	public DateTime getDhOcorrencia() {
		return dhOcorrencia;
	}
	public void setDhOcorrencia(DateTime dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}
	public String getDsOcorrenciaEntrega() {
		return dsOcorrenciaEntrega;
	}
	public void setDsOcorrenciaEntrega(String dsOcorrenciaEntrega) {
		this.dsOcorrenciaEntrega = dsOcorrenciaEntrega;
	}
	public Long getIdOcorrenciaEntrega() {
		return idOcorrenciaEntrega;
	}
	public void setIdOcorrenciaEntrega(Long idOcorrenciaEntrega) {
		this.idOcorrenciaEntrega = idOcorrenciaEntrega;
	}
	public Short getCdOcorrenciaEntrega() {
		return cdOcorrenciaEntrega;
	}
	public void setCdOcorrenciaEntrega(Short cdOcorrenciaEntrega) {
		this.cdOcorrenciaEntrega = cdOcorrenciaEntrega;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public Long getIdNotaFiscal() {
		return idNotaFiscal;
	}
	public void setIdNotaFiscal(Long idNotaFiscal) {
		this.idNotaFiscal = idNotaFiscal;
	}
	public Long getIdDoctoServico() {
		return idDoctoServico;
	}
	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}
	public Long getIdManifesto() {
		return idManifesto;
	}
	public void setIdManifesto(Long idManifesto) {
		this.idManifesto = idManifesto;
	}
	public String getTpManifesto() {
		return tpManifesto;
	}
	public void setTpManifesto(String tpManifesto) {
		this.tpManifesto = tpManifesto;
	}
	public String getDhOcorrenciaString() {
		return dhOcorrenciaString;
	}
	public void setDhOcorrenciaString(String dhOcorrenciaString) {
		this.dhOcorrenciaString = dhOcorrenciaString;
	}
	public Long getIdOcorrenciaEntregaAnterior() {
		return idOcorrenciaEntregaAnterior;
	}
	public void setIdOcorrenciaEntregaAnterior(Long idOcorrenciaEntregaAnterior) {
		this.idOcorrenciaEntregaAnterior = idOcorrenciaEntregaAnterior;
	}
	
}
