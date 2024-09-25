package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class DiferencaCapitalInterior implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long idDiferencaCapitalInterior;
	
	private BigDecimal pcDiferencaPadrao;
	
	private BigDecimal pcDiferencaMinima;
	
	private BigDecimal pcDiferencaPadraoAdvalorem;
	
	private BigDecimal pcDiferencaMinimaAdvalorem;
	
	private UnidadeFederativa ufOrigem;
	
	private UnidadeFederativa ufDestino;

	public Long getIdDiferencaCapitalInterior() {
		return idDiferencaCapitalInterior;
	}

	public BigDecimal getPcDiferencaPadraoAdvalorem() {
		return pcDiferencaPadraoAdvalorem;
	}

	public void setPcDiferencaPadraoAdvalorem(
			BigDecimal pcDiferencaPadraoAdvalorem) {
		this.pcDiferencaPadraoAdvalorem = pcDiferencaPadraoAdvalorem;
	}

	public BigDecimal getPcDiferencaMinimaAdvalorem() {
		return pcDiferencaMinimaAdvalorem;
	}

	public void setPcDiferencaMinimaAdvalorem(
			BigDecimal pcDiferencaMinimaAdvalorem) {
		this.pcDiferencaMinimaAdvalorem = pcDiferencaMinimaAdvalorem;
	}

	public void setIdDiferencaCapitalInterior(Long idDiferencaCapitalInterior) {
		this.idDiferencaCapitalInterior = idDiferencaCapitalInterior;
	}

	public BigDecimal getPcDiferencaPadrao() {
		return pcDiferencaPadrao;
	}

	public void setPcDiferencaPadrao(BigDecimal pcDiferencaPadrao) {
		this.pcDiferencaPadrao = pcDiferencaPadrao;
	}

	public BigDecimal getPcDiferencaMinima() {
		return pcDiferencaMinima;
	}

	public void setPcDiferencaMinima(BigDecimal pcDiferencaMinima) {
		this.pcDiferencaMinima = pcDiferencaMinima;
	}

	public UnidadeFederativa getUfOrigem() {
		return ufOrigem;
	}

	public void setUfOrigem(UnidadeFederativa ufOrigem) {
		this.ufOrigem = ufOrigem;
	}

	public UnidadeFederativa getUfDestino() {
		return ufDestino;
	}

	public void setUfDestino(UnidadeFederativa ufDestino) {
		this.ufDestino = ufDestino;
	}

}
