package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class AgendaTransferencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAgendaTransferencia;

    /** persistent field */
    private YearMonthDay dtPrevistaTransferencia;

    /** persistent field */
    private DomainValue tpOrigem;

    /** nullable persistent field */
    private String obAgendaTransferencia;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.MotivoTransferencia motivoTransferencia;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente;

    public Long getIdAgendaTransferencia() {
        return this.idAgendaTransferencia;
    }

    public void setIdAgendaTransferencia(Long idAgendaTransferencia) {
        this.idAgendaTransferencia = idAgendaTransferencia;
    }

    public YearMonthDay getDtPrevistaTransferencia() {
        return this.dtPrevistaTransferencia;
    }

    public void setDtPrevistaTransferencia(YearMonthDay dtPrevistaTransferencia) {
        this.dtPrevistaTransferencia = dtPrevistaTransferencia;
    }

    public DomainValue getTpOrigem() {
        return this.tpOrigem;
    }

    public void setTpOrigem(DomainValue tpOrigem) {
        this.tpOrigem = tpOrigem;
    }

    public String getObAgendaTransferencia() {
        return this.obAgendaTransferencia;
    }

    public void setObAgendaTransferencia(String obAgendaTransferencia) {
        this.obAgendaTransferencia = obAgendaTransferencia;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.contasreceber.model.MotivoTransferencia getMotivoTransferencia() {
        return this.motivoTransferencia;
    }

	public void setMotivoTransferencia(
			com.mercurio.lms.contasreceber.model.MotivoTransferencia motivoTransferencia) {
        this.motivoTransferencia = motivoTransferencia;
    }

    public com.mercurio.lms.contasreceber.model.DevedorDocServFat getDevedorDocServFat() {
        return this.devedorDocServFat;
    }

	public void setDevedorDocServFat(
			com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat) {
        this.devedorDocServFat = devedorDocServFat;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
        return this.filialByIdFilialOrigem;
    }

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestino() {
        return this.filialByIdFilialDestino;
    }

	public void setFilialByIdFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino) {
        this.filialByIdFilialDestino = filialByIdFilialDestino;
    }

    public com.mercurio.lms.vendas.model.DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(
			com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idAgendaTransferencia",
				getIdAgendaTransferencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AgendaTransferencia))
			return false;
        AgendaTransferencia castOther = (AgendaTransferencia) other;
		return new EqualsBuilder().append(this.getIdAgendaTransferencia(),
				castOther.getIdAgendaTransferencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAgendaTransferencia())
            .toHashCode();
    }

}
