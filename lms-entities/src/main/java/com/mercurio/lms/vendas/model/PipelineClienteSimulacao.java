package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import com.mercurio.lms.tabelaprecos.model.TabelaPreco;

public class PipelineClienteSimulacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** identifier field */
	private Long idPipelineClienteSimulacao;
	/** identifier field */
	private PipelineCliente pipelineCliente;
	/** identifier field */
	private Simulacao simulacao;
	/** identifier field */
	private TabelaPreco tabelaPreco;
	
	public Long getIdPipelineClienteSimulacao() {
		return idPipelineClienteSimulacao;
	}
	public void setIdPipelineClienteSimulacao(Long idPipelineClienteSimulacao) {
		this.idPipelineClienteSimulacao = idPipelineClienteSimulacao;
	}
	public PipelineCliente getPipelineCliente() {
		return pipelineCliente;
	}
	public void setPipelineCliente(PipelineCliente pipelineCliente) {
		this.pipelineCliente = pipelineCliente;
	}
	public Simulacao getSimulacao() {
		return simulacao;
	}
	public void setSimulacao(Simulacao simulacao) {
		this.simulacao = simulacao;
	}
	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}
	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}
	
}
