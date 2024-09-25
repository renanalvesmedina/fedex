package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoAgendamento implements Serializable , Vigencia{

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoAgendamento;

    /** persistent field */
    private VarcharI18n  dsMotivoAgendamento;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List agendamentoEntregasByIdMotivoCancelamento;

    /** persistent field */
    private List agendamentoEntregasByIdMotivoReagendamento;

    public Long getIdMotivoAgendamento() {
        return this.idMotivoAgendamento;
    }

    public void setIdMotivoAgendamento(Long idMotivoAgendamento) {
        this.idMotivoAgendamento = idMotivoAgendamento;
    }

    public VarcharI18n getDsMotivoAgendamento() {
		return dsMotivoAgendamento;
    }

	public void setDsMotivoAgendamento(VarcharI18n dsMotivoAgendamento) {
        this.dsMotivoAgendamento = dsMotivoAgendamento;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.AgendamentoEntrega.class)     
    public List getAgendamentoEntregasByIdMotivoCancelamento() {
        return this.agendamentoEntregasByIdMotivoCancelamento;
    }

	public void setAgendamentoEntregasByIdMotivoCancelamento(
			List agendamentoEntregasByIdMotivoCancelamento) {
        this.agendamentoEntregasByIdMotivoCancelamento = agendamentoEntregasByIdMotivoCancelamento;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.AgendamentoEntrega.class)     
    public List getAgendamentoEntregasByIdMotivoReagendamento() {
        return this.agendamentoEntregasByIdMotivoReagendamento;
    }

	public void setAgendamentoEntregasByIdMotivoReagendamento(
			List agendamentoEntregasByIdMotivoReagendamento) {
        this.agendamentoEntregasByIdMotivoReagendamento = agendamentoEntregasByIdMotivoReagendamento;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoAgendamento",
				getIdMotivoAgendamento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoAgendamento))
			return false;
        MotivoAgendamento castOther = (MotivoAgendamento) other;
		return new EqualsBuilder().append(this.getIdMotivoAgendamento(),
				castOther.getIdMotivoAgendamento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoAgendamento())
            .toHashCode();
    }

	public YearMonthDay getDtVigenciaInicial() {
		return null;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return null;
	}

}
