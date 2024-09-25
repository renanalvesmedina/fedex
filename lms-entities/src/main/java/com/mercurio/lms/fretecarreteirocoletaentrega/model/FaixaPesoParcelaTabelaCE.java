package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class FaixaPesoParcelaTabelaCE implements Serializable, Comparable<FaixaPesoParcelaTabelaCE> {

	private static final long serialVersionUID = 1L;
	private Long idFaixaPesoParcelaTabelaCE;
	private TabelaColetaEntrega tabelaColetaEntrega;
	private BigDecimal psInicial;
	private BigDecimal psFinal;
	private BigDecimal vlValor;
	private String tpFator;

	public Long getIdFaixaPesoParcelaTabelaCE() {
		return idFaixaPesoParcelaTabelaCE;
	}

	public void setIdFaixaPesoParcelaTabelaCE(Long idFaixaPesoParcelaTabelaCE) {
		this.idFaixaPesoParcelaTabelaCE = idFaixaPesoParcelaTabelaCE;
	}

	public TabelaColetaEntrega getTabelaColetaEntrega() {
		return tabelaColetaEntrega;
	}

	public void setTabelaColetaEntrega(TabelaColetaEntrega tabelaColetaEntrega) {
		this.tabelaColetaEntrega = tabelaColetaEntrega;
	}

	public BigDecimal getPsInicial() {
		return psInicial;
	}

	public void setPsInicial(BigDecimal psInicial) {
		this.psInicial = psInicial;
	}

	public BigDecimal getPsFinal() {
		return psFinal;
	}

	public void setPsFinal(BigDecimal psFinal) {
		this.psFinal = psFinal;
	}

	public BigDecimal getVlValor() {
		return vlValor;
	}

	public void setVlValor(BigDecimal vlValor) {
		this.vlValor = vlValor;
	}

	public String getTpFator() {
		return tpFator;
	}

	public void setTpFator(String tpFator) {
		this.tpFator = tpFator;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idFaixaPesoParcelaTabelaCE()",
				getIdFaixaPesoParcelaTabelaCE()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaixaPesoParcelaTabelaCE))
			return false;
        FaixaPesoParcelaTabelaCE castOther = (FaixaPesoParcelaTabelaCE) other;
		return new EqualsBuilder().append(this.getIdFaixaPesoParcelaTabelaCE(),
				castOther.getIdFaixaPesoParcelaTabelaCE()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFaixaPesoParcelaTabelaCE())
            .toHashCode();
    }

   
    public int compareTo(FaixaPesoParcelaTabelaCE o) {
        if (o != null && o.psFinal != null && psFinal == null) {
            return 1;
        } else if ((o == null || o.psFinal == null) && psFinal != null) {
            return -1;
        } else if (o != null && o.psFinal != null && psFinal != null) {
            return o.psFinal.compareTo(psFinal);
        }

        return 0;
    }

}
