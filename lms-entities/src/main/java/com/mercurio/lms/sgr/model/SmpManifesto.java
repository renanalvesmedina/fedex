package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.carregamento.model.Manifesto;

public class SmpManifesto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idSmpManifesto;
	private Manifesto manifesto;
	private SolicMonitPreventivo solicMonitPreventivo;

	public Long getIdSmpManifesto() {
		return idSmpManifesto;
	}

	public void setIdSmpManifesto(Long idSmpManifesto) {
		this.idSmpManifesto = idSmpManifesto;
	}

	public Manifesto getManifesto() {
		return manifesto;
	}

	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}

	public SolicMonitPreventivo getSolicMonitPreventivo() {
		return solicMonitPreventivo;
	}

	public void setSolicMonitPreventivo(SolicMonitPreventivo solicMonitPreventivo) {
		this.solicMonitPreventivo = solicMonitPreventivo;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idSmpManifesto)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof SmpManifesto)) {
			return false;
		}
		SmpManifesto cast = (SmpManifesto) other;
		return new EqualsBuilder()
				.append(idSmpManifesto, cast.idSmpManifesto)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idSmpManifesto)
				.toHashCode();
	}

}
