package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TotalCarregamento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idTotalCarregamento;
	private Long mapaCarregamento;
	private Long totalVolume;
	private BigDecimal totalPeso;
	private BigDecimal totalCubagem;
	private CabecalhoCarregamento cabecalhoCarregamento;
	
	public TotalCarregamento(final Long id) {
		this.idTotalCarregamento = id;
	}

	public TotalCarregamento() {
	}

	public Long getIdTotalCarregamento() {
		return idTotalCarregamento;
	}

	public void setIdTotalCarregamento(final Long idTotalCarregamento) {
		this.idTotalCarregamento = idTotalCarregamento;
	}

	public Long getMapaCarregamento() {
		return mapaCarregamento;
	}

	public void setMapaCarregamento(final Long mapaCarregamento) {
		this.mapaCarregamento = mapaCarregamento;
	}

	public Long getTotalVolume() {
		return totalVolume;
	}

	public void setTotalVolume(final Long totalVolume) {
		this.totalVolume = totalVolume;
	}

	public BigDecimal getTotalPeso() {
		return totalPeso;
	}

	public void setTotalPeso(final BigDecimal totalPeso) {
		this.totalPeso = totalPeso;
	}

	public BigDecimal getTotalCubagem() {
		return totalCubagem;
	}

	public void setTotalCubagem(final BigDecimal totalCubagem) {
		this.totalCubagem = totalCubagem;
	}

	public CabecalhoCarregamento getCabecalhoCarregamento() {
		return cabecalhoCarregamento;
	}

	public void setCabecalhoCarregamento(
			CabecalhoCarregamento cabecalhoCarregamento) {
		this.cabecalhoCarregamento = cabecalhoCarregamento;
	}
}
