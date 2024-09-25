package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

public class ItemLoteCobrancaTerceira implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idItemLoteCobrancaTerceira;
	private LoteCobrancaTerceira loteCobrancaTerceira;
	private Fatura fatura;
	private String dsMotivo;
	private String nrProcesso;
	private YearMonthDay dtPagamento;
	private YearMonthDay dtDevolucao;
	private Double vlPagamento;
	private Double vlJuros;
	private Double vlProtesto;
	private Double vlMulta;
	private Double vlCredito;
	private Double vlContrato;
	private String dsHistorico;
	private String dsObservacao;
	
	public Long getIdItemLoteCobrancaTerceira() {
		return idItemLoteCobrancaTerceira;
	}
	public void setIdItemLoteCobrancaTerceira(Long idItemLoteCobrancaTerceira) {
		this.idItemLoteCobrancaTerceira = idItemLoteCobrancaTerceira;
	}
	public LoteCobrancaTerceira getLoteCobrancaTerceira() {
		return loteCobrancaTerceira;
	}
	public void setLoteCobrancaTerceira(LoteCobrancaTerceira loteCobrancaTerceira) {
		this.loteCobrancaTerceira = loteCobrancaTerceira;
	}
	public Fatura getFatura() {
		return fatura;
	}
	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}
	public String getDsMotivo() {
		return dsMotivo;
	}
	public void setDsMotivo(String dsMotivo) {
		this.dsMotivo = dsMotivo;
	}
	public String getNrProcesso() {
		return nrProcesso;
	}
	public void setNrProcesso(String nrProcesso) {
		this.nrProcesso = nrProcesso;
	}
	public YearMonthDay getDtPagamento() {
		return dtPagamento;
	}
	public void setDtPagamento(YearMonthDay dtPagamento) {
		this.dtPagamento = dtPagamento;
	}
	public YearMonthDay getDtDevolucao() {
		return dtDevolucao;
	}
	public void setDtDevolucao(YearMonthDay dtDevolucao) {
		this.dtDevolucao = dtDevolucao;
	}
	public Double getVlPagamento() {
		return vlPagamento;
	}
	public void setVlPagamento(Double vlPagamento) {
		this.vlPagamento = vlPagamento;
	}
	public Double getVlJuros() {
		return vlJuros;
	}
	public void setVlJuros(Double vlJuros) {
		this.vlJuros = vlJuros;
	}
	public Double getVlProtesto() {
		return vlProtesto;
	}
	public void setVlProtesto(Double vlProtesto) {
		this.vlProtesto = vlProtesto;
	}
	public Double getVlMulta() {
		return vlMulta;
	}
	public void setVlMulta(Double vlMulta) {
		this.vlMulta = vlMulta;
	}
	public Double getVlCredito() {
		return vlCredito;
	}
	public void setVlCredito(Double vlCredito) {
		this.vlCredito = vlCredito;
	}
	public Double getVlContrato() {
		return vlContrato;
	}
	public void setVlContrato(Double vlContrato) {
		this.vlContrato = vlContrato;
	}
	public String getDsHistorico() {
		return dsHistorico;
	}
	public void setDsHistorico(String dsHistorico) {
		this.dsHistorico = dsHistorico;
	}
	public String getDsObservacao() {
		return dsObservacao;
	}
	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
