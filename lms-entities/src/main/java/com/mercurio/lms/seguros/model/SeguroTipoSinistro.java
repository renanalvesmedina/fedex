package com.mercurio.lms.seguros.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class SeguroTipoSinistro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSeguroTipoSinistro;

    /** persistent field */
    private com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro;

    /** persistent field */
    private com.mercurio.lms.seguros.model.TipoSinistro tipoSinistro;

    public Long getIdSeguroTipoSinistro() {
        return this.idSeguroTipoSinistro;
    }

    public void setIdSeguroTipoSinistro(Long idSeguroTipoSinistro) {
        this.idSeguroTipoSinistro = idSeguroTipoSinistro;
    }

    public com.mercurio.lms.seguros.model.TipoSeguro getTipoSeguro() {
        return this.tipoSeguro;
    }

	public void setTipoSeguro(
			com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public com.mercurio.lms.seguros.model.TipoSinistro getTipoSinistro() {
        return this.tipoSinistro;
    }

	public void setTipoSinistro(
			com.mercurio.lms.seguros.model.TipoSinistro tipoSinistro) {
        this.tipoSinistro = tipoSinistro;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSeguroTipoSinistro",
				getIdSeguroTipoSinistro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SeguroTipoSinistro))
			return false;
        SeguroTipoSinistro castOther = (SeguroTipoSinistro) other;
		return new EqualsBuilder().append(this.getIdSeguroTipoSinistro(),
				castOther.getIdSeguroTipoSinistro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSeguroTipoSinistro())
            .toHashCode();
    }

}
