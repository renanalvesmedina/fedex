package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class MunicipioRegionalCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMunicipioRegionalCliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.vendas.model.GerenciaRegional gerenciaRegional;

    public Long getIdMunicipioRegionalCliente() {
        return this.idMunicipioRegionalCliente;
    }

    public void setIdMunicipioRegionalCliente(Long idMunicipioRegionalCliente) {
        this.idMunicipioRegionalCliente = idMunicipioRegionalCliente;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.vendas.model.GerenciaRegional getGerenciaRegional() {
        return this.gerenciaRegional;
    }

	public void setGerenciaRegional(
			com.mercurio.lms.vendas.model.GerenciaRegional gerenciaRegional) {
        this.gerenciaRegional = gerenciaRegional;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMunicipioRegionalCliente",
				getIdMunicipioRegionalCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioRegionalCliente))
			return false;
        MunicipioRegionalCliente castOther = (MunicipioRegionalCliente) other;
		return new EqualsBuilder().append(this.getIdMunicipioRegionalCliente(),
				castOther.getIdMunicipioRegionalCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioRegionalCliente())
            .toHashCode();
    }

}
