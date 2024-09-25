package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistTrocaFilialCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHistTrocaFilialCliente;

    /** persistent field */
    private Boolean blFilialCobranca;

    /** persistent field */
    private Boolean blFilialResponsavel;
    
    /** persistent field */
    private Boolean blFilialComercial;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.HistoricoTrocaFilial historicoTrocaFilial;

    public Long getIdHistTrocaFilialCliente() {
        return this.idHistTrocaFilialCliente;
    }

    public void setIdHistTrocaFilialCliente(Long idHistTrocaFilialCliente) {
        this.idHistTrocaFilialCliente = idHistTrocaFilialCliente;
    }

    public Boolean getBlFilialCobranca() {
        return this.blFilialCobranca;
    }

    public void setBlFilialCobranca(Boolean blFilialCobranca) {
        this.blFilialCobranca = blFilialCobranca;
    }

    public Boolean getBlFilialResponsavel() {
        return this.blFilialResponsavel;
    }

    public void setBlFilialResponsavel(Boolean blFilialResponsavel) {
        this.blFilialResponsavel = blFilialResponsavel;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.HistoricoTrocaFilial getHistoricoTrocaFilial() {
        return this.historicoTrocaFilial;
    }

	public void setHistoricoTrocaFilial(
			com.mercurio.lms.municipios.model.HistoricoTrocaFilial historicoTrocaFilial) {
        this.historicoTrocaFilial = historicoTrocaFilial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHistTrocaFilialCliente",
				getIdHistTrocaFilialCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HistTrocaFilialCliente))
			return false;
        HistTrocaFilialCliente castOther = (HistTrocaFilialCliente) other;
		return new EqualsBuilder().append(this.getIdHistTrocaFilialCliente(),
				castOther.getIdHistTrocaFilialCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHistTrocaFilialCliente())
            .toHashCode();
    }

	/**
	 * @return Returns the blFilialComercial.
	 */
	public Boolean getBlFilialComercial() {
		return blFilialComercial;
	}

	/**
	 * @param blFilialComercial
	 *            The blFilialComercial to set.
	 */
	public void setBlFilialComercial(Boolean blFilialComercial) {
		this.blFilialComercial = blFilialComercial;
	}

}
