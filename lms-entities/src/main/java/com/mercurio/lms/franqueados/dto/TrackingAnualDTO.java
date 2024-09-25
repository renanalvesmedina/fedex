package com.mercurio.lms.franqueados.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TrackingAnualDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public TrackingAnualDTO() {
	}
	
	private BigDecimal cifExpedido = BigDecimal.ZERO;
	private BigDecimal fobExpedido = BigDecimal.ZERO;
	private BigDecimal cifRecebido = BigDecimal.ZERO;
	private BigDecimal fobRecebido = BigDecimal.ZERO;
	private BigDecimal aereo = BigDecimal.ZERO;
	private BigDecimal local = BigDecimal.ZERO;
	private BigDecimal descCobranca = BigDecimal.ZERO;
	private BigDecimal freteCarreteiro = BigDecimal.ZERO;
	private BigDecimal generalidade = BigDecimal.ZERO;
	private BigDecimal gris = BigDecimal.ZERO;
	private BigDecimal creditoBaseCalc = BigDecimal.ZERO;
	private BigDecimal baseCalculo = BigDecimal.ZERO;
	private BigDecimal participacaoBasica = BigDecimal.ZERO;
	private BigDecimal fixoCTRC = BigDecimal.ZERO;
	private BigDecimal ICMS = BigDecimal.ZERO;
	private BigDecimal PIS = BigDecimal.ZERO;
	private BigDecimal COFINS = BigDecimal.ZERO;
	private BigDecimal repasseICMS = BigDecimal.ZERO;
	private BigDecimal repassePIS = BigDecimal.ZERO;
	private BigDecimal repasseCOFINS = BigDecimal.ZERO;
	private BigDecimal participacao = BigDecimal.ZERO;
	private BigDecimal somaLimitador = BigDecimal.ZERO;
	private BigDecimal participacaoTotal = BigDecimal.ZERO;
	private BigDecimal repGeneralidades = BigDecimal.ZERO;
	private BigDecimal participacaoFinal = BigDecimal.ZERO;
	private BigDecimal servicosAdicionais = BigDecimal.ZERO;
	private BigDecimal cteMovimentados = BigDecimal.ZERO;
	private BigDecimal rpc = BigDecimal.ZERO;
	private BigDecimal ppc = BigDecimal.ZERO;
	private BigDecimal freteLocal = BigDecimal.ZERO;
	
	private String mes = "";

	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public void setCifExpedido(BigDecimal cifExpedido) {
		this.cifExpedido = cifExpedido;
	}
	public void setFobExpedido(BigDecimal fobExpedido) {
		this.fobExpedido = fobExpedido;
	}
	public void setCifRecebido(BigDecimal cifRecebido) {
		this.cifRecebido = cifRecebido;
	}
	public void setFobRecebido(BigDecimal fobRecebido) {
		this.fobRecebido = fobRecebido;
	}
	public void setAereo(BigDecimal aereo) {
		this.aereo = aereo;
	}
	public void setLocal(BigDecimal local) {
		this.local = local;
	}
	public void setDescCobranca(BigDecimal descCobranca) {
		this.descCobranca = descCobranca;
	}
	public void setFreteCarreteiro(BigDecimal freteCarreteiro) {
		this.freteCarreteiro = freteCarreteiro;
	}
	public void setGeneralidade(BigDecimal generalidade) {
		this.generalidade = generalidade;
	}
	public void setGris(BigDecimal gris) {
		this.gris = gris;
	}
	public void setCreditoBaseCalc(BigDecimal creditoBaseCalc) {
		this.creditoBaseCalc = creditoBaseCalc;
	}
	public void setBaseCalculo(BigDecimal baseCalculo) {
		this.baseCalculo = baseCalculo;
	}
	public void setParticipacaoBasica(BigDecimal participacaoBasica) {
		this.participacaoBasica = participacaoBasica;
	}
	public void setFixoCTRC(BigDecimal fixoCTRC) {
		this.fixoCTRC = fixoCTRC;
	}
	public void setParticipacao(BigDecimal participacao) {
		this.participacao = participacao;
	}
	public void setSomaLimitador(BigDecimal somaLimitador) {
		this.somaLimitador = somaLimitador;
	}
	public void setParticipacaoTotal(BigDecimal participacaoTotal) {
		this.participacaoTotal = participacaoTotal;
	}
	public void setRepGeneralidades(BigDecimal repGeneralidades) {
		this.repGeneralidades = repGeneralidades;
	}
	public void setParticipacaoFinal(BigDecimal participacaoFinal) {
		this.participacaoFinal = participacaoFinal;
	}
	public BigDecimal getCifExpedido() {
		return cifExpedido;
	}
	public BigDecimal getFobExpedido() {
		return fobExpedido;
	}
	public BigDecimal getCifRecebido() {
		return cifRecebido;
	}
	public BigDecimal getFobRecebido() {
		return fobRecebido;
	}
	public BigDecimal getAereo() {
		return aereo;
	}
	public BigDecimal getLocal() {
		return local;
	}
	public BigDecimal getDescCobranca() {
		return descCobranca;
	}
	public BigDecimal getFreteCarreteiro() {
		return freteCarreteiro;
	}
	public BigDecimal getGeneralidade() {
		return generalidade;
	}
	public BigDecimal getGris() {
		return gris;
	}
	public BigDecimal getCreditoBaseCalc() {
		return creditoBaseCalc;
	}
	public BigDecimal getBaseCalculo() {
		return baseCalculo;
	}
	public BigDecimal getParticipacaoBasica() {
		return participacaoBasica;
	}
	public BigDecimal getFixoCTRC() {
		return fixoCTRC;
	}
	public BigDecimal getParticipacao() {
		return participacao;
	}
	public BigDecimal getSomaLimitador() {
		return somaLimitador;
	}
	public BigDecimal getParticipacaoTotal() {
		return participacaoTotal;
	}
	public BigDecimal getRepGeneralidades() {
		return repGeneralidades;
	}
	public BigDecimal getParticipacaoFinal() {
		return participacaoFinal;
	}
	public BigDecimal getRepasseCOFINS() {
		return repasseCOFINS;
	}
	public void setRepasseCOFINS(BigDecimal repasseCOFINS) {
		this.repasseCOFINS = repasseCOFINS;
	}
	public BigDecimal getRepasseICMS() {
		return repasseICMS;
	}
	public void setRepasseICMS(BigDecimal repasseICMS) {
		this.repasseICMS = repasseICMS;
	}
	public BigDecimal getRepassePIS() {
		return repassePIS;
	}
	public void setRepassePIS(BigDecimal repassePIS) {
		this.repassePIS = repassePIS;
	}
	public BigDecimal getServicosAdicionais() {
		return servicosAdicionais;
	}
	public void setServicosAdicionais(BigDecimal servicosAdicionais) {
		this.servicosAdicionais = servicosAdicionais;
	}
	public BigDecimal getCteMovimentados() {
		return cteMovimentados;
	}
	public void setCteMovimentados(BigDecimal cteMovimentados) {
		this.cteMovimentados = cteMovimentados;
	}
	public BigDecimal getRpc() {
		return rpc;
	}
	public void setRpc(BigDecimal rpc) {
		this.rpc = rpc;
	}
	public BigDecimal getPpc() {
		return ppc;
	}
	public void setPpc(BigDecimal ppc) {
		this.ppc = ppc;
	}
	public BigDecimal getICMS() {
		return ICMS;
	}
	public void setICMS(BigDecimal iCMS) {
		ICMS = iCMS;
	}
	public BigDecimal getPIS() {
		return PIS;
	}
	public void setPIS(BigDecimal pIS) {
		PIS = pIS;
	}
	public BigDecimal getCOFINS() {
		return COFINS;
	}
	public void setCOFINS(BigDecimal cOFINS) {
		COFINS = cOFINS;
	}
	public BigDecimal getFreteLocal() {
		return freteLocal;
	}
	public void setFreteLocal(BigDecimal freteLocal) {
		this.freteLocal = freteLocal;
	}
	
}
