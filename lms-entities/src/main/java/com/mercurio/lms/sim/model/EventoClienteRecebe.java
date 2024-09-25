package com.mercurio.lms.sim.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoClienteRecebe implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoClienteRecebe;

    /** persistent field */
    private com.mercurio.lms.sim.model.ConfiguracaoComunicacao configuracaoComunicacao;

    /** persistent field */
    private com.mercurio.lms.sim.model.Evento evento;

    public Long getIdEventoClienteRecebe() {
        return this.idEventoClienteRecebe;
    }

    public void setIdEventoClienteRecebe(Long idEventoClienteRecebe) {
        this.idEventoClienteRecebe = idEventoClienteRecebe;
    }

    public com.mercurio.lms.sim.model.ConfiguracaoComunicacao getConfiguracaoComunicacao() {
        return this.configuracaoComunicacao;
    }

	public void setConfiguracaoComunicacao(
			com.mercurio.lms.sim.model.ConfiguracaoComunicacao configuracaoComunicacao) {
        this.configuracaoComunicacao = configuracaoComunicacao;
    }

    public com.mercurio.lms.sim.model.Evento getEvento() {
        return this.evento;
    }

    public void setEvento(com.mercurio.lms.sim.model.Evento evento) {
        this.evento = evento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEventoClienteRecebe",
				getIdEventoClienteRecebe()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoClienteRecebe))
			return false;
        EventoClienteRecebe castOther = (EventoClienteRecebe) other;
		return new EqualsBuilder().append(this.getIdEventoClienteRecebe(),
				castOther.getIdEventoClienteRecebe()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoClienteRecebe())
            .toHashCode();
    }

}
