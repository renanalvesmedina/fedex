package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Pais;

public class PaisEnquadramento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idPaisEnquadramento;
	private Pais pais;
	private DomainValue tpInfluenciaMunicipio;
	private EnquadramentoRegra enquadramentoRegra;

	public Long getIdPaisEnquadramento() {
		return idPaisEnquadramento;
	}

	public void setIdPaisEnquadramento(Long idPaisEnquadramento) {
		this.idPaisEnquadramento = idPaisEnquadramento;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public DomainValue getTpInfluenciaMunicipio() {
		return tpInfluenciaMunicipio;
	}

	public void setTpInfluenciaMunicipio(DomainValue tpInfluenciaMunicipio) {
		this.tpInfluenciaMunicipio = tpInfluenciaMunicipio;
	}

	public EnquadramentoRegra getEnquadramentoRegra() {
		return enquadramentoRegra;
	}

	public void setEnquadramentoRegra(EnquadramentoRegra enquadramentoRegra) {
		this.enquadramentoRegra = enquadramentoRegra;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idPaisEnquadramento)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof PaisEnquadramento)) {
			return false;
		}
		PaisEnquadramento cast = (PaisEnquadramento) other;
		return new EqualsBuilder()
				.append(idPaisEnquadramento, cast.idPaisEnquadramento)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idPaisEnquadramento)
				.toHashCode();
	}

}
