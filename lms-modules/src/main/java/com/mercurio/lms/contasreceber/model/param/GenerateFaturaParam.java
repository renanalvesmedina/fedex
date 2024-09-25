package com.mercurio.lms.contasreceber.model.param;


public class GenerateFaturaParam {
	
	private String tpDoctoServico;
	
	private Long idMoeda;
	
	private Long idServico;
	
	private String tpFrete;
	
	private String tpAbrangencia;
	
	private String tpModal;
	
	private String tpIdentificacao;

	private Long idDivisaoCliente;
	
	private Long idCliente;	
	
	private String nrIdentificacaoParcial;

	public Long getIdMoeda() {
		return idMoeda;
	}

	public void setIdMoeda(Long idMoeda) {
		this.idMoeda = idMoeda;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public String getTpDoctoServico() {
		return tpDoctoServico;
	}

	public void setTpDoctoServico(String tpDoctoServico) {
		this.tpDoctoServico = tpDoctoServico;
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

	public String getNrIdentificacaoParcial() {
		return nrIdentificacaoParcial;
	}

	public void setNrIdentificacaoParcial(String nrIdentificacaoParcial) {
		this.nrIdentificacaoParcial = nrIdentificacaoParcial;
	}

	public String getTpIdentificacao() {
		return tpIdentificacao;
	}

	public void setTpIdentificacao(String tpIdentificacao) {
		this.tpIdentificacao = tpIdentificacao;
	}

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

	
}
