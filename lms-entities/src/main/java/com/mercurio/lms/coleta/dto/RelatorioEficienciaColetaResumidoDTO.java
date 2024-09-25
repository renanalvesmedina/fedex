package com.mercurio.lms.coleta.dto;

import java.math.BigDecimal;

public class RelatorioEficienciaColetaResumidoDTO {

	private String idFilial;
	private String nomeFilial;
	private BigDecimal qtdTotal;
	private BigDecimal qtdEficiente;
	private BigDecimal qtdIneficiente;
	private BigDecimal qtdNeutra;

	public String getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(String idFilial) {
		this.idFilial = idFilial;
	}

	public String getNomeFilial() {
		return nomeFilial;
	}

	public void setNomeFilial(String nomeFilial) {
		this.nomeFilial = nomeFilial;
	}

	public BigDecimal getQtdTotal() {
		return qtdTotal;
	}

	public void setQtdTotal(BigDecimal qtdTotal) {
		this.qtdTotal = qtdTotal;
	}

	public BigDecimal getQtdEficiente() {
		return qtdEficiente;
	}

	public void setQtdEficiente(BigDecimal qtdEficiente) {
		this.qtdEficiente = qtdEficiente;
	}

	public BigDecimal getQtdIneficiente() {
		return qtdIneficiente;
	}

	public void setQtdIneficiente(BigDecimal qtdIneficiente) {
		this.qtdIneficiente = qtdIneficiente;
	}

	public BigDecimal getQtdNeutra() {
		return qtdNeutra;
	}

	public void setQtdNeutra(BigDecimal qtdNeutra) {
		this.qtdNeutra = qtdNeutra;
	}

}
