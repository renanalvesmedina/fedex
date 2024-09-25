package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoRim implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoRim;

    /** persistent field */
    private DateTime dhEventoRim;

    /** persistent field */
    private DomainValue tpEventoIndenizacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.indenizacoes.model.MotivoCancelamentoRim motivoCancelamentoRim;

    /** persistent field */
    private com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdEventoRim() {
        return this.idEventoRim;
    }

    public void setIdEventoRim(Long idEventoRim) {
        this.idEventoRim = idEventoRim;
    }

    public DateTime getDhEventoRim() {
        return this.dhEventoRim;
    }

    public void setDhEventoRim(DateTime dhEventoRim) {
        this.dhEventoRim = dhEventoRim;
    }

    public DomainValue getTpEventoIndenizacao() {
        return this.tpEventoIndenizacao;
    }

    public void setTpEventoIndenizacao(DomainValue tpEventoIndenizacao) {
        this.tpEventoIndenizacao = tpEventoIndenizacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.indenizacoes.model.MotivoCancelamentoRim getMotivoCancelamentoRim() {
        return this.motivoCancelamentoRim;
    }

	public void setMotivoCancelamentoRim(
			com.mercurio.lms.indenizacoes.model.MotivoCancelamentoRim motivoCancelamentoRim) {
        this.motivoCancelamentoRim = motivoCancelamentoRim;
    }

    public com.mercurio.lms.indenizacoes.model.ReciboIndenizacao getReciboIndenizacao() {
        return this.reciboIndenizacao;
    }

	public void setReciboIndenizacao(
			com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao) {
        this.reciboIndenizacao = reciboIndenizacao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idEventoRim", getIdEventoRim()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoRim))
			return false;
        EventoRim castOther = (EventoRim) other;
		return new EqualsBuilder().append(this.getIdEventoRim(),
				castOther.getIdEventoRim()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoRim()).toHashCode();
    }

}
