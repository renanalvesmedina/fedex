package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class TrechoCtoInt implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTrechoCtoInt;

    /** nullable persistent field */
    private BigDecimal vlFreteRemetente;

    /** nullable persistent field */
    private BigDecimal vlFreteDestinatario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.TramoFreteInternacional tramoFreteInternacional;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional;

    public Long getIdTrechoCtoInt() {
        return this.idTrechoCtoInt;
    }

    public void setIdTrechoCtoInt(Long idTrechoCtoInt) {
        this.idTrechoCtoInt = idTrechoCtoInt;
    }

    public BigDecimal getVlFreteRemetente() {
        return this.vlFreteRemetente;
    }

    public void setVlFreteRemetente(BigDecimal vlFreteRemetente) {
        this.vlFreteRemetente = vlFreteRemetente;
    }

    public BigDecimal getVlFreteDestinatario() {
        return this.vlFreteDestinatario;
    }

    public void setVlFreteDestinatario(BigDecimal vlFreteDestinatario) {
        this.vlFreteDestinatario = vlFreteDestinatario;
    }

    public com.mercurio.lms.configuracoes.model.TramoFreteInternacional getTramoFreteInternacional() {
        return this.tramoFreteInternacional;
    }

	public void setTramoFreteInternacional(
			com.mercurio.lms.configuracoes.model.TramoFreteInternacional tramoFreteInternacional) {
        this.tramoFreteInternacional = tramoFreteInternacional;
    }

    public com.mercurio.lms.expedicao.model.CtoInternacional getCtoInternacional() {
        return this.ctoInternacional;
    }

	public void setCtoInternacional(
			com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional) {
        this.ctoInternacional = ctoInternacional;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTrechoCtoInt",
				getIdTrechoCtoInt()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TrechoCtoInt))
			return false;
        TrechoCtoInt castOther = (TrechoCtoInt) other;
		return new EqualsBuilder().append(this.getIdTrechoCtoInt(),
				castOther.getIdTrechoCtoInt()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTrechoCtoInt()).toHashCode();
    }

}
