package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ClassificacaoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idClassificacaoCliente;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.vendas.model.DescClassificacaoCliente descClassificacaoCliente;

    public Long getIdClassificacaoCliente() {
        return this.idClassificacaoCliente;
    }

    public void setIdClassificacaoCliente(Long idClassificacaoCliente) {
        this.idClassificacaoCliente = idClassificacaoCliente;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.vendas.model.DescClassificacaoCliente getDescClassificacaoCliente() {
        return this.descClassificacaoCliente;
    }

	public void setDescClassificacaoCliente(
			com.mercurio.lms.vendas.model.DescClassificacaoCliente descClassificacaoCliente) {
        this.descClassificacaoCliente = descClassificacaoCliente;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idClassificacaoCliente",
				getIdClassificacaoCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ClassificacaoCliente))
			return false;
        ClassificacaoCliente castOther = (ClassificacaoCliente) other;
		return new EqualsBuilder().append(this.getIdClassificacaoCliente(),
				castOther.getIdClassificacaoCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdClassificacaoCliente())
            .toHashCode();
    }

}
