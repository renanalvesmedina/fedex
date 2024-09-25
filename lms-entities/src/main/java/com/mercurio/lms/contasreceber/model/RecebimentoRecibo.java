package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class RecebimentoRecibo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idChequeRecibo;

    /** persistent field */
    private BigDecimal vlRecebimento;

    /** persistent field */
    private DomainValue tpRecebimento;

    /** nullable persistent field */
    private Short nrAgenciaCheque;

    /** nullable persistent field */
    private Long nrCheque;

    /** nullable persistent field */
    private String nrContaCorrenteCheque;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Recibo recibo;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Banco banco;

    public Long getIdChequeRecibo() {
        return this.idChequeRecibo;
    }

    public void setIdChequeRecibo(Long idChequeRecibo) {
        this.idChequeRecibo = idChequeRecibo;
    }

    public BigDecimal getVlRecebimento() {
        return this.vlRecebimento;
    }

    public void setVlRecebimento(BigDecimal vlRecebimento) {
        this.vlRecebimento = vlRecebimento;
    }

    public DomainValue getTpRecebimento() {
        return this.tpRecebimento;
    }

    public void setTpRecebimento(DomainValue tpRecebimento) {
        this.tpRecebimento = tpRecebimento;
    }

    public Short getNrAgenciaCheque() {
        return this.nrAgenciaCheque;
    }

    public void setNrAgenciaCheque(Short nrAgenciaCheque) {
        this.nrAgenciaCheque = nrAgenciaCheque;
    }

    public Long getNrCheque() {
        return this.nrCheque;
    }

    public void setNrCheque(Long nrCheque) {
        this.nrCheque = nrCheque;
    }

    public String getNrContaCorrenteCheque() {
        return this.nrContaCorrenteCheque;
    }

    public void setNrContaCorrenteCheque(String nrContaCorrenteCheque) {
        this.nrContaCorrenteCheque = nrContaCorrenteCheque;
    }

    public com.mercurio.lms.contasreceber.model.Recibo getRecibo() {
        return this.recibo;
    }

    public void setRecibo(com.mercurio.lms.contasreceber.model.Recibo recibo) {
        this.recibo = recibo;
    }

    public com.mercurio.lms.configuracoes.model.Banco getBanco() {
        return this.banco;
    }

    public void setBanco(com.mercurio.lms.configuracoes.model.Banco banco) {
        this.banco = banco;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idChequeRecibo",
				getIdChequeRecibo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RecebimentoRecibo))
			return false;
        RecebimentoRecibo castOther = (RecebimentoRecibo) other;
		return new EqualsBuilder().append(this.getIdChequeRecibo(),
				castOther.getIdChequeRecibo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdChequeRecibo()).toHashCode();
    }

}
