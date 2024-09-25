package com.mercurio.lms.rest.expedicao.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.IntegerUtils;
 
public class NetworkAereoAwbDTO { 
	
	private static final String YELLOW = "yellow";
	private static final String GREEN = "green";
	private static final String RED = "red";
	private Long idAwb;
	private String aeroportoOrigem;
	private String aeroportoDestino;
	private String preAwb;
	private String awb;
	private Long idCiaAerea;
	private String nmCiaAerea;
	private String sgCiaAerea;
	private DateTime entrega;
	private DateTime embarque;
	private DateTime desembarque;
	private DateTime disponivelRetirada;
	private DateTime retirada;
	private DomainValue ultimoStatus;
	private String valor;
	private String peso;
	private Integer eea;
	private Integer eva;
	private Integer dpr;
	private String colorEEA;
	private String colorEVA;
	private String colorDpr;
	private BigDecimal ttEEA;
	private String ttEEAFormatado;
	private BigDecimal ttEVA;
	private String ttEVAFormatado;
	private BigDecimal ttDpr;
	private String ttDprFormatado;
	private DateTime retidoSefaz;
	private DateTime liberadoSefaz;
	private BigDecimal ttSefaz;
	private String ttSefazFormatado;
	private DateTime dhAguardandoEmbarque;
	
	public Long getIdCiaAerea() {
		return idCiaAerea;
	}
	public void setIdCiaAerea(Long idCiaAerea) {
		this.idCiaAerea = idCiaAerea;
	}
	public String getNmCiaAerea() {
		return nmCiaAerea;
	}
	public void setNmCiaAerea(String nmCiaAerea) {
		this.nmCiaAerea = nmCiaAerea;
	}
	public String getAeroportoOrigem() {
		return aeroportoOrigem;
	}
	public void setAeroportoOrigem(String aeroportoOrigem) {
		this.aeroportoOrigem = aeroportoOrigem;
	}
	public String getAeroportoDestino() {
		return aeroportoDestino;
	}
	public void setAeroportoDestino(String aeroportoDestino) {
		this.aeroportoDestino = aeroportoDestino;
	}
	public String getPreAwb() {
		return preAwb;
	}
	public void setPreAwb(String preAwb) {
		this.preAwb = preAwb;
	}
	public String getAwb() {
		return awb;
	}
	public void setAwb(String awb) {
		this.awb = awb;
	}

	public DomainValue getUltimoStatus() {
		return ultimoStatus;
	}
	public void setUltimoStatus(DomainValue ultimoStatus) {
		this.ultimoStatus = ultimoStatus;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getPeso() {
		return peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}
	public Long getIdAwb() {
		return idAwb;
	}
	public void setIdAwb(Long idAwb) {
		this.idAwb = idAwb;
	}
	public Integer getEea() {
		return eea;
	}
	public void setEea(Integer eea) {
		this.eea = eea;
	}
	public Integer getEva() {
		return eva;
	}
	public void setEva(Integer eva) {
		this.eva = eva;
	}
	public Integer getDpr() {
		return dpr;
	}
	public void setDpr(Integer dpr) {
		this.dpr = dpr;
	}
	public String getColorEEA() {
		if (IntegerUtils.getInteger("1").equals(getEea())) {
			colorEEA = GREEN;
		} else if (IntegerUtils.getInteger("2").equals(getEea())) {
			colorEEA = YELLOW;
		} else if (IntegerUtils.getInteger("3").equals(getEea())) {
			colorEEA = RED;
		}
		
		return colorEEA;
	}
	public String getColorEVA() {
		if (IntegerUtils.getInteger("1").equals(getEva())) {
			colorEVA = GREEN;
		} else if (IntegerUtils.getInteger("2").equals(getEva())) {
			colorEVA = YELLOW;
		} else if (IntegerUtils.getInteger("3").equals(getEva())) {
			colorEVA = RED;
		}
		
		return colorEVA;
	}
	public String getColorDpr() {
		if (IntegerUtils.getInteger("1").equals(getDpr())) {
			colorDpr = GREEN;
		} else if (IntegerUtils.getInteger("2").equals(getDpr())) {
			colorDpr = YELLOW;
		} else if (IntegerUtils.getInteger("3").equals(getDpr())) {
			colorDpr = RED;
		}		
		return colorDpr;
	}
	public BigDecimal getTtEEA() {
		return ttEEA;
	}
	public void setTtEEA(BigDecimal ttEEA) {
		this.ttEEA = ttEEA;
	}
	public BigDecimal getTtEVA() {
		return ttEVA;
	}
	public void setTtEVA(BigDecimal ttEVA) {
		this.ttEVA = ttEVA;
	}
	public BigDecimal getTtDpr() {
		return ttDpr;
	}
	public void setTtDpr(BigDecimal ttDpr) {
		this.ttDpr = ttDpr;
	}
	public String getTtEEAFormatado() {
		return ttEEAFormatado;
	}
	public void setTtEEAFormatado(String ttEEAFormatado) {
		this.ttEEAFormatado = ttEEAFormatado;
	}
	public String getTtEVAFormatado() {
		return ttEVAFormatado;
	}
	public void setTtEVAFormatado(String ttEVAFormatado) {
		this.ttEVAFormatado = ttEVAFormatado;
	}
	public String getTtDprFormatado() {
		return ttDprFormatado;
	}
	public void setTtDprFormatado(String ttDprFormatado) {
		this.ttDprFormatado = ttDprFormatado;
	}
	public BigDecimal getTtSefaz() {
		return ttSefaz;
	}
	public void setTtSefaz(BigDecimal ttSefaz) {
		this.ttSefaz = ttSefaz;
	}
	public String getTtSefazFormatado() {
		return ttSefazFormatado;
	}
	public void setTtSefazFormatado(String ttSefazFormatado) {
		this.ttSefazFormatado = ttSefazFormatado;
	}
	public DateTime getEntrega() {
		return entrega;
	}
	public void setEntrega(DateTime entrega) {
		this.entrega = entrega;
	}
	public DateTime getEmbarque() {
		return embarque;
	}
	public void setEmbarque(DateTime embarque) {
		this.embarque = embarque;
	}
	public DateTime getDesembarque() {
		return desembarque;
	}
	public void setDesembarque(DateTime desembarque) {
		this.desembarque = desembarque;
	}
	public DateTime getRetirada() {
		return retirada;
	}
	public void setRetirada(DateTime retirada) {
		this.retirada = retirada;
	}
	public DateTime getRetidoSefaz() {
		return retidoSefaz;
	}
	public void setRetidoSefaz(DateTime retidoSefaz) {
		this.retidoSefaz = retidoSefaz;
	}
	public DateTime getLiberadoSefaz() {
		return liberadoSefaz;
	}
	public void setLiberadoSefaz(DateTime liberadoSefaz) {
		this.liberadoSefaz = liberadoSefaz;
	}
	public DateTime getDisponivelRetirada() {
		return disponivelRetirada;
	}
	public void setDisponivelRetirada(DateTime disponivelRetirada) {
		this.disponivelRetirada = disponivelRetirada;
	}
	public DateTime getDhAguardandoEmbarque() {
		return dhAguardandoEmbarque;
	}
	public void setDhAguardandoEmbarque(DateTime dhAguardandoEmbarque) {
		this.dhAguardandoEmbarque = dhAguardandoEmbarque;
	}
	public String getSgCiaAerea() {
		return sgCiaAerea;
	}
	public void setSgCiaAerea(String sgCiaAerea) {
		this.sgCiaAerea = sgCiaAerea;
	}
	
} 
