package com.mercurio.lms.contasreceber.model.param;

import org.joda.time.YearMonthDay;

public class BoletoParam {
	
	private String nrBoleto;
	
	private Long nrSequenciaFilial;
	
	private String tpDocumentoServico;
	
	private String tpSituacaoBoleto;	
	
	private Long idFilialOrigem;
	
	private Long idDevedorDocServFat;
	
	private Long idFatura;
	
	private Long idCedente;
	
	private Long idFilialFatura;
	
	private Long idFilialCobranca;	
	
	private Long idCliente;
	
	private YearMonthDay dtEmissaoInicial;
	
	private YearMonthDay dtEmissaoFinal;
	
	private YearMonthDay dtVencimentoInicial;
	
	private YearMonthDay dtVencimentoFinal;
	
	private YearMonthDay dtLiquidacaoInicial;
	
	private YearMonthDay dtLiquidacaoFinal;

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

	public YearMonthDay getDtLiquidacaoFinal() {
		return dtLiquidacaoFinal;
	}

	public void setDtLiquidacaoFinal(YearMonthDay dtLiquidacaoFinal) {
		this.dtLiquidacaoFinal = dtLiquidacaoFinal;
	}

	public YearMonthDay getDtLiquidacaoInicial() {
		return dtLiquidacaoInicial;
	}

	public void setDtLiquidacaoInicial(YearMonthDay dtLiquidacaoInicial) {
		this.dtLiquidacaoInicial = dtLiquidacaoInicial;
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

	public Long getIdDevedorDocServFat() {
		return idDevedorDocServFat;
	}

	public void setIdDevedorDocServFat(Long idDevedorDocServFat) {
		this.idDevedorDocServFat = idDevedorDocServFat;
	}

	public Long getIdFilialFatura() {
		return idFilialFatura;
	}

	public void setIdFilialFatura(Long idFilialFatura) {
		this.idFilialFatura = idFilialFatura;
	}

	public Long getIdFilialOrigem() {
		return idFilialOrigem;
	}

	public void setIdFilialOrigem(Long idFilialOrigem) {
		this.idFilialOrigem = idFilialOrigem;
	}

	public Long getNrSequenciaFilial() {
		return nrSequenciaFilial;
	}

	public void setNrSequenciaFilial(Long nrSequenciaFilial) {
		this.nrSequenciaFilial = nrSequenciaFilial;
	}

	public String getTpDocumentoServico() {
		return tpDocumentoServico;
	}

	public void setTpDocumentoServico(String tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}

	public Long getIdFilialCobranca() {
		return idFilialCobranca;
	}

	public void setIdFilialCobranca(Long idFilialCobranca) {
		this.idFilialCobranca = idFilialCobranca;
	}

	public String getTpSituacaoBoleto() {
		return tpSituacaoBoleto;
	}

	public void setTpSituacaoBoleto(String tpSituacaoBoleto) {
		this.tpSituacaoBoleto = tpSituacaoBoleto;
	}

	public Long getIdFatura() {
		return idFatura;
	}

	public void setIdFatura(Long idFatura) {
		this.idFatura = idFatura;
	}

	public String getNrBoleto() {
		return nrBoleto;
	}

	public void setNrBoleto(String nrBoleto) {
		this.nrBoleto = nrBoleto;
	}

	
}
