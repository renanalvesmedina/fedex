package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReciboDesconto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idReciboDesconto;

    /** persistent field */
    private Long nrReciboDesconto;

    /** persistent field */
    private Short dvReciboDesconto;

    /** persistent field */
    private BigDecimal vlReciboDesconto;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private String obReciboDesconto;

    /** persistent field */
    private DomainValue tpSituacaoReciboDesconto;

    /** persistent field */
    private DomainValue tpSituacaoAprovacao;

    /** nullable persistent field */
    private DateTime dhTransmissao;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Redeco redeco;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private Pendencia pendencia;
    
    public Long getIdReciboDesconto() {
        return this.idReciboDesconto;
    }

    public void setIdReciboDesconto(Long idReciboDesconto) {
        this.idReciboDesconto = idReciboDesconto;
    }

    public Long getNrReciboDesconto() {
        return this.nrReciboDesconto;
    }

    public void setNrReciboDesconto(Long nrReciboDesconto) {
        this.nrReciboDesconto = nrReciboDesconto;
    }

    public Short getDvReciboDesconto() {
        return this.dvReciboDesconto;
    }

    public void setDvReciboDesconto(Short dvReciboDesconto) {
        this.dvReciboDesconto = dvReciboDesconto;
    }

    public BigDecimal getVlReciboDesconto() {
        return this.vlReciboDesconto;
    }

    public void setVlReciboDesconto(BigDecimal vlReciboDesconto) {
        this.vlReciboDesconto = vlReciboDesconto;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getObReciboDesconto() {
        return this.obReciboDesconto;
    }

    public void setObReciboDesconto(String obReciboDesconto) {
        this.obReciboDesconto = obReciboDesconto;
    }

    public DomainValue getTpSituacaoReciboDesconto() {
        return this.tpSituacaoReciboDesconto;
    }

    public void setTpSituacaoReciboDesconto(DomainValue tpSituacaoReciboDesconto) {
        this.tpSituacaoReciboDesconto = tpSituacaoReciboDesconto;
    }

    public DomainValue getTpSituacaoAprovacao() {
        return this.tpSituacaoAprovacao;
    }

    public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
        this.tpSituacaoAprovacao = tpSituacaoAprovacao;
    }

    public DateTime getDhTransmissao() {
        return this.dhTransmissao;
    }

    public void setDhTransmissao(DateTime dhTransmissao) {
        this.dhTransmissao = dhTransmissao;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.contasreceber.model.Redeco getRedeco() {
        return this.redeco;
    }

    public void setRedeco(com.mercurio.lms.contasreceber.model.Redeco redeco) {
        this.redeco = redeco;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idReciboDesconto",
				getIdReciboDesconto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReciboDesconto))
			return false;
        ReciboDesconto castOther = (ReciboDesconto) other;
		return new EqualsBuilder().append(this.getIdReciboDesconto(),
				castOther.getIdReciboDesconto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReciboDesconto()).toHashCode();
    }

    public Pendencia getPendencia() {
        return pendencia;
    }

    public void setPendencia(Pendencia pendencia) {
        this.pendencia = pendencia;
    }

}
