package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

public class FilialEnquadramento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idFilialEnquadramento;
	private Filial filial;
	private DomainValue tpInfluenciaMunicipio;
	private EnquadramentoRegra enquadramentoRegra;

	public Long getIdFilialEnquadramento() {
		return idFilialEnquadramento;
	}

	public void setIdFilialEnquadramento(Long idFilialEnquadramento) {
		this.idFilialEnquadramento = idFilialEnquadramento;
	}

	public EnquadramentoRegra getEnquadramentoRegra() {
		return enquadramentoRegra;
	}

	public void setEnquadramentoRegra(EnquadramentoRegra enquadramentoRegra) {
		this.enquadramentoRegra = enquadramentoRegra;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public DomainValue getTpInfluenciaMunicipio() {
		return tpInfluenciaMunicipio;
	}

	public void setTpInfluenciaMunicipio(DomainValue tpInfluenciaMunicipio) {
		this.tpInfluenciaMunicipio = tpInfluenciaMunicipio;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idFilialEnquadramento)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FilialEnquadramento)) {
			return false;
		}
		FilialEnquadramento cast = (FilialEnquadramento) other;
		return new EqualsBuilder()
				.append(idFilialEnquadramento, cast.idFilialEnquadramento)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFilialEnquadramento)
				.toHashCode();
	}

}
