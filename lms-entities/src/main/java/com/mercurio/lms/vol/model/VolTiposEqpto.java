package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolTiposEqpto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoEqpto;

    /** persistent field */
    private String dsNome;

    /** persistent field */
    private List volModeloseqps;
    
    public Long getIdTipoEqpto() {
        return this.idTipoEqpto;
    }

    public void setIdTipoEqpto(Long idTipoEqpto) {
        this.idTipoEqpto = idTipoEqpto;
    }

    public String getDsNome() {
        return this.dsNome;
    }

    public void setDsNome(String dsNome) {
        this.dsNome = dsNome;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolModeloseqps.class)     
    public List getVolModeloseqps() {
        return this.volModeloseqps;
    }

    public void setVolModeloseqps(List volModeloseqps) {
        this.volModeloseqps = volModeloseqps;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idTipoEqpto", getIdTipoEqpto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolTiposEqpto))
			return false;
        VolTiposEqpto castOther = (VolTiposEqpto) other;
		return new EqualsBuilder().append(this.getIdTipoEqpto(),
				castOther.getIdTipoEqpto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoEqpto()).toHashCode();
    }

}
