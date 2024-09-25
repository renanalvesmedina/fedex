package com.mercurio.lms.workflow.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoWorkflow implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoWorkflow;

    /** persistent field */
    private TimeOfDay hrAcaoAutomatica;

    /** persistent field */
    private DomainValue tpAcaoAutomatica;

    /** persistent field */
    private DomainValue tpAlerta;

    /** persistent field */
    private Boolean blRequerAprovacao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String nmClasseVisualizacao;

    /** nullable persistent field */
    private String nmClasseAcao;
    
    /** nullable persistent field */
    private String nmChaveTitulo;    

    /** nullable persistent field */
    private com.mercurio.lms.workflow.model.TipoEvento tipoEvento;

    /** persistent field */
    private com.mercurio.lms.workflow.model.Comite comite;

    /** persistent field */
    private List ocorrencias;

    /** persistent field */
    private List emailEventoUsuario;
    
    public Long getIdEventoWorkflow() {
        return this.idEventoWorkflow;
    }

    public void setIdEventoWorkflow(Long idEventoWorkflow) {
        this.idEventoWorkflow = idEventoWorkflow;
    }

    public TimeOfDay getHrAcaoAutomatica() {
        return this.hrAcaoAutomatica;
    }

    public void setHrAcaoAutomatica(TimeOfDay hrAcaoAutomatica) {
        this.hrAcaoAutomatica = hrAcaoAutomatica;
    }

    public DomainValue getTpAcaoAutomatica() {
        return this.tpAcaoAutomatica;
    }

    public void setTpAcaoAutomatica(DomainValue tpAcaoAutomatica) {
        this.tpAcaoAutomatica = tpAcaoAutomatica;
    }

    public DomainValue getTpAlerta() {
        return this.tpAlerta;
    }

    public void setTpAlerta(DomainValue tpAlerta) {
        this.tpAlerta = tpAlerta;
    }

    public Boolean getBlRequerAprovacao() {
        return this.blRequerAprovacao;
    }

    public void setBlRequerAprovacao(Boolean blRequerAprovacao) {
        this.blRequerAprovacao = blRequerAprovacao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getNmClasseVisualizacao() {
        return this.nmClasseVisualizacao;
    }

    public void setNmClasseVisualizacao(String nmClasseVisualizacao) {
        this.nmClasseVisualizacao = nmClasseVisualizacao;
    }

    public String getNmClasseAcao() {
        return this.nmClasseAcao;
    }

    public void setNmClasseAcao(String nmClasseAcao) {
        this.nmClasseAcao = nmClasseAcao;
    }

    public com.mercurio.lms.workflow.model.TipoEvento getTipoEvento() {
        return this.tipoEvento;
    }

	public void setTipoEvento(
			com.mercurio.lms.workflow.model.TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public com.mercurio.lms.workflow.model.Comite getComite() {
        return this.comite;
    }

    public void setComite(com.mercurio.lms.workflow.model.Comite comite) {
        this.comite = comite;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.workflow.model.Ocorrencia.class)     
    public List getOcorrencias() {
        return this.ocorrencias;
    }

    public void setOcorrencias(List ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEventoWorkflow",
				getIdEventoWorkflow()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoWorkflow))
			return false;
        EventoWorkflow castOther = (EventoWorkflow) other;
		return new EqualsBuilder().append(this.getIdEventoWorkflow(),
				castOther.getIdEventoWorkflow()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoWorkflow()).toHashCode();
    }

	public String getNmChaveTitulo() {
		return nmChaveTitulo;
	}

	public void setNmChaveTitulo(String nmChaveTitulo) {
		this.nmChaveTitulo = nmChaveTitulo;
	}

	public List getEmailEventoUsuario() {
		return emailEventoUsuario;
	}

	public void setEmailEventoUsuario(List emailEventoUsuario) {
		this.emailEventoUsuario = emailEventoUsuario;
	}

}
