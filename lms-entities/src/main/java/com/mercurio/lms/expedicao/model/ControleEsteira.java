package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.joda.time.YearMonthDay;

public class ControleEsteira implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long idControleEsteira;
	
	private Long codInterno;
	
	private String nrLote;
	
	private YearMonthDay data;
	
	private String hora;
	
	private String codBarras;
	
	private BigDecimal comprimento;
	
	private BigDecimal largura;
	
	private BigDecimal altura;
	
	private BigDecimal volume;

	private BigDecimal peso;
	
	private String statusSoftware;
	
	private Boolean blProcessoRPP;

	private Timestamp dhProcessamento;

	public Long getIdControleEsteira() {
		return idControleEsteira;
	}

	public void setIdControleEsteira(Long idControleEsteira) {
		this.idControleEsteira = idControleEsteira;
	}

	public String getNrLote() {
		return nrLote;
	}

	public void setNrLote(String nrLote) {
		this.nrLote = nrLote;
	}

	public YearMonthDay getData() {
		return data;
	}

	public void setData(YearMonthDay data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getCodBarras() {
		return codBarras;
	}

	public void setCodBarras(String codBarras) {
		this.codBarras = codBarras;
	}

	public BigDecimal getComprimento() {
		return comprimento;
	}

	public void setComprimento(BigDecimal comprimento) {
		this.comprimento = comprimento;
	}

	public BigDecimal getLargura() {
		return largura;
	}

	public void setLargura(BigDecimal largura) {
		this.largura = largura;
	}

	public BigDecimal getAltura() {
		return altura;
	}

	public void setAltura(BigDecimal altura) {
		this.altura = altura;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}

	public String getStatusSoftware() {
		return statusSoftware;
	}

	public void setStatusSoftware(String statusSoftware) {
		this.statusSoftware = statusSoftware;
	}

	public Boolean getBlProcessoRPP() {
		return blProcessoRPP;
	}

	public void setBlProcessoRPP(Boolean blProcessoRPP) {
		this.blProcessoRPP = blProcessoRPP;
	}

	public Long getCodInterno() {
		return codInterno;
	}

	public void setCodInterno(Long codInterno) {
		this.codInterno = codInterno;
	}

	public Timestamp getDhProcessamento() {
		return dhProcessamento;
}

	public void setDhProcessamento(Timestamp dhProcessamento) {
		this.dhProcessamento = dhProcessamento;
	}
}
