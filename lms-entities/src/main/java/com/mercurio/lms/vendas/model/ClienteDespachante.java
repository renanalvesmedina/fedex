package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ClienteDespachante implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idClienteDespachante;

    /** persistent field */
    private DomainValue tpLocal;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Cliente cliente;

    /** persistent field */
    private Despachante despachante;

    public Long getIdClienteDespachante() {
        return this.idClienteDespachante;
    }

    public void setIdClienteDespachante(Long idClienteDespachante) {
        this.idClienteDespachante = idClienteDespachante;
    }

    public DomainValue getTpLocal() {
        return this.tpLocal;
    }

    public void setTpLocal(DomainValue tpLocal) {
        this.tpLocal = tpLocal;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Despachante getDespachante() {
        return this.despachante;
    }

    public void setDespachante(Despachante despachante) {
        this.despachante = despachante;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idClienteDespachante",
				getIdClienteDespachante()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ClienteDespachante))
			return false;
        ClienteDespachante castOther = (ClienteDespachante) other;
		return new EqualsBuilder().append(this.getIdClienteDespachante(),
				castOther.getIdClienteDespachante()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdClienteDespachante())
            .toHashCode();
    }

}
