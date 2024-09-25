package com.mercurio.lms.contasreceber.model.param;

import org.joda.time.YearMonthDay;


public class DataVencimentoParam {
	
	private Long idServico;
	
	private Long idDivisao;
	
	private String tpFrete;
	
	private YearMonthDay dtEmissao;
	
	private String tpModal;
	
	private String tpAbrangencia;
	
	public DataVencimentoParam() {
		super();
	}

	public DataVencimentoParam(Long idServico, Long idDivisao, String tpFrete, YearMonthDay dtEmissao, String tpModal, String tpAbrangencia) {
		super();
		this.idServico = idServico;
		this.idDivisao = idDivisao;
		this.tpFrete = tpFrete;
		this.dtEmissao = dtEmissao;
		this.tpModal = tpModal;
		this.tpAbrangencia = tpAbrangencia;
	}

	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public Long getIdDivisao() {
		return idDivisao;
	}

	public void setIdDivisao(Long idDivisao) {
		this.idDivisao = idDivisao;
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

	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public String getTpModal() {
		return tpModal;
	}

	public void setTpModal(String tpModal) {
		this.tpModal = tpModal;
	}
	
	
}
