package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class DadosClientesColetaDTO  extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nrIdentificacao;
	private String nmPessoa;
	private DateTime dhPedidoColeta;
	private String sgFilial;
	private Long nrColeta;
	public String getNrIdentificacao() {
		return nrIdentificacao;
	}
	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	public DateTime getDhPedidoColeta() {
		return dhPedidoColeta;
	}
	public void setDhPedidoColeta(DateTime dhPedidoColeta) {
		this.dhPedidoColeta = dhPedidoColeta;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public Long getNrColeta() {
		return nrColeta;
	}
	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}

}
