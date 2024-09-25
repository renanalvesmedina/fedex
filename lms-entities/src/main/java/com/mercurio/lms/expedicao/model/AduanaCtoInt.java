package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class AduanaCtoInt implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idAduanaCtoInt;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PontoParada pontoParada;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional;

    public Long getIdAduanaCtoInt() {
        return this.idAduanaCtoInt;
    }

    public void setIdAduanaCtoInt(Long idAduanaCtoInt) {
        this.idAduanaCtoInt = idAduanaCtoInt;
    }

    public com.mercurio.lms.municipios.model.PontoParada getPontoParada() {
        return this.pontoParada;
    }

	public void setPontoParada(
			com.mercurio.lms.municipios.model.PontoParada pontoParada) {
        this.pontoParada = pontoParada;
    }

    public com.mercurio.lms.expedicao.model.CtoInternacional getCtoInternacional() {
        return this.ctoInternacional;
    }

	public void setCtoInternacional(
			com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional) {
        this.ctoInternacional = ctoInternacional;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAduanaCtoInt",
				getIdAduanaCtoInt()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AduanaCtoInt))
			return false;
        AduanaCtoInt castOther = (AduanaCtoInt) other;
		return new EqualsBuilder().append(this.getIdAduanaCtoInt(),
				castOther.getIdAduanaCtoInt()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAduanaCtoInt()).toHashCode();
    }

}
