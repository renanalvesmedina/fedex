package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mercurio.lms.contratacaoveiculos.model.PerifericoRastreador;

public class PerifericoExigenciaGerRisco implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idPerifericoExigencia;
	private PerifericoRastreador perifericoRastreador;
	@JsonIgnore
	private ExigenciaGerRisco exigenciaGerRisco;

	public Long getIdPerifericoExigencia() {
		return idPerifericoExigencia;
	}

	public void setIdPerifericoExigencia(Long idPerifericoExigencia) {
		this.idPerifericoExigencia = idPerifericoExigencia;
	}

	public PerifericoRastreador getPerifericoRastreador() {
		return perifericoRastreador;
	}

	public void setPerifericoRastreador(PerifericoRastreador perifericoRastreador) {
		this.perifericoRastreador = perifericoRastreador;
	}

	public ExigenciaGerRisco getExigenciaGerRisco() {
		return exigenciaGerRisco;
	}
	
	public void setExigenciaGerRisco(ExigenciaGerRisco exigenciaGerRisco) {
		this.exigenciaGerRisco = exigenciaGerRisco;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(idPerifericoExigencia)
				.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof PerifericoExigenciaGerRisco)) {
			return false;
		}
		PerifericoExigenciaGerRisco cast = (PerifericoExigenciaGerRisco) other;
		return new EqualsBuilder()
				.append(idPerifericoExigencia, cast.idPerifericoExigencia)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(idPerifericoExigencia)
				.toHashCode();
	}

}
