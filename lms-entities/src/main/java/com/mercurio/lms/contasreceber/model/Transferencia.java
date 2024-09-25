package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Transferencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTransferencia;

    /** persistent field */
    private Long nrTransferencia;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private DomainValue tpSituacaoTransferencia;

    /** persistent field */
    private DomainValue tpOrigem;

    /** nullable persistent field */
    private YearMonthDay dtRecebimento;

    /** nullable persistent field */
    private DateTime dhTransmissao;
    
    /** nullable persistent field */
    private Boolean blIndicadorImpressao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

    /** persistent field */
    private List itemTransferencias;

    public Long getIdTransferencia() {
        return this.idTransferencia;
    }

    public void setIdTransferencia(Long idTransferencia) {
        this.idTransferencia = idTransferencia;
    }

    public Long getNrTransferencia() {
        return this.nrTransferencia;
    }

    public void setNrTransferencia(Long nrTransferencia) {
        this.nrTransferencia = nrTransferencia;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public DomainValue getTpSituacaoTransferencia() {
        return this.tpSituacaoTransferencia;
    }

    public void setTpSituacaoTransferencia(DomainValue tpSituacaoTransferencia) {
        this.tpSituacaoTransferencia = tpSituacaoTransferencia;
    }

    public DomainValue getTpOrigem() {
        return this.tpOrigem;
    }

    public void setTpOrigem(DomainValue tpOrigem) {
        this.tpOrigem = tpOrigem;
    }

    public YearMonthDay getDtRecebimento() {
        return this.dtRecebimento;
    }

    public void setDtRecebimento(YearMonthDay dtRecebimento) {
        this.dtRecebimento = dtRecebimento;
    }

    public DateTime getDhTransmissao() {
        return this.dhTransmissao;
    }

    public void setDhTransmissao(DateTime dhTransmissao) {
        this.dhTransmissao = dhTransmissao;
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

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemTransferencia.class)     
    public List getItemTransferencias() {
        return this.itemTransferencias;
    }

    public void setItemTransferencias(List itemTransferencias) {
        this.itemTransferencias = itemTransferencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTransferencia",
				getIdTransferencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Transferencia))
			return false;
        Transferencia castOther = (Transferencia) other;
		return new EqualsBuilder().append(this.getIdTransferencia(),
				castOther.getIdTransferencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTransferencia()).toHashCode();
    }

    public Boolean getBlIndicadorImpressao() {
        return blIndicadorImpressao;
    }

    public void setBlIndicadorImpressao(Boolean blIndicadorImpressao) {
        this.blIndicadorImpressao = blIndicadorImpressao;
    }

}