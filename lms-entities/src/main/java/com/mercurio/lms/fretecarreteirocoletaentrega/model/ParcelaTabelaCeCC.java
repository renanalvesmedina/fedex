package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ParcelaTabelaCeCC implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long idParcelaTabelaCeCC;
	private TabelaColetaEntregaCC tabelaColetaEntregaCC;
	private ParcelaTabelaCe parcelaTabelaCe;
	private BigDecimal qtParcela;
	private BigDecimal vlParcela;
	private FaixaPesoParcelaTabelaCE faixaPesoParcelaTabelaCE;
	
	public Long getIdParcelaTabelaCeCC() {
		return idParcelaTabelaCeCC;
	}

	public void setIdParcelaTabelaCeCC(Long idParcelaTabelaCeCC) {
		this.idParcelaTabelaCeCC = idParcelaTabelaCeCC;
	}

	public TabelaColetaEntregaCC getTabelaColetaEntregaCC() {
		return tabelaColetaEntregaCC;
	}

	public void setTabelaColetaEntregaCC(
			TabelaColetaEntregaCC tabelaColetaEntregaCC) {
		this.tabelaColetaEntregaCC = tabelaColetaEntregaCC;
	}

	public ParcelaTabelaCe getParcelaTabelaCe() {
		return parcelaTabelaCe;
	}

	public void setParcelaTabelaCe(ParcelaTabelaCe parcelaTabelaCe) {
		this.parcelaTabelaCe = parcelaTabelaCe;
	}

	public BigDecimal getQtParcela() {
		return qtParcela;
	}

	public void setQtParcela(BigDecimal qtParcela) {
		this.qtParcela = qtParcela;
	}

	public BigDecimal getVlParcela() {
		return vlParcela;
	}

	public void setVlParcela(BigDecimal vlParcela) {
		this.vlParcela = vlParcela;
	}

	public void setFaixaPesoParcelaTabelaCE(
			FaixaPesoParcelaTabelaCE faixaPesoParcelaTabelaCE) {
		this.faixaPesoParcelaTabelaCE = faixaPesoParcelaTabelaCE;
	}

	public FaixaPesoParcelaTabelaCE getFaixaPesoParcelaTabelaCE() {
		return faixaPesoParcelaTabelaCE;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idTabelaColetaEntregaCC()",
				getIdParcelaTabelaCeCC()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParcelaTabelaCeCC))
			return false;
        ParcelaTabelaCeCC castOther = (ParcelaTabelaCeCC) other;
		return new EqualsBuilder().append(this.getIdParcelaTabelaCeCC(),
				castOther.getIdParcelaTabelaCeCC()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParcelaTabelaCeCC())
            .toHashCode();
    }
}
