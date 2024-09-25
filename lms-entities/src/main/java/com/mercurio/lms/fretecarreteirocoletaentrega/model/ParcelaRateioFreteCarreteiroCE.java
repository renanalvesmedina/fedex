package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PARCELA_RATEIO_FC_CE")
@SequenceGenerator(name = "PARCELA_RATEIO_FC_CE_SQ", sequenceName = "PARCELA_RATEIO_FC_CE_SQ", allocationSize = 1)
public class ParcelaRateioFreteCarreteiroCE implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARCELA_RATEIO_FC_CE_SQ")
	@Column(name = "ID_PARCELA_RATEIO_FC_CE", nullable = false)
	private Long idParcelaRateioFreteCarreteiroCE;

	@Column(name = "TP_VALOR")
	private String tpValor;

	@Column(name = "QT_TOTAL")
	private BigDecimal qtdTotal;

	@Column(name = "VL_FATOR")
	private BigDecimal vlTotal;
	
	@ManyToOne
	@JoinColumn(name = "ID_RATEIO_FRETE_CARRETEIRO_CE", nullable = false)
	private RateioFreteCarreteiroCE rateioFreteCarreteiroCE;

	public Long getIdParcelaRateioFreteCarreteiroCE() {
		return idParcelaRateioFreteCarreteiroCE;
	}

	public void setIdParcelaRateioFreteCarreteiroCE(Long idParcelaRateioFreteCarreteiroCE) {
		this.idParcelaRateioFreteCarreteiroCE = idParcelaRateioFreteCarreteiroCE;
	}

	public String getTpValor() {
		return tpValor;
	}

	public void setTpValor(String tpValor) {
		this.tpValor = tpValor;
	}

	public BigDecimal getQtdTotal() {
		return qtdTotal;
	}

	public void setQtdTotal(BigDecimal qtdTotal) {
		this.qtdTotal = qtdTotal;
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}

	public RateioFreteCarreteiroCE getRateioFreteCarreteiroCE() {
		return rateioFreteCarreteiroCE;
	}

	public void setRateioFreteCarreteiroCE(RateioFreteCarreteiroCE rateioFreteCarreteiroCE) {
		this.rateioFreteCarreteiroCE = rateioFreteCarreteiroCE;
	}

	@Override
	public String toString() {
		return "ParcelaRateioFreteCarreteiroCE [idParcelaRateioFreteCarreteiroCE=" + idParcelaRateioFreteCarreteiroCE + ", tpValor=" + tpValor
				+ ", qtdTotal=" + qtdTotal + ", vlTotal=" + vlTotal + ", rateioFreteCarreteiroCE=" + rateioFreteCarreteiroCE + "]";
	}

	

}