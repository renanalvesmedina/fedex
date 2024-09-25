package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ChequeFatura implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idChequeFatura;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Cheque cheque;

    public Long getIdChequeFatura() {
        return this.idChequeFatura;
    }

    public void setIdChequeFatura(Long idChequeFatura) {
        this.idChequeFatura = idChequeFatura;
    }

    public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
        return this.fatura;
    }

    public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
        this.fatura = fatura;
    }

    public com.mercurio.lms.contasreceber.model.Cheque getCheque() {
        return this.cheque;
    }

    public void setCheque(com.mercurio.lms.contasreceber.model.Cheque cheque) {
        this.cheque = cheque;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idChequeFatura",
				getIdChequeFatura()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ChequeFatura))
			return false;
        ChequeFatura castOther = (ChequeFatura) other;
		return new EqualsBuilder().append(this.getIdChequeFatura(),
				castOther.getIdChequeFatura()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdChequeFatura()).toHashCode();
    }

}
