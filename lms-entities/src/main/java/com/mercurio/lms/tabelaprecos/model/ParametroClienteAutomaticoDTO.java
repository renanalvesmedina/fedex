package com.mercurio.lms.tabelaprecos.model;

import java.math.BigDecimal;

public class ParametroClienteAutomaticoDTO {

	private BigDecimal percentualGris;			
	private BigDecimal percentualMinGris;			
	private BigDecimal percentualTDE;			
	private BigDecimal percentualMinTDE;			
	private BigDecimal percentualTRT;			
	private BigDecimal percentualMinTRT;
	private BigDecimal percentualPedagioFracao;
	private BigDecimal percentualPedagioPostoFracao;
	private BigDecimal percentualPedagioFaixaPeso;	
	private BigDecimal percentualPedagioDocumento;	
	private BigDecimal percentualAdvalorem2;		
	private BigDecimal percentualMinProgr;			
	private BigDecimal percentualFretePeso;			
	private BigDecimal percentualAdvalorem;	
	private BigDecimal percentualMinFreteQuilo;		
	private BigDecimal percentualTarifaMinima;		
	private BigDecimal percentualMinFretePeso;
	private BigDecimal percentualFrete;
	private BigDecimal percentualMinFrete;
	private BigDecimal percentualTonelada;
	private String     tpPedagioPadrao;
	private Long 	   idReajusteTabPreco;
	private Long       idTabPrecoBase;
	private Long       idTabNova;
	private BigDecimal percentualGeral;
	private BigDecimal valorTabelaGris;
	private BigDecimal valorTabelaTDE;
	private BigDecimal valorTabelaTRT;
	private BigDecimal valorMinTabelaGris;
	private BigDecimal valorMinTabelaTDE;
	private BigDecimal valorMinTabelaTRT;
	private BigDecimal valorTabelaPedagio;
	private BigDecimal percentualEspecifica;
	
