package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class PipelineEtapa implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idPipelineEtapa;
	
	/** persistent field */
	private PipelineCliente pipelineCliente;
	
	/** persistent field */
	private DomainValue tpPipelineEtapa;
	
	/** persistent field */
	private YearMonthDay dtEvento;
	
	/** persistent field */
	private Visita visita;
	
	/** persistent field */
	private String dsObservacao;

	public Long getIdPipelineEtapa() {
		return idPipelineEtapa;
	}

	public void setIdPipelineEtapa(Long idPipelineEtapa) {
		this.idPipelineEtapa = idPipelineEtapa;
	}

	public PipelineCliente getPipelineCliente() {
		return pipelineCliente;
	}

	public void setPipelineCliente(PipelineCliente pipelineCliente) {
		this.pipelineCliente = pipelineCliente;
	}

	public DomainValue getTpPipelineEtapa() {
		return tpPipelineEtapa;
	}

	public void setTpPipelineEtapa(DomainValue tpPipelineEtapa) {
		this.tpPipelineEtapa = tpPipelineEtapa;
	}

	public YearMonthDay getDtEvento() {
		return dtEvento;
	}

	public void setDtEvento(YearMonthDay dtEvento) {
		this.dtEvento = dtEvento;
	}

	public Visita getVisita() {
		return visita;
	}

	public void setVisita(Visita visita) {
		this.visita = visita;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	
}
