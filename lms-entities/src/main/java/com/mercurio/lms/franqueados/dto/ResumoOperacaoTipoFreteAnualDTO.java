package com.mercurio.lms.franqueados.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ResumoOperacaoTipoFreteAnualDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public ResumoOperacaoTipoFreteAnualDTO() {
	}

	BigDecimal ctesTotal = BigDecimal.ZERO;
	BigDecimal freteTotal = BigDecimal.ZERO;
	BigDecimal baseCalcTotal = BigDecimal.ZERO;
	BigDecimal limitadorTotal = BigDecimal.ZERO;
	BigDecimal particTotal = BigDecimal.ZERO;
	BigDecimal pesoTotal = BigDecimal.ZERO;
	BigDecimal rpkTotal = BigDecimal.ZERO;
	BigDecimal rpcTotal = BigDecimal.ZERO;
	BigDecimal ppcTotal = BigDecimal.ZERO;
	BigDecimal percFrete = BigDecimal.ZERO;
	BigDecimal percBaseCalc = BigDecimal.ZERO;
	
	BigDecimal ctesColeta = BigDecimal.ZERO;
	BigDecimal freteColeta = BigDecimal.ZERO;
	BigDecimal particColeta = BigDecimal.ZERO;
	BigDecimal pesoColeta = BigDecimal.ZERO;
	BigDecimal rpkColeta = BigDecimal.ZERO;
	BigDecimal rpcColeta = BigDecimal.ZERO;
	BigDecimal ppcColeta = BigDecimal.ZERO;
	BigDecimal percFreteColeta = BigDecimal.ZERO;
	BigDecimal percBaseCalcColeta = BigDecimal.ZERO;
	
	BigDecimal ctesEntrega = BigDecimal.ZERO;
	BigDecimal freteEntrega = BigDecimal.ZERO;
	BigDecimal particEntrega = BigDecimal.ZERO;
	BigDecimal pesoEntrega = BigDecimal.ZERO;
	BigDecimal rpkEntrega = BigDecimal.ZERO;
	BigDecimal rpcEntrega = BigDecimal.ZERO;
	BigDecimal ppcEntrega = BigDecimal.ZERO;
	BigDecimal percFreteEntrega = BigDecimal.ZERO;
	BigDecimal percBaseCalcEntrega = BigDecimal.ZERO;
	
	BigDecimal cifExpedido = BigDecimal.ZERO;
	BigDecimal fobExpedido = BigDecimal.ZERO;
	BigDecimal cifRecebido = BigDecimal.ZERO;
	BigDecimal fobRecebido = BigDecimal.ZERO;
	BigDecimal aereo = BigDecimal.ZERO;
	BigDecimal local = BigDecimal.ZERO;
	
	String mes = "";

	public BigDecimal getCtesTotal() {
		return ctesTotal;
	}
	public void setCtesTotal(BigDecimal ctesTotal) {
		this.ctesTotal = ctesTotal;
	}
	public BigDecimal getFreteTotal() {
		return freteTotal;
	}
	public void setFreteTotal(BigDecimal freteTotal) {
		this.freteTotal = freteTotal;
	}
	public BigDecimal getBaseCalcTotal() {
		return baseCalcTotal;
	}
	public void setBaseCalcTotal(BigDecimal baseCalcTotal) {
		this.baseCalcTotal = baseCalcTotal;
	}
	public BigDecimal getLimitadorTotal() {
		return limitadorTotal;
	}
	public void setLimitadorTotal(BigDecimal limitadorTotal) {
		this.limitadorTotal = limitadorTotal;
	}
	public BigDecimal getParticTotal() {
		return particTotal;
	}
	public void setParticTotal(BigDecimal particTotal) {
		this.particTotal = particTotal;
	}
	public BigDecimal getPesoTotal() {
		return pesoTotal;
	}
	public void setPesoTotal(BigDecimal pesoTotal) {
		this.pesoTotal = pesoTotal;
	}
	public BigDecimal getRpkTotal() {
		return rpkTotal;
	}
	public void setRpkTotal(BigDecimal rpkTotal) {
		this.rpkTotal = rpkTotal;
	}
	public BigDecimal getRpcTotal() {
		return rpcTotal;
	}
	public void setRpcTotal(BigDecimal rpcTotal) {
		this.rpcTotal = rpcTotal;
	}
	public BigDecimal getPpcTotal() {
		return ppcTotal;
	}
	public void setPpcTotal(BigDecimal ppcTotal) {
		this.ppcTotal = ppcTotal;
	}
	public BigDecimal getPercFrete() {
		return percFrete;
	}
	public void setPercFrete(BigDecimal percFrete) {
		this.percFrete = percFrete;
	}
	public BigDecimal getPercBaseCalc() {
		return percBaseCalc;
	}
	public void setPercBaseCalc(BigDecimal percBaseCalc) {
		this.percBaseCalc = percBaseCalc;
	}
	public BigDecimal getCtesColeta() {
		return ctesColeta;
	}
	public void setCtesColeta(BigDecimal ctesColeta) {
		this.ctesColeta = ctesColeta;
	}
	public BigDecimal getFreteColeta() {
		return freteColeta;
	}
	public void setFreteColeta(BigDecimal freteColeta) {
		this.freteColeta = freteColeta;
	}
	public BigDecimal getPesoColeta() {
		return pesoColeta;
	}
	public void setPesoColeta(BigDecimal pesoColeta) {
		this.pesoColeta = pesoColeta;
	}
	public BigDecimal getParticColeta() {
		return particColeta;
	}
	public void setParticColeta(BigDecimal particColeta) {
		this.particColeta = particColeta;
	}
	public BigDecimal getRpkColeta() {
		return rpkColeta;
	}
	public void setRpkColeta(BigDecimal rpkColeta) {
		this.rpkColeta = rpkColeta;
	}
	public BigDecimal getRpcColeta() {
		return rpcColeta;
	}
	public void setRpcColeta(BigDecimal rpcColeta) {
		this.rpcColeta = rpcColeta;
	}
	public BigDecimal getPpcColeta() {
		return ppcColeta;
	}
	public void setPpcColeta(BigDecimal ppcColeta) {
		this.ppcColeta = ppcColeta;
	}
	public BigDecimal getPercFreteColeta() {
		return percFreteColeta;
	}
	public void setPercFreteColeta(BigDecimal percFreteColeta) {
		this.percFreteColeta = percFreteColeta;
	}
	public BigDecimal getCtesEntrega() {
		return ctesEntrega;
	}
	public void setCtesEntrega(BigDecimal ctesEntrega) {
		this.ctesEntrega = ctesEntrega;
	}
	public BigDecimal getFreteEntrega() {
		return freteEntrega;
	}
	public void setFreteEntrega(BigDecimal freteEntrega) {
		this.freteEntrega = freteEntrega;
	}
	public BigDecimal getParticEntrega() {
		return particEntrega;
	}
	public void setParticEntrega(BigDecimal particEntrega) {
		this.particEntrega = particEntrega;
	}
	public BigDecimal getPesoEntrega() {
		return pesoEntrega;
	}
	public void setPesoEntrega(BigDecimal pesoEntrega) {
		this.pesoEntrega = pesoEntrega;
	}
	public BigDecimal getRpkEntrega() {
		return rpkEntrega;
	}
	public void setRpkEntrega(BigDecimal rpkEntrega) {
		this.rpkEntrega = rpkEntrega;
	}
	public BigDecimal getRpcEntrega() {
		return rpcEntrega;
	}
	public void setRpcEntrega(BigDecimal rpcEntrega) {
		this.rpcEntrega = rpcEntrega;
	}
	public BigDecimal getPpcEntrega() {
		return ppcEntrega;
	}
	public void setPpcEntrega(BigDecimal ppcEntrega) {
		this.ppcEntrega = ppcEntrega;
	}
	public BigDecimal getPercFreteEntrega() {
		return percFreteEntrega;
	}
	public void setPercFreteEntrega(BigDecimal percFreteEntrega) {
		this.percFreteEntrega = percFreteEntrega;
	}
	public BigDecimal getCifExpedido() {
		return cifExpedido;
	}
	public void setCifExpedido(BigDecimal cifExpedido) {
		this.cifExpedido = cifExpedido;
	}
	public BigDecimal getFobExpedido() {
		return fobExpedido;
	}
	public void setFobExpedido(BigDecimal fobExpedido) {
		this.fobExpedido = fobExpedido;
	}
	public BigDecimal getCifRecebido() {
		return cifRecebido;
	}
	public void setCifRecebido(BigDecimal cifRecebido) {
		this.cifRecebido = cifRecebido;
	}
	public BigDecimal getFobRecebido() {
		return fobRecebido;
	}
	public void setFobRecebido(BigDecimal fobRecebido) {
		this.fobRecebido = fobRecebido;
	}
	public BigDecimal getAereo() {
		return aereo;
	}
	public void setAereo(BigDecimal aereo) {
		this.aereo = aereo;
	}
	public BigDecimal getLocal() {
		return local;
	}
	public void setLocal(BigDecimal local) {
		this.local = local;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public BigDecimal getPercBaseCalcColeta() {
		return percBaseCalcColeta;
	}
	public void setPercBaseCalcColeta(BigDecimal percBaseCalcColeta) {
		this.percBaseCalcColeta = percBaseCalcColeta;
	}
	public BigDecimal getPercBaseCalcEntrega() {
		return percBaseCalcEntrega;
	}
	public void setPercBaseCalcEntrega(BigDecimal percBaseCalcEntrega) {
		this.percBaseCalcEntrega = percBaseCalcEntrega;
	}
}
