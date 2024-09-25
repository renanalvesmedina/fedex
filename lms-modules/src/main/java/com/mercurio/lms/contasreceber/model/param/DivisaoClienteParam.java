package com.mercurio.lms.contasreceber.model.param;

public class DivisaoClienteParam {
	
	private Long idCliente;
	private Long idDivisaoCliente;
	private Long idServico;
	private String tpModal;
	private String tpAbrangencia;
	private String tpSituacao;
	
	public Long getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	
	public Long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}
	
	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}
	
	public Long getIdServico() {
		return idServico;
	}
	
	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}
	
	public String getTpAbrangencia() {
		return tpAbrangencia;
	}
	
	public void setTpAbrangencia(String tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}
	
	public String getTpModal() {
		return tpModal;
	}
	
	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}
	
	public String getTpSituacao() {
		return tpSituacao;
	}
	
	public void setTpSituacao(String tpSituacao) {
		this.tpSituacao = tpSituacao;
	}	
	
}
