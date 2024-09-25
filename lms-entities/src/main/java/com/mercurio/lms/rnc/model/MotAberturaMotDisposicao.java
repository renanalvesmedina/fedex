package com.mercurio.lms.rnc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotAberturaMotDisposicao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotAberturaMotDisposicao;

    /** persistent field */
    private com.mercurio.lms.rnc.model.MotivoAberturaNc motivoAberturaNc;

    /** persistent field */
    private com.mercurio.lms.rnc.model.MotivoDisposicao motivoDisposicao;

    public Long getIdMotAberturaMotDisposicao() {
        return this.idMotAberturaMotDisposicao;
    }

    public void setIdMotAberturaMotDisposicao(Long idMotAberturaMotDisposicao) {
        this.idMotAberturaMotDisposicao = idMotAberturaMotDisposicao;
    }

    public com.mercurio.lms.rnc.model.MotivoAberturaNc getMotivoAberturaNc() {
        return this.motivoAberturaNc;
    }

	public void setMotivoAberturaNc(
			com.mercurio.lms.rnc.model.MotivoAberturaNc motivoAberturaNc) {
        this.motivoAberturaNc = motivoAberturaNc;
    }

    public com.mercurio.lms.rnc.model.MotivoDisposicao getMotivoDisposicao() {
        return this.motivoDisposicao;
    }

	public void setMotivoDisposicao(
			com.mercurio.lms.rnc.model.MotivoDisposicao motivoDisposicao) {
        this.motivoDisposicao = motivoDisposicao;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotAberturaMotDisposicao",
				getIdMotAberturaMotDisposicao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotAberturaMotDisposicao))
			return false;
        MotAberturaMotDisposicao castOther = (MotAberturaMotDisposicao) other;
		return new EqualsBuilder().append(this.getIdMotAberturaMotDisposicao(),
				castOther.getIdMotAberturaMotDisposicao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotAberturaMotDisposicao())
            .toHashCode();
    }

}
