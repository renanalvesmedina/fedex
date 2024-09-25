package com.mercurio.lms.entrega.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class AgendamentoDoctoServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAgendamentoDoctoServico;
     
	/** persistent field */
	private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.entrega.model.AgendamentoEntrega agendamentoEntrega;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    public Long getIdAgendamentoDoctoServico() {
        return this.idAgendamentoDoctoServico;
    }

    public void setIdAgendamentoDoctoServico(Long idAgendamentoDoctoServico) {
        this.idAgendamentoDoctoServico = idAgendamentoDoctoServico;
    }
        
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public com.mercurio.lms.entrega.model.AgendamentoEntrega getAgendamentoEntrega() {
        return this.agendamentoEntrega;
    }

	public void setAgendamentoEntrega(
			com.mercurio.lms.entrega.model.AgendamentoEntrega agendamentoEntrega) {
        this.agendamentoEntrega = agendamentoEntrega;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAgendamentoDoctoServico",
				getIdAgendamentoDoctoServico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AgendamentoDoctoServico))
			return false;
        AgendamentoDoctoServico castOther = (AgendamentoDoctoServico) other;
		return new EqualsBuilder().append(this.getIdAgendamentoDoctoServico(),
				castOther.getIdAgendamentoDoctoServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAgendamentoDoctoServico())
            .toHashCode();
    }

}
