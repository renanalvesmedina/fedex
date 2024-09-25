package com.mercurio.lms.rest.expedicao;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.JTDateTimeUtils;

public class DadosVolumeDTO {
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
	
	public Long getCodInterno() {
		return codInterno;
	}
	public void setCodInterno(Long codInterno) {
		this.codInterno = codInterno;
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
	public void setData(String data) {
		this.data = JTDateTimeUtils.convertDataStringToYearMonthDay(data,"dd/MM/yyyy");
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
	
}
