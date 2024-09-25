package com.mercurio.lms.coleta.report.dto;

import java.math.BigDecimal;

public class RelatorioEficienciaColetaResumido {

	private String idFilial;
	private String nomeFilial;
	private BigDecimal qtdTotal;
	private BigDecimal qtdEficiente;
	private BigDecimal qtdIneficiente;
	private BigDecimal qtdNeutra;

	public RelatorioEficienciaColetaResumido(String idFilial,
			String nomeFilial, BigDecimal qtdTotal, BigDecimal qtdEficiente,
			BigDecimal qtdIneficiente, BigDecimal qtdNeutra) {
		this.idFilial = idFilial;
		this.nomeFilial = nomeFilial;
		this.qtdTotal = qtdTotal;
		this.qtdEficiente = qtdEficiente;
		this.qtdIneficiente = qtdIneficiente;
		this.qtdNeutra = qtdNeutra;
	}

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
