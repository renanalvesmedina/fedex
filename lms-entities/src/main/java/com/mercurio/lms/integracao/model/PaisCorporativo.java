package com.mercurio.lms.integracao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

public class PaisCorporativo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
	private Long idPais;
	
	/** persistent field */
	private String nmPais;
	
	/** persistent field */
	private Integer cdIso;

    /** persistent field */
    private List municipios;
    
	public Long getIdPais() {
		return idPais;
	}

	public void setIdPais(Long idPais) {
		this.idPais = idPais;
	}
	
	public String getNmPais() {
		return nmPais;
	}

	public void setNmPais(String nmPais) {
		this.nmPais = nmPais;
	}

	public Integer getCdIso() {
		return cdIso;
	}

	public void setCdIso(Integer cdIso) {
		this.cdIso = cdIso;
	}
	
    @ParametrizedAttribute(type = com.mercurio.lms.integracao.model.MunicipioCorporativo.class)     
    public List getMunicipios() {
        return this.municipios;
    }

    public void setMunicipios(List municipios) {
        this.municipios = municipios;
    }

	public String toString() {
		return new ToStringBuilder(this).append("idMunicipio", getIdPais())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PaisCorporativo))
			return false;
        PaisCorporativo castOther = (PaisCorporativo) other;
		return new EqualsBuilder().append(this.getIdPais(),
				castOther.getIdPais()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPais()).toHashCode();
    }	
}
