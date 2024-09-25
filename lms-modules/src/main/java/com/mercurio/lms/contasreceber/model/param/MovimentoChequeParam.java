package com.mercurio.lms.contasreceber.model.param;

import org.joda.time.YearMonthDay;


public class MovimentoChequeParam {
	
	private Long nrLoteChequeInicial;

	private Long nrLoteChequeFinal;
	
	private Short nrBanco;
	
	private Short nrAgencia;
	
	private Long nrCheque;
	
	private String nrContaBancaria;
	
	private String tpSituacaoCheque;
	
	private String tpHistoricoCheque;
	
	private YearMonthDay dtEmissaoInicial;
	
	private YearMonthDay dtEmissaoFinal;
	
	private YearMonthDay dtVencimentoInicial;
	
	private YearMonthDay dtVencimentoFinal;
	
	private YearMonthDay dtReapresentacaoInicial;
	
	private YearMonthDay dtReapresentacaoFinal;
	
	private Long idFilial;

	public YearMonthDay getDtReapresentacaoFinal() {
		return dtReapresentacaoFinal;
	}

	public void setDtReapresentacaoFinal(YearMonthDay dtReapresentacaoFinal) {
		this.dtReapresentacaoFinal = dtReapresentacaoFinal;
	}

	public YearMonthDay getDtReapresentacaoInicial() {
		return dtReapresentacaoInicial;
	}

	public void setDtReapresentacaoInicial(YearMonthDay dtReapresentacaoInicial) {
		this.dtReapresentacaoInicial = dtReapresentacaoInicial;
	}

	public YearMonthDay getDtEmissaoFinal() {
		return dtEmissaoFinal;
	}

	public void setDtEmissaoFinal(YearMonthDay dtEmissaoFinal) {
		this.dtEmissaoFinal = dtEmissaoFinal;
	}

	public YearMonthDay getDtEmissaoInicial() {
		return dtEmissaoInicial;
	}

	public void setDtEmissaoInicial(YearMonthDay dtEmissaoInicial) {
		this.dtEmissaoInicial = dtEmissaoInicial;
	}

	public YearMonthDay getDtVencimentoFinal() {
		return dtVencimentoFinal;
	}

	public void setDtVencimentoFinal(YearMonthDay dtVencimentoFinal) {
		this.dtVencimentoFinal = dtVencimentoFinal;
	}

	public YearMonthDay getDtVencimentoInicial() {
		return dtVencimentoInicial;
	}

	public void setDtVencimentoInicial(YearMonthDay dtVencimentoInicial) {
		this.dtVencimentoInicial = dtVencimentoInicial;
	}

	public Short getNrAgencia() {
		return nrAgencia;
	}

	public void setNrAgencia(Short nrAgencia) {
		this.nrAgencia = nrAgencia;
	}

	public Short getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(Short nrBanco) {
		this.nrBanco = nrBanco;
	}

	public Long getNrCheque() {
		return nrCheque;
	}

	public void setNrCheque(Long nrCheque) {
		this.nrCheque = nrCheque;
	}

	public String getNrContaBancaria() {
		return nrContaBancaria;
	}

	public void setNrContaBancaria(String nrContaBancaria) {
		this.nrContaBancaria = nrContaBancaria;
	}

	public Long getNrLoteChequeFinal() {
		return nrLoteChequeFinal;
	}

	public void setNrLoteChequeFinal(Long nrLoteChequeFinal) {
		this.nrLoteChequeFinal = nrLoteChequeFinal;
	}

	public Long getNrLoteChequeInicial() {
		return nrLoteChequeInicial;
	}

	public void setNrLoteChequeInicial(Long nrLoteChequeInicial) {
		this.nrLoteChequeInicial = nrLoteChequeInicial;
	}

	public String getTpHistoricoCheque() {
		return tpHistoricoCheque;
	}

	public void setTpHistoricoCheque(String tpHistoricoCheque) {
		this.tpHistoricoCheque = tpHistoricoCheque;
	}

	public String getTpSituacaoCheque() {
		return tpSituacaoCheque;
	}

	public void setTpSituacaoCheque(String tpSituacaoCheque) {
		this.tpSituacaoCheque = tpSituacaoCheque;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
	
	
}
