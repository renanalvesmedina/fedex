package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MeioTransportePeriferico implements Serializable {

	private static final long serialVersionUID = 1L;

    private Long idMeioTransportePeriferico;

    private com.mercurio.lms.contratacaoveiculos.model.PerifericoRastreador perifericoRastreador;
    
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    public Long getIdMeioTransportePeriferico() {
		return idMeioTransportePeriferico;
	}

	public void setIdMeioTransportePeriferico(Long idMeioTransportePeriferico) {
		this.idMeioTransportePeriferico = idMeioTransportePeriferico;
	}

	public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public com.mercurio.lms.contratacaoveiculos.model.PerifericoRastreador getPerifericoRastreador() {
		return perifericoRastreador;
	}

	public void setPerifericoRastreador(
			com.mercurio.lms.contratacaoveiculos.model.PerifericoRastreador perifericoRastreador) {
		this.perifericoRastreador = perifericoRastreador;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idMeioTransportePeriferico",
				getIdMeioTransportePeriferico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTransportePeriferico))
			return false;
        MeioTransportePeriferico castOther = (MeioTransportePeriferico) other;
		return new EqualsBuilder().append(this.getIdMeioTransportePeriferico(),
				castOther.getIdMeioTransportePeriferico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTransportePeriferico())
            .toHashCode();
    }
}
