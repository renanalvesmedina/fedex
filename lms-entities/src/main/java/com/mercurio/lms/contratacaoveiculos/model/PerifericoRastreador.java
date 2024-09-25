package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

public class PerifericoRastreador implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idPerifericoRastreador;
	private DomainValue tpSituacao;
	private VarcharI18n dsPerifericoRastreador;
	@JsonIgnore
	private List<MeioTransportePeriferico> meioTransportePerifericos;

	public Long getIdPerifericoRastreador() {
		return idPerifericoRastreador;
	}

	public void setIdPerifericoRastreador(Long idPerifericoRastreador) {
		this.idPerifericoRastreador = idPerifericoRastreador;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public VarcharI18n getDsPerifericoRastreador() {
		return dsPerifericoRastreador;
	}

	public void setDsPerifericoRastreador(VarcharI18n dsPerifericoRastreador) {
		this.dsPerifericoRastreador = dsPerifericoRastreador;
	}

	@ParametrizedAttribute(type = MeioTransportePeriferico.class)
	public List<MeioTransportePeriferico> getMeioTransportePerifericos() {
		return meioTransportePerifericos;
	}

	public void setMeioTransportePerifericos(List<MeioTransportePeriferico> meioTransportePerifericos) {
		this.meioTransportePerifericos = meioTransportePerifericos;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idPerifericoRastreador)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof PerifericoRastreador)) {
			return false;
		}
		PerifericoRastreador cast = (PerifericoRastreador) other;
		return new EqualsBuilder()
				.append(idPerifericoRastreador, cast.idPerifericoRastreador)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idPerifericoRastreador)
				.toHashCode();
	}

}