	public BigDecimal getPercentualMinGris() {
		return percentualMinGris;
	}
	public void setPercentualMinGris(BigDecimal percentualMinGris) {
		this.percentualMinGris = percentualMinGris;
	}
	public BigDecimal getPercentualTDE() {
		return percentualTDE;
	}
	public void setPercentualTDE(BigDecimal percentualTDE) {
		this.percentualTDE = percentualTDE;
	}
	public BigDecimal getPercentualMinTDE() {
		return percentualMinTDE;
	}
	public void setPercentualMinTDE(BigDecimal percentualMinTDE) {
		this.percentualMinTDE = percentualMinTDE;
	}
	public BigDecimal getPercentualTRT() {
		return percentualTRT;
	}
	public void setPercentualTRT(BigDecimal percentualTRT) {
		this.percentualTRT = percentualTRT;
	}
	public BigDecimal getPercentualMinTRT() {
		return percentualMinTRT;
	}
	public void setPercentualMinTRT(BigDecimal percentualMinTRT) {
		this.percentualMinTRT = percentualMinTRT;
	}
	public BigDecimal getPercentualPedagioFracao() {
		return percentualPedagioFracao;
	}
	public void setPercentualPedagioFracao(BigDecimal percentualPedagioFracao) {
		this.percentualPedagioFracao = percentualPedagioFracao;
	}
	public BigDecimal getPercentualPedagioPostoFracao() {
		return percentualPedagioPostoFracao;
	}
	public void setPercentualPedagioPostoFracao(BigDecimal percentualPedagioPostoFracao) {
		this.percentualPedagioPostoFracao = percentualPedagioPostoFracao;
	}
	public BigDecimal getPercentualPedagioFaixaPeso() {
		return percentualPedagioFaixaPeso;
	}
	public void setPercentualPedagioFaixaPeso(BigDecimal percentualPedagioFaixaPeso) {
		this.percentualPedagioFaixaPeso = percentualPedagioFaixaPeso;
	}
	public BigDecimal getPercentualPedagioDocumento() {
		return percentualPedagioDocumento;
	}
	public void setPercentualPedagioDocumento(BigDecimal percentualPedagioDocumento) {
		this.percentualPedagioDocumento = percentualPedagioDocumento;
	}
	public BigDecimal getPercentualAdvalorem2() {
		return percentualAdvalorem2;
	}
	public void setPercentualAdvalorem2(BigDecimal percentualAdvalorem2) {
		this.percentualAdvalorem2 = percentualAdvalorem2;
	}
	public BigDecimal getPercentualMinProgr() {
		return percentualMinProgr;
	}
	public void setPercentualMinProgr(BigDecimal percentualMinProgr) {
		this.percentualMinProgr = percentualMinProgr;
	}
	public BigDecimal getPercentualFretePeso() {
		return percentualFretePeso;
	}
	public void setPercentualFretePeso(BigDecimal percentualFretePeso) {
		this.percentualFretePeso = percentualFretePeso;
	}
	public BigDecimal getPercentualAdvalorem() {
		return percentualAdvalorem;
	}
	public void setPercentualAdvalorem(BigDecimal percentualAdvalorem) {
		this.percentualAdvalorem = percentualAdvalorem;
	}
	public BigDecimal getPercentualMinFreteQuilo() {
		return percentualMinFreteQuilo;
	}
	public void setPercentualMinFreteQuilo(BigDecimal percentualMinFreteQuilo) {
		this.percentualMinFreteQuilo = percentualMinFreteQuilo;
	}
	public BigDecimal getPercentualTarifaMinima() {
		return percentualTarifaMinima;
	}
	public void setPercentualTarifaMinima(BigDecimal percentualTarifaMinima) {
		this.percentualTarifaMinima = percentualTarifaMinima;
	}
	public BigDecimal getPercentualMinFretePeso() {
		return percentualMinFretePeso;
	}
	public void setPercentualMinFretePeso(BigDecimal percentualMinFretePeso) {
		this.percentualMinFretePeso = percentualMinFretePeso;
	}
	public String getTpPedagioPadrao() {
		return tpPedagioPadrao;
	}
	public void setTpPedagioPadrao(String tpPedagioPadrao) {
		this.tpPedagioPadrao = tpPedagioPadrao;
	}
	public BigDecimal getPercentualGris() {
		return percentualGris;
	}
	public void setPercentualGris(BigDecimal percentualGris) {
		this.percentualGris = percentualGris;
	}
	public Long getIdReajusteTabPreco() {
		return idReajusteTabPreco;
	}
	public void setIdReajusteTabPreco(Long idReajusteTabPreco) {
		this.idReajusteTabPreco = idReajusteTabPreco;
	}
	public BigDecimal getPercentualFrete() {
		return percentualFrete;
	}
	public void setPercentualFrete(BigDecimal percentualFrete) {
		this.percentualFrete = percentualFrete;
	}
	public BigDecimal getPercentualMinFrete() {
		return percentualMinFrete;
	}
	public void setPercentualMinFrete(BigDecimal percentualMinFrete) {
		this.percentualMinFrete = percentualMinFrete;
	}
	public BigDecimal getPercentualTonelada() {
		return percentualTonelada;
	}
	public void setPercentualTonelada(BigDecimal percentualTonelada) {
		this.percentualTonelada = percentualTonelada;
	}
	public Long getIdTabPrecoBase() {
		return idTabPrecoBase;
	}
	public void setIdTabPrecoBase(Long idTabPrecoBase) {
		this.idTabPrecoBase = idTabPrecoBase;
	}
	public Long getIdTabNova() {
		return idTabNova;
	}
	public void setIdTabNova(Long idTabNova) {
		this.idTabNova = idTabNova;
	}
	public BigDecimal getPercentualGeral() {
		return percentualGeral;
	}
	public void setPercentualGeral(BigDecimal percentualGeral) {
		this.percentualGeral = percentualGeral;
	}
	public BigDecimal getValorTabelaGris() {
		return valorTabelaGris;
	}
	public void setValorTabelaGris(BigDecimal valorTabelaGris) {
		this.valorTabelaGris = valorTabelaGris;
	}
	public BigDecimal getValorMinTabelaGris() {
		return valorMinTabelaGris;
	}
	public void setValorMinTabelaGris(BigDecimal valorMinTabelaGris) {
		this.valorMinTabelaGris = valorMinTabelaGris;
	}
	public BigDecimal getValorMinTabelaTDE() {
		return valorMinTabelaTDE;
	}
	public void setValorMinTabelaTDE(BigDecimal valorMinTabelaTDE) {
		this.valorMinTabelaTDE = valorMinTabelaTDE;
	}
	public BigDecimal getValorMinTabelaTRT() {
		return valorMinTabelaTRT;
	}
	public void setValorMinTabelaTRT(BigDecimal valorMinTabelaTRT) {
		this.valorMinTabelaTRT = valorMinTabelaTRT;
	}
	public BigDecimal getValorTabelaPedagio() {
		return valorTabelaPedagio;
	}
	public void setValorTabelaPedagio(BigDecimal valorTabelaPedagio) {
		this.valorTabelaPedagio = valorTabelaPedagio;
	}
	public BigDecimal getPercentualEspecifica() {
		return percentualEspecifica;
	}
	public void setPercentualEspecifica(BigDecimal percentualEspecifica) {
		this.percentualEspecifica = percentualEspecifica;
	}
	public BigDecimal getValorTabelaTDE() {
		return valorTabelaTDE;
	}
	public void setValorTabelaTDE(BigDecimal valorTabelaTDE) {
		this.valorTabelaTDE = valorTabelaTDE;
	}
	public BigDecimal getValorTabelaTRT() {
		return valorTabelaTRT;
	}
	public void setValorTabelaTRT(BigDecimal valorTabelaTRT) {
		this.valorTabelaTRT = valorTabelaTRT;
	}		
}
