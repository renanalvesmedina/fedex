package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class HistoricoVolume implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idVolume;
	private Long idHistoricoVolume;
	private Long idCarregamento;
	private DateTime dataHistorico;
	private String codigoVolume;
	private DomainValue codigoStatus;
	private Long matriculaResponsavel;
	private String autorizador;
	private Long idRejeitoMpc;

	public HistoricoVolume(Long id) {
		this.idHistoricoVolume = id;
	}

	public HistoricoVolume() {
	}

	public Long getIdVolume() {
		return idVolume;
	}

	public void setIdVolume(Long idVolume) {
		this.idVolume = idVolume;
	}

	public Long getIdHistoricoVolume() {
		return idHistoricoVolume;
	}

	public void setIdHistoricoVolume(Long idHistoricoVolume) {
		this.idHistoricoVolume = idHistoricoVolume;
	}

	public Long getIdCarregamento() {
		return idCarregamento;
	}

	public void setIdCarregamento(Long idCarregamento) {
		this.idCarregamento = idCarregamento;
	}

	public DateTime getDataHistorico() {
		return dataHistorico;
	}

	public void setDataHistorico(DateTime dataHistorico) {
		this.dataHistorico = dataHistorico;
	}

	public String getCodigoVolume() {
		return codigoVolume;
	}

	public void setCodigoVolume(String codigoVolume) {
		this.codigoVolume = codigoVolume;
	}

	public DomainValue getCodigoStatus() {
		return codigoStatus;
	}

	public void setCodigoStatus(DomainValue codigoStatus) {
		this.codigoStatus = codigoStatus;
	}

	public Long getMatriculaResponsavel() {
		return matriculaResponsavel;
	}

	public void setMatriculaResponsavel(Long matriculaResponsavel) {
		this.matriculaResponsavel = matriculaResponsavel;
	}

	public String getAutorizador() {
		return autorizador;
	}

	public void setAutorizador(String autorizador) {
		this.autorizador = autorizador;
	}
	
	public Long getIdRejeitoMpc() {
		return idRejeitoMpc;
	}

	public void setIdRejeitoMpc(Long idRejeitoMpc) {
		this.idRejeitoMpc = idRejeitoMpc;
	}
}
