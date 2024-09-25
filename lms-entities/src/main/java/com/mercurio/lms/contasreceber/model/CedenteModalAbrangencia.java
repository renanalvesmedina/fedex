package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class CedenteModalAbrangencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCedenteModalAbrangencia;

    /** persistent field */
    private DomainValue tpModal;

    /** persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Cedente cedente;

    public Long getIdCedenteModalAbrangencia() {
        return this.idCedenteModalAbrangencia;
    }

    public void setIdCedenteModalAbrangencia(Long idCedenteModalAbrangencia) {
        this.idCedenteModalAbrangencia = idCedenteModalAbrangencia;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public com.mercurio.lms.contasreceber.model.Cedente getCedente() {
        return this.cedente;
    }

    public void setCedente(com.mercurio.lms.contasreceber.model.Cedente cedente) {
        this.cedente = cedente;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCedenteModalAbrangencia",
				getIdCedenteModalAbrangencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CedenteModalAbrangencia))
			return false;
        CedenteModalAbrangencia castOther = (CedenteModalAbrangencia) other;
		return new EqualsBuilder().append(this.getIdCedenteModalAbrangencia(),
				castOther.getIdCedenteModalAbrangencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCedenteModalAbrangencia())
            .toHashCode();
    }

}
