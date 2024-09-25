package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ItemNotaDebitoNacional implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idItemNotaDebitoNacional;
    
    /** persistent field */
    private BigDecimal vlJuroReceber;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.NotaDebitoNacional notaDebitoNacional;

    public Long getIdItemNotaDebitoNacional() {
        return this.idItemNotaDebitoNacional;
    }

    public void setIdItemNotaDebitoNacional(Long idItemNotaDebitoNacional) {
        this.idItemNotaDebitoNacional = idItemNotaDebitoNacional;
    }

    public BigDecimal getVlJuroReceber() {
        return this.vlJuroReceber;
    }

    public void setVlJuroReceber(BigDecimal vlJuroReceber) {
        this.vlJuroReceber = vlJuroReceber;
    }

    public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
        return this.fatura;
    }

    public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
        this.fatura = fatura;
    }

    public com.mercurio.lms.contasreceber.model.NotaDebitoNacional getNotaDebitoNacional() {
        return this.notaDebitoNacional;
    }

	public void setNotaDebitoNacional(
			com.mercurio.lms.contasreceber.model.NotaDebitoNacional notaDebitoNacional) {
        this.notaDebitoNacional = notaDebitoNacional;
    }

	public String toString() {
		return new ToStringBuilder(this).append("idItemNotaDebitoNacional",
				getIdItemNotaDebitoNacional()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemNotaDebitoNacional))
			return false;
        ItemNotaDebitoNacional castOther = (ItemNotaDebitoNacional) other;
		return new EqualsBuilder().append(this.getIdItemNotaDebitoNacional(),
				castOther.getIdItemNotaDebitoNacional()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemNotaDebitoNacional())
            .toHashCode();
    }

}