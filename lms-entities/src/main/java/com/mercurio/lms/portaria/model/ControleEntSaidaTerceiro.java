package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class ControleEntSaidaTerceiro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idControleEntSaidaTerceiro;

    /** persistent field */
    private DateTime dhEntrada;

    /** nullable persistent field */
    private DateTime dhSaida;

    private String nrIdentificacaoSemiReboque;
    
    /** persistent field */
    private com.mercurio.lms.portaria.model.Portaria portaria;

    /** persistent field */
    private com.mercurio.lms.portaria.model.MeioTransporteTerceiro meioTransporteTerceiro;
        
    /** persistent field */
    private com.mercurio.lms.portaria.model.MotoristaTerceiro motoristaTerceiro;

    public Long getIdControleEntSaidaTerceiro() {
        return this.idControleEntSaidaTerceiro;
    }

    public void setIdControleEntSaidaTerceiro(Long idControleEntSaidaTerceiro) {
        this.idControleEntSaidaTerceiro = idControleEntSaidaTerceiro;
    }

    public DateTime getDhEntrada() {
        return this.dhEntrada;
    }

    public void setDhEntrada(DateTime dhEntrada) {
        this.dhEntrada = dhEntrada;
    }

    public DateTime getDhSaida() {
        return this.dhSaida;
    }

    public void setDhSaida(DateTime dhSaida) {
        this.dhSaida = dhSaida;
    }

    public com.mercurio.lms.portaria.model.Portaria getPortaria() {
        return this.portaria;
    }

    public void setPortaria(com.mercurio.lms.portaria.model.Portaria portaria) {
        this.portaria = portaria;
    }

    public com.mercurio.lms.portaria.model.MeioTransporteTerceiro getMeioTransporteTerceiro() {
        return this.meioTransporteTerceiro;
    }

	public void setMeioTransporteTerceiro(
			com.mercurio.lms.portaria.model.MeioTransporteTerceiro meioTransporteTerceiro) {
        this.meioTransporteTerceiro = meioTransporteTerceiro;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idControleEntSaidaTerceiro",
				getIdControleEntSaidaTerceiro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ControleEntSaidaTerceiro))
			return false;
        ControleEntSaidaTerceiro castOther = (ControleEntSaidaTerceiro) other;
		return new EqualsBuilder().append(this.getIdControleEntSaidaTerceiro(),
				castOther.getIdControleEntSaidaTerceiro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdControleEntSaidaTerceiro())
            .toHashCode();
    }

	/**
	 * @return Returns the motoristaTerceiro.
	 */
	public com.mercurio.lms.portaria.model.MotoristaTerceiro getMotoristaTerceiro() {
		return motoristaTerceiro;
	}

	/**
	 * @param motoristaTerceiro
	 *            The motoristaTerceiro to set.
	 */
	public void setMotoristaTerceiro(
			com.mercurio.lms.portaria.model.MotoristaTerceiro motoristaTerceiro) {
		this.motoristaTerceiro = motoristaTerceiro;
	}

	/**
	 * @return Returns the nrIdentificacaoSemiReboque.
	 */
	public String getNrIdentificacaoSemiReboque() {
		return nrIdentificacaoSemiReboque;
	}

	/**
	 * @param nrIdentificacaoSemiReboque
	 *            The nrIdentificacaoSemiReboque to set.
	 */
	public void setNrIdentificacaoSemiReboque(String nrIdentificacaoSemiReboque) {
		this.nrIdentificacaoSemiReboque = nrIdentificacaoSemiReboque;
	}

}
