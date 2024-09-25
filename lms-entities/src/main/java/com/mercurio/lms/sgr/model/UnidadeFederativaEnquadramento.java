package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

public class UnidadeFederativaEnquadramento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idUnidadeFederativaEnquadramento;
	private UnidadeFederativa unidadeFederativa;
	private DomainValue tpInfluenciaMunicipio;
	private EnquadramentoRegra enquadramentoRegra;

	public Long getIdUnidadeFederativaEnquadramento() {
		return this.idUnidadeFederativaEnquadramento;
	}

	public void setIdUnidadeFederativaEnquadramento(Long idUnidadeFederativaEnquadramento) {
		this.idUnidadeFederativaEnquadramento = idUnidadeFederativaEnquadramento;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return this.unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public DomainValue getTpInfluenciaMunicipio() {
		return tpInfluenciaMunicipio;
	}

	public void setTpInfluenciaMunicipio(DomainValue tpInfluenciaMunicipio) {
		this.tpInfluenciaMunicipio = tpInfluenciaMunicipio;
	}

	public EnquadramentoRegra getEnquadramentoRegra() {
		return this.enquadramentoRegra;
	}

	public void setEnquadramentoRegra(EnquadramentoRegra enquadramentoRegra) {
		this.enquadramentoRegra = enquadramentoRegra;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idUnidadeFederativaEnquadramento)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof UnidadeFederativaEnquadramento)) {
			return false;
		}
		UnidadeFederativaEnquadramento cast = (UnidadeFederativaEnquadramento) other;
		return new EqualsBuilder()
				.append(idUnidadeFederativaEnquadramento, cast.idUnidadeFederativaEnquadramento)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idUnidadeFederativaEnquadramento)
				.toHashCode();
	}

}
