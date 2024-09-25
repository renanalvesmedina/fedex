package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class SaldoCheque implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSaldoCheque;

    /** persistent field */
    private BigDecimal vlSaldo;

    /** persistent field */
    private YearMonthDay dtSaldo;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.MoedaPais moedaPais;

    public Long getIdSaldoCheque() {
        return this.idSaldoCheque;
    }

    public void setIdSaldoCheque(Long idSaldoCheque) {
        this.idSaldoCheque = idSaldoCheque;
    }

    public BigDecimal getVlSaldo() {
        return this.vlSaldo;
    }

    public void setVlSaldo(BigDecimal vlSaldo) {
        this.vlSaldo = vlSaldo;
    }

    public YearMonthDay getDtSaldo() {
        return this.dtSaldo;
    }

    public void setDtSaldo(YearMonthDay dtSaldo) {
        this.dtSaldo = dtSaldo;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }
    
    public com.mercurio.lms.configuracoes.model.MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(
			com.mercurio.lms.configuracoes.model.MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idSaldoCheque",
				getIdSaldoCheque()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SaldoCheque))
			return false;
        SaldoCheque castOther = (SaldoCheque) other;
		return new EqualsBuilder().append(this.getIdSaldoCheque(),
				castOther.getIdSaldoCheque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSaldoCheque()).toHashCode();
    }

}
