package com.mercurio.lms.prestcontasciaaerea.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class AwbCancelado implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAwbCancelado;

    /** persistent field */
    private Long nrAwbCancelado;

    /** persistent field */
    private Long dvAwbCancelado;

    /** persistent field */
    private com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta;

    public Long getIdAwbCancelado() {
        return this.idAwbCancelado;
    }

    public void setIdAwbCancelado(Long idAwbCancelado) {
        this.idAwbCancelado = idAwbCancelado;
    }

    public Long getNrAwbCancelado() {
        return this.nrAwbCancelado;
    }

    public void setNrAwbCancelado(Long nrAwbCancelado) {
        this.nrAwbCancelado = nrAwbCancelado;
    }

    public Long getDvAwbCancelado() {
        return this.dvAwbCancelado;
    }

    public void setDvAwbCancelado(Long dvAwbCancelado) {
        this.dvAwbCancelado = dvAwbCancelado;
    }

    public com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta getPrestacaoConta() {
        return this.prestacaoConta;
    }

	public void setPrestacaoConta(
			com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta prestacaoConta) {
        this.prestacaoConta = prestacaoConta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAwbCancelado",
				getIdAwbCancelado()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AwbCancelado))
			return false;
        AwbCancelado castOther = (AwbCancelado) other;
		return new EqualsBuilder().append(this.getIdAwbCancelado(),
				castOther.getIdAwbCancelado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAwbCancelado()).toHashCode();
    }

}
