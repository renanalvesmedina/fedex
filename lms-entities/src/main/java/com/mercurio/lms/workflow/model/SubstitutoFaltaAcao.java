package com.mercurio.lms.workflow.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class SubstitutoFaltaAcao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSubstitutoFaltaAcao;

    /** persistent field */
    private com.mercurio.lms.workflow.model.Acao acao;

    /** persistent field */
    private com.mercurio.lms.workflow.model.SubstitutoFalta substitutoFalta;

    public Long getIdSubstitutoFaltaAcao() {
        return this.idSubstitutoFaltaAcao;
    }

    public void setIdSubstitutoFaltaAcao(Long idSubstitutoFaltaAcao) {
        this.idSubstitutoFaltaAcao = idSubstitutoFaltaAcao;
    }

    public com.mercurio.lms.workflow.model.Acao getAcao() {
        return this.acao;
    }

    public void setAcao(com.mercurio.lms.workflow.model.Acao acao) {
        this.acao = acao;
    }

    public com.mercurio.lms.workflow.model.SubstitutoFalta getSubstitutoFalta() {
        return this.substitutoFalta;
    }

	public void setSubstitutoFalta(
			com.mercurio.lms.workflow.model.SubstitutoFalta substitutoFalta) {
        this.substitutoFalta = substitutoFalta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSubstitutoFaltaAcao",
				getIdSubstitutoFaltaAcao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SubstitutoFaltaAcao))
			return false;
        SubstitutoFaltaAcao castOther = (SubstitutoFaltaAcao) other;
		return new EqualsBuilder().append(this.getIdSubstitutoFaltaAcao(),
				castOther.getIdSubstitutoFaltaAcao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSubstitutoFaltaAcao())
            .toHashCode();
    }

}
