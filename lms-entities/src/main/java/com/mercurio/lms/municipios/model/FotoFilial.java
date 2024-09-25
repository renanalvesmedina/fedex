package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FotoFilial implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFotoFilial;

    /** persistent field */
    private byte[] imFilial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdFotoFilial() {
        return this.idFotoFilial;
    }

    public void setIdFotoFilial(Long idFotoFilial) {
        this.idFotoFilial = idFotoFilial;
    }

    public byte[] getImFilial() {
        return this.imFilial;
    }

    public void setImFilial(byte[] imFilial) {
        this.imFilial = imFilial;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFotoFilial",
				getIdFotoFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FotoFilial))
			return false;
        FotoFilial castOther = (FotoFilial) other;
		return new EqualsBuilder().append(this.getIdFotoFilial(),
				castOther.getIdFotoFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFotoFilial()).toHashCode();
    }

}
