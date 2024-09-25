package com.mercurio.lms.contasreceber.model.param;

import org.joda.time.YearMonthDay;

public class DepositoCcorrenteParam {
	
	private Long idDepositoCcorrente;
	
	private Long idCliente;
	
	private Long idCedente;
	
	private YearMonthDay dtDepositoInicial;
	
	private YearMonthDay dtDepositoFinal;
	
	private String tpSituacaoDeposito;


	public YearMonthDay getDtDepositoFinal() {
		return dtDepositoFinal;
	}

	public void setDtDepositoFinal(YearMonthDay dtDepositoFinal) {
		this.dtDepositoFinal = dtDepositoFinal;
	}

	public YearMonthDay getDtDepositoInicial() {
		return dtDepositoInicial;
	}

	public void setDtDepositoInicial(YearMonthDay dtDepositoInicial) {
		this.dtDepositoInicial = dtDepositoInicial;
	}

	public Long getIdCedente() {
		return idCedente;
	}

	public void setIdCedente(Long idCedente) {
		this.idCedente = idCedente;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getTpSituacaoDeposito() {
		return tpSituacaoDeposito;
	}

	public void setTpSituacaoDeposito(String tpSituacaoDeposito) {
		this.tpSituacaoDeposito = tpSituacaoDeposito;
	}

	public Long getIdDepositoCcorrente() {
		return idDepositoCcorrente;
	}

	public void setIdDepositoCcorrente(Long idDepositoCcorrente) {
		this.idDepositoCcorrente = idDepositoCcorrente;
	}
	
	
}
