package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.seguros.model.ReguladoraSeguro;

public class EscoltaReguladora implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idEscoltaReguladora;
	private ReguladoraSeguro reguladoraSeguro;
	private Escolta escolta;

	public Long getIdEscoltaReguladora() {
		return idEscoltaReguladora;
	}

	public void setIdEscoltaReguladora(Long idEscoltaReguladora) {
		this.idEscoltaReguladora = idEscoltaReguladora;
	}

	public ReguladoraSeguro getReguladoraSeguro() {
		return reguladoraSeguro;
	}

	public void setReguladoraSeguro(ReguladoraSeguro reguladoraSeguro) {
		this.reguladoraSeguro = reguladoraSeguro;
	}

	public Escolta getEscolta() {
		return escolta;
	}

	public void setEscolta(Escolta escolta) {
		this.escolta = escolta;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idEscoltaReguladora)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof EscoltaReguladora)) {
			return false;
		}
		EscoltaReguladora cast = (EscoltaReguladora) other;
		return new EqualsBuilder()
				.append(idEscoltaReguladora, cast.idEscoltaReguladora)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idEscoltaReguladora)
				.toHashCode();
	}

}
