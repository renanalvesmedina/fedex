package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolEmailsRecusa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEmailRecusa;

    /** persistent field */
    private VolRecusas volRecusa;

    /** persistent field */
    private VolContatos volContatos;

    /** persistent field */
    private List volRecusas;

    public VolContatos getVolContatos() {
		return volContatos;
	}

	public void setVolContatos(VolContatos volContatos) {
		this.volContatos = volContatos;
	}

	public Long getIdEmailRecusa() {
        return this.idEmailRecusa;
    }

    public void setIdEmailRecusa(Long idEmailRecusa) {
        this.idEmailRecusa = idEmailRecusa;
    }

    public VolRecusas getVolRecusa() {
        return this.volRecusa;
    }

    public void setVolRecusa(VolRecusas volRecusa) {
        this.volRecusa = volRecusa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolRecusas.class)     
    public List getVolRecusas() {
        return this.volRecusas;
    }

    public void setVolRecusas(List volRecusas) {
        this.volRecusas = volRecusas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEmailRecusa",
				getIdEmailRecusa()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolEmailsRecusa))
			return false;
        VolEmailsRecusa castOther = (VolEmailsRecusa) other;
		return new EqualsBuilder().append(this.getIdEmailRecusa(),
				castOther.getIdEmailRecusa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEmailRecusa()).toHashCode();
    }

}
