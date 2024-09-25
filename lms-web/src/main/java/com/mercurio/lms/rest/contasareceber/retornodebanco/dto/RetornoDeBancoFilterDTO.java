package com.mercurio.lms.rest.contasareceber.retornodebanco.dto;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.configuracoes.dto.BancoSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
 
public class RetornoDeBancoFilterDTO extends BaseFilterDTO { 
	private static final long serialVersionUID = 1L; 
	
	private DomainValue tpSituacaoFatura;
	private DomainValue tpSituacaoBoleto;
	private BancoSuggestDTO banco;
	private YearMonthDay dtEmissaoBoletoInicial;
	private YearMonthDay dtEmissaoBoletoFinal;
	private YearMonthDay dtEmissaoFaturaInicial;
	private YearMonthDay dtEmissaoFaturaFinal;
	private FilialSuggestDTO filial;
	private YearMonthDay dtVencimentoInicial;
	private YearMonthDay dtVencimentoFinal;
	private YearMonthDay dtMovimentoInicial;
	private YearMonthDay dtMovimentoFinal;
	private DomainValue somenteLiquidacao;
	private DomainValue somenteNaoCadastrado;
	
	
	public DomainValue getTpSituacaoFatura() {
		return tpSituacaoFatura;
	}
	public void setTpSituacaoFatura(DomainValue tpSituacaoFatura) {
		this.tpSituacaoFatura = tpSituacaoFatura;
	}
	public DomainValue getTpSituacaoBoleto() {
		return tpSituacaoBoleto;
	}
	public void setTpSituacaoBoleto(DomainValue tpSituacaoBoleto) {
		this.tpSituacaoBoleto = tpSituacaoBoleto;
	}
	public YearMonthDay getDtEmissaoBoletoInicial() {
		return dtEmissaoBoletoInicial;
	}
	public void setDtEmissaoBoletoInicial(YearMonthDay dtEmissaoBoletoInicial) {
		this.dtEmissaoBoletoInicial = dtEmissaoBoletoInicial;
	}
	public YearMonthDay getDtEmissaoBoletoFinal() {
		return dtEmissaoBoletoFinal;
	}
	public void setDtEmissaoBoletoFinal(YearMonthDay dtEmissaoBoletoFinal) {
		this.dtEmissaoBoletoFinal = dtEmissaoBoletoFinal;
	}
	public YearMonthDay getDtEmissaoFaturaInicial() {
		return dtEmissaoFaturaInicial;
	}
	public void setDtEmissaoFaturaInicial(YearMonthDay dtEmissaoFaturaInicial) {
		this.dtEmissaoFaturaInicial = dtEmissaoFaturaInicial;
	}
	public YearMonthDay getDtEmissaoFaturaFinal() {
		return dtEmissaoFaturaFinal;
	}
	public void setDtEmissaoFaturaFinal(YearMonthDay dtEmissaoFaturaFinal) {
		this.dtEmissaoFaturaFinal = dtEmissaoFaturaFinal;
	}
	public YearMonthDay getDtVencimentoInicial() {
		return dtVencimentoInicial;
	}
	public void setDtVencimentoInicial(YearMonthDay dtVencimentoInicial) {
		this.dtVencimentoInicial = dtVencimentoInicial;
	}
	public YearMonthDay getDtVencimentoFinal() {
		return dtVencimentoFinal;
	}
	public void setDtVencimentoFinal(YearMonthDay dtVencimentoFinal) {
		this.dtVencimentoFinal = dtVencimentoFinal;
	}
	public YearMonthDay getDtMovimentoInicial() {
		return dtMovimentoInicial;
	}
	public void setDtMovimentoInicial(YearMonthDay dtMovimentoInicial) {
		this.dtMovimentoInicial = dtMovimentoInicial;
	}
	public YearMonthDay getDtMovimentoFinal() {
		return dtMovimentoFinal;
	}
	public void setDtMovimentoFinal(YearMonthDay dtMovimentoFinal) {
		this.dtMovimentoFinal = dtMovimentoFinal;
	}
	public DomainValue getSomenteLiquidacao() {
		return somenteLiquidacao;
	}
	public void setSomenteLiquidacao(DomainValue somenteLiquidacao) {
		this.somenteLiquidacao = somenteLiquidacao;
	}
	public DomainValue getSomenteNaoCadastrado() {
		return somenteNaoCadastrado;
	}
	public void setSomenteNaoCadastrado(DomainValue somenteNaoCadastrado) {
		this.somenteNaoCadastrado = somenteNaoCadastrado;
	}
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	public BancoSuggestDTO getBanco() {
		return banco;
	}
	public void setBanco(BancoSuggestDTO banco) {
		this.banco = banco;
	}
} 
