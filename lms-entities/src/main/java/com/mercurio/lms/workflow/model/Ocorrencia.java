package com.mercurio.lms.workflow.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class Ocorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrencia;

    /** persistent field */
    private DomainValue tpSituacaoOcorrencia;

    /** persistent field */
    private DateTime dhInclusao;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;    

    /** persistent field */
    private com.mercurio.lms.workflow.model.EventoWorkflow eventoWorkflow;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private List pendencias;

    public Long getIdOcorrencia() {
        return this.idOcorrencia;
    }

    public void setIdOcorrencia(Long idOcorrencia) {
        this.idOcorrencia = idOcorrencia;
    }

    public DomainValue getTpSituacaoOcorrencia() {
        return this.tpSituacaoOcorrencia;
    }

    public void setTpSituacaoOcorrencia(DomainValue tpSituacaoOcorrencia) {
        this.tpSituacaoOcorrencia = tpSituacaoOcorrencia;
    }

    public DateTime getDhInclusao() {
        return this.dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public com.mercurio.lms.workflow.model.EventoWorkflow getEventoWorkflow() {
        return this.eventoWorkflow;
    }

	public void setEventoWorkflow(
			com.mercurio.lms.workflow.model.EventoWorkflow eventoWorkflow) {
        this.eventoWorkflow = eventoWorkflow;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.workflow.model.Pendencia.class)     
    public List getPendencias() {
        return this.pendencias;
    }

    public void setPendencias(List pendencias) {
        this.pendencias = pendencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrencia",
				getIdOcorrencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Ocorrencia))
			return false;
        Ocorrencia castOther = (Ocorrencia) other;
		return new EqualsBuilder().append(this.getIdOcorrencia(),
				castOther.getIdOcorrencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrencia()).toHashCode();
    }

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

}
