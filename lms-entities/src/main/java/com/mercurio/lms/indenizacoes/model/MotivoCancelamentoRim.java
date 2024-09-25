package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoCancelamentoRim implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoCancelamentoRim;

    /** persistent field */
    private VarcharI18n dsMotivoCancelamentoRim;

    /** persistent field */
    private DomainValue tpCancelamento;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List eventoRims;

    public Long getIdMotivoCancelamentoRim() {
        return this.idMotivoCancelamentoRim;
    }

    public void setIdMotivoCancelamentoRim(Long idMotivoCancelamentoRim) {
        this.idMotivoCancelamentoRim = idMotivoCancelamentoRim;
    }

    public VarcharI18n getDsMotivoCancelamentoRim() {
		return dsMotivoCancelamentoRim;
    }

	public void setDsMotivoCancelamentoRim(VarcharI18n dsMotivoCancelamentoRim) {
        this.dsMotivoCancelamentoRim = dsMotivoCancelamentoRim;
    }

    public DomainValue getTpCancelamento() {
        return this.tpCancelamento;
    }

    public void setTpCancelamento(DomainValue tpCancelamento) {
        this.tpCancelamento = tpCancelamento;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.EventoRim.class)     
    public List getEventoRims() {
        return this.eventoRims;
    }

    public void setEventoRims(List eventoRims) {
        this.eventoRims = eventoRims;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoCancelamentoRim",
				getIdMotivoCancelamentoRim()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoCancelamentoRim))
			return false;
        MotivoCancelamentoRim castOther = (MotivoCancelamentoRim) other;
		return new EqualsBuilder().append(this.getIdMotivoCancelamentoRim(),
				castOther.getIdMotivoCancelamentoRim()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoCancelamentoRim())
            .toHashCode();
    }

}
