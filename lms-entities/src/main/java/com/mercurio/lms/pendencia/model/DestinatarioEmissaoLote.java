package com.mercurio.lms.pendencia.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DestinatarioEmissaoLote implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDestinatarioEmissaoLote;

    /** persistent field */
    private String dsSetor;

    /** nullable persistent field */
    private String dsEmailDestinario;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.EmissaoLote emissaoLote;

    public Long getIdDestinatarioEmissaoLote() {
        return this.idDestinatarioEmissaoLote;
    }

    public void setIdDestinatarioEmissaoLote(Long idDestinatarioEmissaoLote) {
        this.idDestinatarioEmissaoLote = idDestinatarioEmissaoLote;
    }

    public String getDsSetor() {
        return this.dsSetor;
    }

    public void setDsSetor(String dsSetor) {
        this.dsSetor = dsSetor;
    }

    public String getDsEmailDestinario() {
        return this.dsEmailDestinario;
    }

    public void setDsEmailDestinario(String dsEmailDestinario) {
        this.dsEmailDestinario = dsEmailDestinario;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.pendencia.model.EmissaoLote getEmissaoLote() {
        return this.emissaoLote;
    }

	public void setEmissaoLote(
			com.mercurio.lms.pendencia.model.EmissaoLote emissaoLote) {
        this.emissaoLote = emissaoLote;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDestinatarioEmissaoLote",
				getIdDestinatarioEmissaoLote()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DestinatarioEmissaoLote))
			return false;
        DestinatarioEmissaoLote castOther = (DestinatarioEmissaoLote) other;
		return new EqualsBuilder().append(this.getIdDestinatarioEmissaoLote(),
				castOther.getIdDestinatarioEmissaoLote()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDestinatarioEmissaoLote())
            .toHashCode();
    }

}
