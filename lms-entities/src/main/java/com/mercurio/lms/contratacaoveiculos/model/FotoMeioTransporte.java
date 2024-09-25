package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FotoMeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFotoMeioTransporte;

    /** persistent field */
    private byte[] imMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    public Long getIdFotoMeioTransporte() {
        return this.idFotoMeioTransporte;
    }

    public void setIdFotoMeioTransporte(Long idFotoMeioTransporte) {
        this.idFotoMeioTransporte = idFotoMeioTransporte;
    }

    public byte[] getImMeioTransporte() {
        return this.imMeioTransporte;
    }

    public void setImMeioTransporte(byte[] imMeioTransporte) {
        this.imMeioTransporte = imMeioTransporte;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFotoMeioTransporte",
				getIdFotoMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FotoMeioTransporte))
			return false;
        FotoMeioTransporte castOther = (FotoMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdFotoMeioTransporte(),
				castOther.getIdFotoMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFotoMeioTransporte())
            .toHashCode();
    }

}
