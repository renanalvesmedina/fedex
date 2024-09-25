package com.mercurio.lms.integracao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MunicipioVinculo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
	private Long idMunicipioVinculo;
	
	/** persistent field */
	private com.mercurio.lms.municipios.model.Municipio municipioLms;

	/** persistent field */
	private com.mercurio.lms.integracao.model.MunicipioCorporativo municipioCorporativo;

	public Long getIdMunicipioVinculo() {
		return idMunicipioVinculo;
	}

	public void setIdMunicipioVinculo(Long idMunicipioVinculo) {
		this.idMunicipioVinculo = idMunicipioVinculo;
	}

	public com.mercurio.lms.municipios.model.Municipio getMunicipioLms() {
		return municipioLms;
	}

	public void setMunicipioLms(
			com.mercurio.lms.municipios.model.Municipio municipioLms) {
		this.municipioLms = municipioLms;
	}

    public com.mercurio.lms.integracao.model.MunicipioCorporativo getMunicipioCorporativo() {
		return municipioCorporativo;
	}

	public void setMunicipioCorporativo(
			com.mercurio.lms.integracao.model.MunicipioCorporativo municipioCorporativo) {
		this.municipioCorporativo = municipioCorporativo;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idMunicipioVinculo",
				getIdMunicipioVinculo()).toString();
    }
    
    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioVinculo))
			return false;
        MunicipioVinculo castOther = (MunicipioVinculo) other;
		return new EqualsBuilder().append(this.getIdMunicipioVinculo(),
				castOther.getIdMunicipioVinculo()).isEquals();
    }
    
    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioVinculo())
            .toHashCode();
    }
}
