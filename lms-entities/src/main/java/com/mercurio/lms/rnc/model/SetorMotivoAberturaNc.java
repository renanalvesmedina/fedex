package com.mercurio.lms.rnc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class SetorMotivoAberturaNc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSetorMotivoAberturaNc;

    /** persistent field */
    private com.mercurio.lms.rnc.model.MotivoAberturaNc motivoAberturaNc;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Setor setor;

    public Long getIdSetorMotivoAberturaNc() {
        return this.idSetorMotivoAberturaNc;
    }

    public void setIdSetorMotivoAberturaNc(Long idSetorMotivoAberturaNc) {
        this.idSetorMotivoAberturaNc = idSetorMotivoAberturaNc;
    }

    public com.mercurio.lms.rnc.model.MotivoAberturaNc getMotivoAberturaNc() {
        return this.motivoAberturaNc;
    }

	public void setMotivoAberturaNc(
			com.mercurio.lms.rnc.model.MotivoAberturaNc motivoAberturaNc) {
        this.motivoAberturaNc = motivoAberturaNc;
    }

    public com.mercurio.lms.configuracoes.model.Setor getSetor() {
        return this.setor;
    }

    public void setSetor(com.mercurio.lms.configuracoes.model.Setor setor) {
        this.setor = setor;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSetorMotivoAberturaNc",
				getIdSetorMotivoAberturaNc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SetorMotivoAberturaNc))
			return false;
        SetorMotivoAberturaNc castOther = (SetorMotivoAberturaNc) other;
		return new EqualsBuilder().append(this.getIdSetorMotivoAberturaNc(),
				castOther.getIdSetorMotivoAberturaNc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSetorMotivoAberturaNc())
            .toHashCode();
    }

}
