package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoManifesto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoManifesto;

    /** persistent field */
    private DateTime dhEvento;

    /** persistent field */
    private DomainValue tpEventoManifesto;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.Manifesto manifesto;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdEventoManifesto() {
        return this.idEventoManifesto;
    }

    public void setIdEventoManifesto(Long idEventoManifesto) {
        this.idEventoManifesto = idEventoManifesto;
    }

    public DateTime getDhEvento() {
        return this.dhEvento;
    }

    public void setDhEvento(DateTime dhEvento) {
        this.dhEvento = dhEvento;
    }

    public DomainValue getTpEventoManifesto() {
        return this.tpEventoManifesto;
    }

    public void setTpEventoManifesto(DomainValue tpEventoManifesto) {
        this.tpEventoManifesto = tpEventoManifesto;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.carregamento.model.Manifesto getManifesto() {
        return this.manifesto;
    }

	public void setManifesto(
			com.mercurio.lms.carregamento.model.Manifesto manifesto) {
        this.manifesto = manifesto;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEventoManifesto",
				getIdEventoManifesto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoManifesto))
			return false;
        EventoManifesto castOther = (EventoManifesto) other;
		return new EqualsBuilder().append(this.getIdEventoManifesto(),
				castOther.getIdEventoManifesto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoManifesto())
            .toHashCode();
    }

}
