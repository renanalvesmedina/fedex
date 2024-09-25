package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetalheCarregamento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idDetalheCarregamento;
	private Long idCabecalhoCarregamento;
	private Long mapaCarregamento;
	private String codigoDestino;
	private String rotaDestino;
	private String codigoVolume;
	private String itemVolume;
	private BigDecimal cubagemVolume;
	private BigDecimal pesoVolume;
	
	public DetalheCarregamento(final Long id) {
		this.idDetalheCarregamento = id;
	}

	public DetalheCarregamento() {
	}

	public Long getIdDetalheCarregamento() {
		return idDetalheCarregamento;
	}

	public void setIdDetalheCarregamento(final Long idDetalheCarregamento) {
		this.idDetalheCarregamento = idDetalheCarregamento;
	}

	public Long getIdCabecalhoCarregamento() {
		return idCabecalhoCarregamento;
	}

	public void setIdCabecalhoCarregamento(final Long idCabecalhoCarregamento) {
		this.idCabecalhoCarregamento = idCabecalhoCarregamento;
	}

	public Long getMapaCarregamento() {
		return mapaCarregamento;
	}

	public void setMapaCarregamento(final Long mapaCarregamento) {
		this.mapaCarregamento = mapaCarregamento;
	}

	public String getCodigoDestino() {
		return codigoDestino;
	}

	public void setCodigoDestino(final String codigoDestino) {
		this.codigoDestino = codigoDestino;
	}

	public String getRotaDestino() {
		return rotaDestino;
	}

	public void setRotaDestino(final String rotaDestino) {
		this.rotaDestino = rotaDestino;
	}

	public String getCodigoVolume() {
		return codigoVolume;
	}

	public void setCodigoVolume(final String codigoVolume) {
		this.codigoVolume = codigoVolume;
	}

	public String getItemVolume() {
		return itemVolume;
	}

	public void setItemVolume(final String itemVolume) {
		this.itemVolume = itemVolume;
	}

	public BigDecimal getCubagemVolume() {
		return cubagemVolume;
	}

	public void setCubagemVolume(final BigDecimal cubagemVolume) {
		this.cubagemVolume = cubagemVolume;
	}

	public BigDecimal getPesoVolume() {
		return pesoVolume;
	}

	public void setPesoVolume(final BigDecimal pesoVolume) {
		this.pesoVolume = pesoVolume;
	}
}
