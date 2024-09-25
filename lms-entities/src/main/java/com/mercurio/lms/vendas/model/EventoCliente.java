package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoCliente;

    /** persistent field */
    private VarcharI18n dsEventoCliente;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.sim.model.Evento evento;
   
    public Long getIdEventoCliente() {
        return this.idEventoCliente;
    }

    public void setIdEventoCliente(Long idEventoCliente) {
        this.idEventoCliente = idEventoCliente;
    }

    public VarcharI18n getDsEventoCliente() {
		return dsEventoCliente;
    }

	public void setDsEventoCliente(VarcharI18n dsEventoCliente) {
        this.dsEventoCliente = dsEventoCliente;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.sim.model.Evento getEvento() {
        return this.evento;
    }

    public void setEvento(com.mercurio.lms.sim.model.Evento evento) {
        this.evento = evento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEventoCliente",
				getIdEventoCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoCliente))
			return false;
        EventoCliente castOther = (EventoCliente) other;
		return new EqualsBuilder().append(this.getIdEventoCliente(),
				castOther.getIdEventoCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoCliente()).toHashCode();
    }

	public String getDsEvento() {
		StringBuilder dsEvento = new StringBuilder();
		if (getEvento() != null) {
		   dsEvento.append(getEvento().getCdEvento());
		   if (getEvento().getDescricaoEvento() != null) {
				dsEvento.append(" - ")
						.append(getEvento().getDescricaoEvento()
								.getDsDescricaoEvento());
		   }
		   if (getEvento().getLocalizacaoMercadoria() != null) {
				dsEvento.append(" - ").append(
						getEvento().getLocalizacaoMercadoria()
								.getDsLocalizacaoMercadoria());
		   }
		}
		return dsEvento.toString();
	}
}