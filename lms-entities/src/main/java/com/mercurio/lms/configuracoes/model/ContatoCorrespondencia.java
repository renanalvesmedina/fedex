package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ContatoCorrespondencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idContatoCorrespondencia;

    /** persistent field */
    private DomainValue tpCorrespondencia;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Contato contato;

    public Long getIdContatoCorrespondencia() {
        return this.idContatoCorrespondencia;
    }

    public void setIdContatoCorrespondencia(Long idContatoCorrespondencia) {
        this.idContatoCorrespondencia = idContatoCorrespondencia;
    }

    public DomainValue getTpCorrespondencia() {
        return this.tpCorrespondencia;
    }

    public void setTpCorrespondencia(DomainValue tpCorrespondencia) {
        this.tpCorrespondencia = tpCorrespondencia;
    }

    public com.mercurio.lms.configuracoes.model.Contato getContato() {
        return this.contato;
    }

    public void setContato(com.mercurio.lms.configuracoes.model.Contato contato) {
        this.contato = contato;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idContatoCorrespondencia",
				getIdContatoCorrespondencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ContatoCorrespondencia))
			return false;
        ContatoCorrespondencia castOther = (ContatoCorrespondencia) other;
		return new EqualsBuilder().append(this.getIdContatoCorrespondencia(),
				castOther.getIdContatoCorrespondencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdContatoCorrespondencia())
            .toHashCode();
    }

}
