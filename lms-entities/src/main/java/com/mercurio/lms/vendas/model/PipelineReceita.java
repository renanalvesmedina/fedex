package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;

public class PipelineReceita implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** identifier field */
	private Long idPipelineReceita;
	
	/** persistent field */
	private PipelineCliente pipelineCliente;
	
	/** persistent field */
	private DomainValue tpAbrangencia;
	
	/** persistent field */
	private DomainValue tpModal;
	
	/** persistent field */
	private BigDecimal vlReceita;
	
	/** persistent field */
	private String dsConcorrente1;
	
	/** persistent field */
	private String dsConcorrente2;

	public Long getIdPipelineReceita() {
		return idPipelineReceita;
	}

	public void setIdPipelineReceita(Long idPipelineReceita) {
		this.idPipelineReceita = idPipelineReceita;
	}

	public PipelineCliente getPipelineCliente() {
		return pipelineCliente;
	}

	public void setPipelineCliente(PipelineCliente pipelineCliente) {
		this.pipelineCliente = pipelineCliente;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public BigDecimal getVlReceita() {
		return vlReceita;
	}

	public void setVlReceita(BigDecimal vlReceita) {
		this.vlReceita = vlReceita;
	}

	public String getDsConcorrente1() {
		return dsConcorrente1;
	}

	public void setDsConcorrente1(String dsConcorrente1) {
		this.dsConcorrente1 = dsConcorrente1;
	}

	public String getDsConcorrente2() {
		return dsConcorrente2;
	}

	public void setDsConcorrente2(String dsConcorrente2) {
		this.dsConcorrente2 = dsConcorrente2;
	}
	}
