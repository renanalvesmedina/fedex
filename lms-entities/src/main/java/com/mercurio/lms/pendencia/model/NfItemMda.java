package com.mercurio.lms.pendencia.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class NfItemMda implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNfItemMda;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.ItemMda itemMda;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento;

    public Long getIdNfItemMda() {
        return this.idNfItemMda;
    }

    public void setIdNfItemMda(Long idNfItemMda) {
        this.idNfItemMda = idNfItemMda;
    }

    public com.mercurio.lms.pendencia.model.ItemMda getItemMda() {
        return this.itemMda;
    }

    public void setItemMda(com.mercurio.lms.pendencia.model.ItemMda itemMda) {
        this.itemMda = itemMda;
    }

    public com.mercurio.lms.expedicao.model.NotaFiscalConhecimento getNotaFiscalConhecimento() {
        return this.notaFiscalConhecimento;
    }

	public void setNotaFiscalConhecimento(
			com.mercurio.lms.expedicao.model.NotaFiscalConhecimento notaFiscalConhecimento) {
        this.notaFiscalConhecimento = notaFiscalConhecimento;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idNfItemMda", getIdNfItemMda()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NfItemMda))
			return false;
        NfItemMda castOther = (NfItemMda) other;
		return new EqualsBuilder().append(this.getIdNfItemMda(),
				castOther.getIdNfItemMda()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNfItemMda()).toHashCode();
    }

}
