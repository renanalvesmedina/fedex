package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class AwbColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAwbColeta;

    /** persistent field */
    private com.mercurio.lms.coleta.model.DetalheColeta detalheColeta;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Awb awb;

    public Long getIdAwbColeta() {
        return this.idAwbColeta;
    }

    public void setIdAwbColeta(Long idAwbColeta) {
        this.idAwbColeta = idAwbColeta;
    }

    public com.mercurio.lms.coleta.model.DetalheColeta getDetalheColeta() {
        return this.detalheColeta;
    }

	public void setDetalheColeta(
			com.mercurio.lms.coleta.model.DetalheColeta detalheColeta) {
        this.detalheColeta = detalheColeta;
    }

    public com.mercurio.lms.expedicao.model.Awb getAwb() {
        return this.awb;
    }

    public void setAwb(com.mercurio.lms.expedicao.model.Awb awb) {
        this.awb = awb;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idAwbColeta", getIdAwbColeta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AwbColeta))
			return false;
        AwbColeta castOther = (AwbColeta) other;
		return new EqualsBuilder().append(this.getIdAwbColeta(),
				castOther.getIdAwbColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAwbColeta()).toHashCode();
    }

}
