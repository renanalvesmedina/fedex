package com.mercurio.lms.contasreceber.model.param;

import org.joda.time.YearMonthDay;


public class ReciboParam {
	
	private Long nrRecibo;
	
	private Long idFilial;
	
	private YearMonthDay dtEmissaoInicial;
	
	private YearMonthDay dtEmissaoFinal;
	
	private String tpSituacaoRecibo;
	
	private String tpSituacaoAprovacao;

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

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public Long getNrRecibo() {
		return nrRecibo;
	}

	public void setNrRecibo(Long nrRecibo) {
		this.nrRecibo = nrRecibo;
	}

	public String getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(String tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public String getTpSituacaoRecibo() {
		return tpSituacaoRecibo;
	}

	public void setTpSituacaoRecibo(String tpSituacaoRecibo) {
		this.tpSituacaoRecibo = tpSituacaoRecibo;
	}


}
