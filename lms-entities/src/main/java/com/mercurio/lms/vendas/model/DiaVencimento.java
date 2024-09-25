package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DiaVencimento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDiaVencimento;

    /** nullable persistent field */
    private Byte nrDiaVencimento;

    /** persistent field */
    private com.mercurio.lms.vendas.model.PrazoVencimento prazoVencimento;

    public Long getIdDiaVencimento() {
        return this.idDiaVencimento;
    }

    public void setIdDiaVencimento(Long idDiaVencimento) {
        this.idDiaVencimento = idDiaVencimento;
    }

    public Byte getNrDiaVencimento() {
        return this.nrDiaVencimento;
    }

    public void setNrDiaVencimento(Byte nrDiaVencimento) {
        this.nrDiaVencimento = nrDiaVencimento;
    }

    public com.mercurio.lms.vendas.model.PrazoVencimento getPrazoVencimento() {
        return this.prazoVencimento;
    }

	public void setPrazoVencimento(
			com.mercurio.lms.vendas.model.PrazoVencimento prazoVencimento) {
        this.prazoVencimento = prazoVencimento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDiaVencimento",
				getIdDiaVencimento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DiaVencimento))
			return false;
        DiaVencimento castOther = (DiaVencimento) other;
		return new EqualsBuilder().append(this.getIdDiaVencimento(),
				castOther.getIdDiaVencimento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDiaVencimento()).toHashCode();
    }

}
