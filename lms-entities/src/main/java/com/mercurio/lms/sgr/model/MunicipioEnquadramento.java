package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Municipio;

public class MunicipioEnquadramento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idMunicipioEnquadramento;
	private DomainValue tpInfluenciaMunicipio;
	private Municipio municipio;
	private EnquadramentoRegra enquadramentoRegra;

	public Long getIdMunicipioEnquadramento() {
		return idMunicipioEnquadramento;
	}

	public void setIdMunicipioEnquadramento(Long idMunicipioEnquadramento) {
		this.idMunicipioEnquadramento = idMunicipioEnquadramento;
	}

	public DomainValue getTpInfluenciaMunicipio() {
		return tpInfluenciaMunicipio;
	}

	public void setTpInfluenciaMunicipio(DomainValue tpInfluenciaMunicipio) {
		this.tpInfluenciaMunicipio = tpInfluenciaMunicipio;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public EnquadramentoRegra getEnquadramentoRegra() {
		return enquadramentoRegra;
	}

	public void setEnquadramentoRegra(EnquadramentoRegra enquadramentoRegra) {
		this.enquadramentoRegra = enquadramentoRegra;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idMunicipioEnquadramento)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof MunicipioEnquadramento)) {
			return false;
		}
		MunicipioEnquadramento cast = (MunicipioEnquadramento) other;
		return new EqualsBuilder()
				.append(idMunicipioEnquadramento, cast.idMunicipioEnquadramento)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idMunicipioEnquadramento)
				.toHashCode();
	}

}
