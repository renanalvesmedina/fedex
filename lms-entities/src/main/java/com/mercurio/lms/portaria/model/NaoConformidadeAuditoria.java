package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class NaoConformidadeAuditoria implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNaoConformidadeAuditoria;

    /** persistent field */
    private com.mercurio.lms.portaria.model.RegistroAuditoria registroAuditoria;

    /** persistent field */
    private com.mercurio.lms.rnc.model.NaoConformidade naoConformidade;

    public Long getIdNaoConformidadeAuditoria() {
        return this.idNaoConformidadeAuditoria;
    }

    public void setIdNaoConformidadeAuditoria(Long idNaoConformidadeAuditoria) {
        this.idNaoConformidadeAuditoria = idNaoConformidadeAuditoria;
    }

    public com.mercurio.lms.portaria.model.RegistroAuditoria getRegistroAuditoria() {
        return this.registroAuditoria;
    }

	public void setRegistroAuditoria(
			com.mercurio.lms.portaria.model.RegistroAuditoria registroAuditoria) {
        this.registroAuditoria = registroAuditoria;
    }

    public com.mercurio.lms.rnc.model.NaoConformidade getNaoConformidade() {
        return this.naoConformidade;
    }

	public void setNaoConformidade(
			com.mercurio.lms.rnc.model.NaoConformidade naoConformidade) {
        this.naoConformidade = naoConformidade;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNaoConformidadeAuditoria",
				getIdNaoConformidadeAuditoria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NaoConformidadeAuditoria))
			return false;
        NaoConformidadeAuditoria castOther = (NaoConformidadeAuditoria) other;
		return new EqualsBuilder().append(this.getIdNaoConformidadeAuditoria(),
				castOther.getIdNaoConformidadeAuditoria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNaoConformidadeAuditoria())
            .toHashCode();
    }

}
