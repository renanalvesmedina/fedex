package com.mercurio.lms.tributos.model;

import java.math.BigDecimal;

public class CalcularPisCofinsCsllIrInss {

	private String tpImpostoCalculado;
	private BigDecimal vlBaseCalculo;
	private BigDecimal vlImposto;
	private BigDecimal pcAliquotaImposto;
	private BigDecimal vlBaseEstorno;
	private String obImposto;
	private ImpostoCalculado impostoCalculado;

	public CalcularPisCofinsCsllIrInss(String tpImpostoCalculado,
			BigDecimal vlBaseCalculo, BigDecimal vlImposto,
			BigDecimal pcAliquotaImposto, BigDecimal vlBaseEstorno,
			String obImposto, ImpostoCalculado impostoCalculado) {
		this.tpImpostoCalculado = tpImpostoCalculado;
		this.vlBaseCalculo = vlBaseCalculo;
		this.vlImposto = vlImposto;
		this.pcAliquotaImposto = pcAliquotaImposto;
		this.vlBaseEstorno = vlBaseEstorno;
		this.obImposto = obImposto;
		this.impostoCalculado = impostoCalculado;
	}

	public String getObImposto() {
		return obImposto;
	}

	public void setObImposto(String obImposto) {
		this.obImposto = obImposto;
	}

	public BigDecimal getPcAliquotaImposto() {
		return pcAliquotaImposto;
	}

	public void setPcAliquotaImposto(BigDecimal pcAliquotaImposto) {
		this.pcAliquotaImposto = pcAliquotaImposto;
	}

	public String getTpImpostoCalculado() {
		return tpImpostoCalculado;
	}

	public void setTpImpostoCalculado(String tpImpostoCalculado) {
		this.tpImpostoCalculado = tpImpostoCalculado;
	}

	public BigDecimal getVlBaseCalculo() {
		return vlBaseCalculo;
	}

	public void setVlBaseCalculo(BigDecimal vlBaseCalculo) {
		this.vlBaseCalculo = vlBaseCalculo;
	}

	public BigDecimal getVlImposto() {
		return vlImposto;
	}

	public void setVlImposto(BigDecimal vlImposto) {
		this.vlImposto = vlImposto;
	}

	public BigDecimal getVlBaseEstorno() {
		return vlBaseEstorno;
	}

	public void setVlBaseEstorno(BigDecimal vlBaseEstorno) {
		this.vlBaseEstorno = vlBaseEstorno;
	}

	public ImpostoCalculado getImpostoCalculado() {
		return impostoCalculado;
}

	public void setImpostoCalculado(ImpostoCalculado impostoCalculado) {
		this.impostoCalculado = impostoCalculado;
	}
}
