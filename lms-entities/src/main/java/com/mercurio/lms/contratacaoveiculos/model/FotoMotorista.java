package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FotoMotorista implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFotoMotorista;

    /** persistent field */
    private byte[] imFotoMotorista;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Motorista motorista;

    public Long getIdFotoMotorista() {
        return this.idFotoMotorista;
    }

    public void setIdFotoMotorista(Long idFotoMotorista) {
        this.idFotoMotorista = idFotoMotorista;
    }

    public byte[] getFotoMotorista() {
        return this.imFotoMotorista;
    }

    public void setFotoMotorista(byte[] imFotoMotorista) {
        this.imFotoMotorista = imFotoMotorista;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Motorista getMotorista() {
        return this.motorista;
    }

	public void setMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista motorista) {
        this.motorista = motorista;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFotoMotorista",
				getIdFotoMotorista()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FotoMotorista))
			return false;
        FotoMotorista castOther = (FotoMotorista) other;
		return new EqualsBuilder().append(this.getIdFotoMotorista(),
				castOther.getIdFotoMotorista()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFotoMotorista()).toHashCode();
    }

	public byte[] getImFotoMotorista() {
		return imFotoMotorista;
	}

	public void setImFotoMotorista(byte[] imFotoMotorista) {
		this.imFotoMotorista = imFotoMotorista;
	}

}
