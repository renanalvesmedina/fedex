package com.mercurio.lms.rest.contasareceber.consultarfaturasvencidas.dto;

import com.mercurio.adsm.rest.BaseDTO;
 
public class FaturasVencidasVencerDTO extends BaseDTO { 
	
	private static final long serialVersionUID = 1L; 
 
	private String fatura;
	private String clienteDevedor;
	private String emissao;
	private String vencimento;
	private String situacao;
	private String valorTotal;
	private String valorDesconto;
	private String numeroBoleto;
	
	public String getFatura() {
		return fatura;
	}
	public void setFatura(String fatura) {
		this.fatura = fatura;
	}
	public String getClienteDevedor() {
		return clienteDevedor;
	}
	public void setClienteDevedor(String clienteDevedor) {
		this.clienteDevedor = clienteDevedor;
	}
	public String getEmissao() {
		return emissao;
	}
	public void setEmissao(String emissao) {
		this.emissao = emissao;
	}
	public String getVencimento() {
		return vencimento;
	}
	public void setVencimento(String vencimento) {
		this.vencimento = vencimento;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	public String getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}
	public String getValorDesconto() {
		return valorDesconto;
	}
	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	public String getNumeroBoleto() {
		return numeroBoleto;
	}
	public void setNumeroBoleto(String numeroBoleto) {
		this.numeroBoleto = numeroBoleto;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
} 
