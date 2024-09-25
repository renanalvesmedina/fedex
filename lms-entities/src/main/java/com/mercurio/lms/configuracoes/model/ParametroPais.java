package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.municipios.model.Pais;

/** @author Hibernate CodeGenerator */
public class ParametroPais implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idParametroPais;

	/** persistent field */
	private Long nrUltimoCrt;

	private Pais pais;

	public Long getIdParametroPais() {
		return idParametroPais;
	}

	public void setIdParametroPais(Long idParametroPais) {
		this.idParametroPais = idParametroPais;
	}

	public Long getNrUltimoCrt() {
		return nrUltimoCrt;
	}

	public void setNrUltimoCrt(Long nrUltimoCrt) {
		this.nrUltimoCrt = nrUltimoCrt;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Pais.class) 
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idParametroPais",
				getIdParametroPais()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroPais))
			return false;
		ParametroPais castOther = (ParametroPais) other;
		return new EqualsBuilder().append(this.getIdParametroPais(),
				castOther.getIdParametroPais()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroPais()).toHashCode();
	}

}
