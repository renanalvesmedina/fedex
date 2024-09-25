package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoTransferencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoTransferencia;

    /** persistent field */
    private VarcharI18n dsMotivoTransferencia;

    /** persistent field */
    private DomainValue tpSituacao;    
    
    /** persistent field */
    private List itemTransferencias;

    /** persistent field */
    private List agendaTransferencias;

    public Long getIdMotivoTransferencia() {
        return this.idMotivoTransferencia;
    }

    public void setIdMotivoTransferencia(Long idMotivoTransferencia) {
        this.idMotivoTransferencia = idMotivoTransferencia;
    }

    public VarcharI18n getDsMotivoTransferencia() {
		return dsMotivoTransferencia;
    }

	public void setDsMotivoTransferencia(VarcharI18n dsMotivoTransferencia) {
        this.dsMotivoTransferencia = dsMotivoTransferencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemTransferencia.class)     
    public List getItemTransferencias() {
        return this.itemTransferencias;
    }

    public void setItemTransferencias(List itemTransferencias) {
        this.itemTransferencias = itemTransferencias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.AgendaTransferencia.class)     
    public List getAgendaTransferencias() {
        return this.agendaTransferencias;
    }

    public void setAgendaTransferencias(List agendaTransferencias) {
        this.agendaTransferencias = agendaTransferencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoTransferencia",
				getIdMotivoTransferencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoTransferencia))
			return false;
        MotivoTransferencia castOther = (MotivoTransferencia) other;
		return new EqualsBuilder().append(this.getIdMotivoTransferencia(),
				castOther.getIdMotivoTransferencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoTransferencia())
            .toHashCode();
    }

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

}
