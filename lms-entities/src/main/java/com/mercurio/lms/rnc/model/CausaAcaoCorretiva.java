package com.mercurio.lms.rnc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class CausaAcaoCorretiva implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCausaAcaoCorretiva;

    /** persistent field */
    private com.mercurio.lms.rnc.model.CausaNaoConformidade causaNaoConformidade;

    /** persistent field */
    private com.mercurio.lms.rnc.model.AcaoCorretiva acaoCorretiva;

    public Long getIdCausaAcaoCorretiva() {
        return this.idCausaAcaoCorretiva;
    }

    public void setIdCausaAcaoCorretiva(Long idCausaAcaoCorretiva) {
        this.idCausaAcaoCorretiva = idCausaAcaoCorretiva;
    }

    public com.mercurio.lms.rnc.model.CausaNaoConformidade getCausaNaoConformidade() {
        return this.causaNaoConformidade;
    }

	public void setCausaNaoConformidade(
			com.mercurio.lms.rnc.model.CausaNaoConformidade causaNaoConformidade) {
        this.causaNaoConformidade = causaNaoConformidade;
    }

    public com.mercurio.lms.rnc.model.AcaoCorretiva getAcaoCorretiva() {
        return this.acaoCorretiva;
    }

	public void setAcaoCorretiva(
			com.mercurio.lms.rnc.model.AcaoCorretiva acaoCorretiva) {
        this.acaoCorretiva = acaoCorretiva;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCausaAcaoCorretiva",
				getIdCausaAcaoCorretiva()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CausaAcaoCorretiva))
			return false;
        CausaAcaoCorretiva castOther = (CausaAcaoCorretiva) other;
		return new EqualsBuilder().append(this.getIdCausaAcaoCorretiva(),
				castOther.getIdCausaAcaoCorretiva()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCausaAcaoCorretiva())
            .toHashCode();
    }

}
