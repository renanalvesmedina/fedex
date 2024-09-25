package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class LocalEvento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLocalEvento;

    /** persistent field */
    private String  dsLocalEvento;

    /** persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private Short cdLocalEvento;

    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private DomainValue tpModal;
    
    /** persistent field */
    private List eventos;

    public Long getIdLocalEvento() {
        return this.idLocalEvento;
    }

    public void setIdLocalEvento(Long idLocalEvento) {
        this.idLocalEvento = idLocalEvento;
    }

    public String  getDsLocalEvento() {
        return this.dsLocalEvento;
    }

    public void setDsLocalEvento(String  dsLocalEvento) {
        this.dsLocalEvento = dsLocalEvento;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public Short getCdLocalEvento() {
        return this.cdLocalEvento;
    }

    public void setCdLocalEvento(Short cdLocalEvento) {
        this.cdLocalEvento = cdLocalEvento;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sim.model.Evento.class)     
    public List getEventos() {
        return this.eventos;
    }

    public void setEventos(List eventos) {
        this.eventos = eventos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLocalEvento",
				getIdLocalEvento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LocalEvento))
			return false;
        LocalEvento castOther = (LocalEvento) other;
		return new EqualsBuilder().append(this.getIdLocalEvento(),
				castOther.getIdLocalEvento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLocalEvento()).toHashCode();
    }

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public String getLocalEventoConcatenado() {
		if (this.cdLocalEvento!= null && dsLocalEvento!=null){
			return this.cdLocalEvento.toString() + " - " + this.dsLocalEvento;
		} else
			return "";
	}
}
