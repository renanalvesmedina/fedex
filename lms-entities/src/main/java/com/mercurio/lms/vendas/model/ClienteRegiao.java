package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ClienteRegiao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idClienteRegiao;

    /** nullable persistent field */
    private DomainValue tpModal;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio;

    public Long getIdClienteRegiao() {
        return this.idClienteRegiao;
    }

    public void setIdClienteRegiao(Long idClienteRegiao) {
        this.idClienteRegiao = idClienteRegiao;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idClienteRegiao",
				getIdClienteRegiao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ClienteRegiao))
			return false;
        ClienteRegiao castOther = (ClienteRegiao) other;
		return new EqualsBuilder().append(this.getIdClienteRegiao(),
				castOther.getIdClienteRegiao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdClienteRegiao()).toHashCode();
    }

	public com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipio() {
		return tipoLocalizacaoMunicipio;
	}

	public void setTipoLocalizacaoMunicipio(
			com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio) {
		this.tipoLocalizacaoMunicipio = tipoLocalizacaoMunicipio;
	}

}
