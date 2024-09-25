package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DespachanteCtoInt implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDespachanteCtoInt;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Despachante despachante;

    public Long getIdDespachanteCtoInt() {
        return this.idDespachanteCtoInt;
    }

    public void setIdDespachanteCtoInt(Long idDespachanteCtoInt) {
        this.idDespachanteCtoInt = idDespachanteCtoInt;
    }

    public com.mercurio.lms.expedicao.model.CtoInternacional getCtoInternacional() {
        return this.ctoInternacional;
    }

	public void setCtoInternacional(
			com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional) {
        this.ctoInternacional = ctoInternacional;
    }

    public com.mercurio.lms.vendas.model.Despachante getDespachante() {
        return this.despachante;
    }

	public void setDespachante(
			com.mercurio.lms.vendas.model.Despachante despachante) {
        this.despachante = despachante;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDespachanteCtoInt",
				getIdDespachanteCtoInt()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DespachanteCtoInt))
			return false;
        DespachanteCtoInt castOther = (DespachanteCtoInt) other;
		return new EqualsBuilder().append(this.getIdDespachanteCtoInt(),
				castOther.getIdDespachanteCtoInt()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDespachanteCtoInt())
            .toHashCode();
    }

}
