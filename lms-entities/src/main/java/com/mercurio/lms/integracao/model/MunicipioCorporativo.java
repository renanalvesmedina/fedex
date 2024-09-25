package com.mercurio.lms.integracao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MunicipioCorporativo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
	private Long idMunicipio;
	
	/** persistent field */
	private String nmMunicipio;
	
	/** persistent field */
	private String nrCep;
	
	/** persistent field */
	private Integer cdIbge;
	
	/** persistent field */
	private String sgUnidadeFederativa;

    /** persistent field */
    private com.mercurio.lms.integracao.model.PaisCorporativo pais;

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public String getNmMunicipio() {
		return nmMunicipio;
	}

	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}

	public String getNrCep() {
		return nrCep;
	}

	public void setNrCep(String nrCep) {
		this.nrCep = nrCep;
	}

	public Integer getCdIbge() {
		return cdIbge;
	}

	public void setCdIbge(Integer cdIbge) {
		this.cdIbge = cdIbge;
	}
	
	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}

	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}
	
    public com.mercurio.lms.integracao.model.PaisCorporativo getPais() {
		return pais;
	}

	public void setPais(com.mercurio.lms.integracao.model.PaisCorporativo pais) {
		this.pais = pais;
	}

	public String toString() {
        return new ToStringBuilder(this)
				.append("idMunicipio", getIdMunicipio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioCorporativo))
			return false;
        MunicipioCorporativo castOther = (MunicipioCorporativo) other;
		return new EqualsBuilder().append(this.getIdMunicipio(),
				castOther.getIdMunicipio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipio()).toHashCode();
    }
	
}
