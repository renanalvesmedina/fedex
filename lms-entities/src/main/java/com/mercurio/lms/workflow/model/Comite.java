package com.mercurio.lms.workflow.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class Comite implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idComite;

    /** persistent field */
    private VarcharI18n nmComite;

    /** nullable persistent field */
    private DomainValue tpModal;

    /** nullable persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private List integrantes;

    /** persistent field */
    private List eventoWorkflows;

    public Long getIdComite() {
        return this.idComite;
    }

    public void setIdComite(Long idComite) {
        this.idComite = idComite;
    }

    public VarcharI18n getNmComite() {
		return nmComite;
    }

	public void setNmComite(VarcharI18n nmComite) {
        this.nmComite = nmComite;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.workflow.model.Integrante.class)     
    public List getIntegrantes() {
        return this.integrantes;
    }

    public void setIntegrantes(List integrantes) {
        this.integrantes = integrantes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.workflow.model.EventoWorkflow.class)     
    public List getEventoWorkflows() {
        return this.eventoWorkflows;
    }

    public void setEventoWorkflows(List eventoWorkflows) {
        this.eventoWorkflows = eventoWorkflows;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idComite", getIdComite())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Comite))
			return false;
        Comite castOther = (Comite) other;
		return new EqualsBuilder().append(this.getIdComite(),
				castOther.getIdComite()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdComite()).toHashCode();
    }

}
