package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaCreditoParcela implements Serializable {

	private static final long serialVersionUID = 2L;

    /** identifier field */
    private Long idNotaCreditoParcela;

    /** persistent field */
    private BigDecimal qtNotaCreditoParcela;
    
    private Integer qtColeta;
    
    private Integer qtEntrega;

    /** persistent field */
    private BigDecimal vlNotaCreditoParcela;
   
    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe parcelaTabelaCe;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE faixaPesoParcelaTabelaCE;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito notaCredito;

    public Long getIdNotaCreditoParcela() {
        return this.idNotaCreditoParcela;
    }

    public void setIdNotaCreditoParcela(Long idNotaCreditoParcela) {
        this.idNotaCreditoParcela = idNotaCreditoParcela;
    }

    public BigDecimal getQtNotaCreditoParcela() {
        return this.qtNotaCreditoParcela;
    }

    public void setQtNotaCreditoParcela(BigDecimal qtNotaCreditoParcela) {
        this.qtNotaCreditoParcela = qtNotaCreditoParcela;
    }

    public BigDecimal getVlNotaCreditoParcela() {
        return this.vlNotaCreditoParcela;
    }

    public void setVlNotaCreditoParcela(BigDecimal vlNotaCreditoParcela) {
        this.vlNotaCreditoParcela = vlNotaCreditoParcela;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe getParcelaTabelaCe() {
        return this.parcelaTabelaCe;
    }

	public void setParcelaTabelaCe(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe parcelaTabelaCe) {
        this.parcelaTabelaCe = parcelaTabelaCe;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito getNotaCredito() {
        return this.notaCredito;
    }

	public void setNotaCredito(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito notaCredito) {
        this.notaCredito = notaCredito;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNotaCreditoParcela",
				getIdNotaCreditoParcela()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NotaCreditoParcela))
			return false;
        NotaCreditoParcela castOther = (NotaCreditoParcela) other;
		return new EqualsBuilder().append(this.getIdNotaCreditoParcela(),
				castOther.getIdNotaCreditoParcela()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNotaCreditoParcela())
            .toHashCode();
    }

    public Integer getQtColeta() {
		return qtColeta;
	}

	public void setQtColeta(Integer qtColeta) {
		this.qtColeta = qtColeta;
	}

	public Integer getQtEntrega() {
		return qtEntrega;
	}

	public void setQtEntrega(Integer qtEntrega) {
		this.qtEntrega = qtEntrega;
	}

	public com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE getFaixaPesoParcelaTabelaCE() {
        return this.faixaPesoParcelaTabelaCE;
}

	public void setFaixaPesoParcelaTabelaCE(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE faixaPesoParcelaTabelaCE) {
        this.faixaPesoParcelaTabelaCE = faixaPesoParcelaTabelaCE;
    }


}
