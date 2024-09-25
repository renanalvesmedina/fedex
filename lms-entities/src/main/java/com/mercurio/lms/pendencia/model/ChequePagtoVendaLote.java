package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class ChequePagtoVendaLote implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idChequePagtoVendaLote;

    /** persistent field */
    private YearMonthDay dtVencimento;

    /** persistent field */
    private Long nrCheque;

    /** persistent field */
    private BigDecimal vlCheque;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.LotePendencia lotePendencia;

    public Long getIdChequePagtoVendaLote() {
        return this.idChequePagtoVendaLote;
    }

    public void setIdChequePagtoVendaLote(Long idChequePagtoVendaLote) {
        this.idChequePagtoVendaLote = idChequePagtoVendaLote;
    }

    public YearMonthDay getDtVencimento() {
        return this.dtVencimento;
    }

    public void setDtVencimento(YearMonthDay dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Long getNrCheque() {
        return this.nrCheque;
    }

    public void setNrCheque(Long nrCheque) {
        this.nrCheque = nrCheque;
    }

    public BigDecimal getVlCheque() {
        return this.vlCheque;
    }

    public void setVlCheque(BigDecimal vlCheque) {
        this.vlCheque = vlCheque;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.pendencia.model.LotePendencia getLotePendencia() {
        return this.lotePendencia;
    }

	public void setLotePendencia(
			com.mercurio.lms.pendencia.model.LotePendencia lotePendencia) {
        this.lotePendencia = lotePendencia;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idChequePagtoVendaLote",
				getIdChequePagtoVendaLote()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ChequePagtoVendaLote))
			return false;
        ChequePagtoVendaLote castOther = (ChequePagtoVendaLote) other;
		return new EqualsBuilder().append(this.getIdChequePagtoVendaLote(),
				castOther.getIdChequePagtoVendaLote()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdChequePagtoVendaLote())
            .toHashCode();
    }

}
