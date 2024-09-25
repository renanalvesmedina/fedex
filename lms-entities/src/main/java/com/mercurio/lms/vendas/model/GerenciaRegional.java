package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class GerenciaRegional implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idGerenciaRegional;

    /** persistent field */
    private String dsGerenciaRegional;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private List municipioRegionalClientes;
    
    private List municipios;

    public Long getIdGerenciaRegional() {
        return this.idGerenciaRegional;
    }

    public void setIdGerenciaRegional(Long idGerenciaRegional) {
        this.idGerenciaRegional = idGerenciaRegional;
    }

    public String getDsGerenciaRegional() {
        return this.dsGerenciaRegional;
    }

    public void setDsGerenciaRegional(String dsGerenciaRegional) {
        this.dsGerenciaRegional = dsGerenciaRegional;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.MunicipioRegionalCliente.class)     
    public List getMunicipioRegionalClientes() {
        return this.municipioRegionalClientes;
    }

    public void setMunicipioRegionalClientes(List municipioRegionalClientes) {
        this.municipioRegionalClientes = municipioRegionalClientes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idGerenciaRegional",
				getIdGerenciaRegional()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof GerenciaRegional))
			return false;
        GerenciaRegional castOther = (GerenciaRegional) other;
		return new EqualsBuilder().append(this.getIdGerenciaRegional(),
				castOther.getIdGerenciaRegional()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGerenciaRegional())
            .toHashCode();
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Municipio.class)     
    public List getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List municipios) {
		this.municipios = municipios;
	}

}
