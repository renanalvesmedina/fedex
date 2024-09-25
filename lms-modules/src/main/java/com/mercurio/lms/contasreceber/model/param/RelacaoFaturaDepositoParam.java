package com.mercurio.lms.contasreceber.model.param;

import java.util.List;

import org.joda.time.YearMonthDay;



public class RelacaoFaturaDepositoParam {
	
	private YearMonthDay dtEmissaoInicial;
	
	private YearMonthDay dtEmissaoFinal;
	
	private Long nrFaturaInicial;
	
	private Long nrFaturaFinal;
	
	private Long idCliente;
	
	private List lstTpSituacaoFatura;
	
	private List lstFaturas;

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

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public List getLstTpSituacaoFatura() {
		return lstTpSituacaoFatura;
	}

	public void setLstTpSituacaoFatura(List lstTpSituacaoFatura) {
		this.lstTpSituacaoFatura = lstTpSituacaoFatura;
	}

	public List getLstFaturas() {
		return lstFaturas;
	}

	public void setLstFaturas(List lstFaturas) {
		this.lstFaturas = lstFaturas;
	}

	public Long getNrFaturaFinal() {
		return nrFaturaFinal;
	}

	public void setNrFaturaFinal(Long nrFaturaFinal) {
		this.nrFaturaFinal = nrFaturaFinal;
	}

	public Long getNrFaturaInicial() {
		return nrFaturaInicial;
	}

	public void setNrFaturaInicial(Long nrFaturaInicial) {
		this.nrFaturaInicial = nrFaturaInicial;
	}
}
