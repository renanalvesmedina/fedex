package com.mercurio.lms.seguros.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReguladoraSeguradora implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idReguladoraSeguradora;

    /** persistent field */
    private com.mercurio.lms.seguros.model.ReguladoraSeguro reguladoraSeguro;

    /** persistent field */
    private com.mercurio.lms.seguros.model.Seguradora seguradora;

    public Long getIdReguladoraSeguradora() {
        return this.idReguladoraSeguradora;
    }

    public void setIdReguladoraSeguradora(Long idReguladoraSeguradora) {
        this.idReguladoraSeguradora = idReguladoraSeguradora;
    }

    public com.mercurio.lms.seguros.model.ReguladoraSeguro getReguladoraSeguro() {
        return this.reguladoraSeguro;
    }

	public void setReguladoraSeguro(
			com.mercurio.lms.seguros.model.ReguladoraSeguro reguladoraSeguro) {
        this.reguladoraSeguro = reguladoraSeguro;
    }

    public com.mercurio.lms.seguros.model.Seguradora getSeguradora() {
        return this.seguradora;
    }

	public void setSeguradora(
			com.mercurio.lms.seguros.model.Seguradora seguradora) {
        this.seguradora = seguradora;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idReguladoraSeguradora",
				getIdReguladoraSeguradora()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReguladoraSeguradora))
			return false;
        ReguladoraSeguradora castOther = (ReguladoraSeguradora) other;
		return new EqualsBuilder().append(this.getIdReguladoraSeguradora(),
				castOther.getIdReguladoraSeguradora()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReguladoraSeguradora())
            .toHashCode();
    }

}
