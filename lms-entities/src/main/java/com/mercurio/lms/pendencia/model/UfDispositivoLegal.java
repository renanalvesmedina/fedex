package com.mercurio.lms.pendencia.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class UfDispositivoLegal implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idUfDispositivoLegal;

    /** persistent field */
    private String dsDispositivoLegal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;

    public Long getIdUfDispositivoLegal() {
        return this.idUfDispositivoLegal;
    }

    public void setIdUfDispositivoLegal(Long idUfDispositivoLegal) {
        this.idUfDispositivoLegal = idUfDispositivoLegal;
    }

    public String getDsDispositivoLegal() {
        return this.dsDispositivoLegal;
    }

    public void setDsDispositivoLegal(String dsDispositivoLegal) {
        this.dsDispositivoLegal = dsDispositivoLegal;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idUfDispositivoLegal",
				getIdUfDispositivoLegal()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof UfDispositivoLegal))
			return false;
        UfDispositivoLegal castOther = (UfDispositivoLegal) other;
		return new EqualsBuilder().append(this.getIdUfDispositivoLegal(),
				castOther.getIdUfDispositivoLegal()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdUfDispositivoLegal())
            .toHashCode();
    }

}
