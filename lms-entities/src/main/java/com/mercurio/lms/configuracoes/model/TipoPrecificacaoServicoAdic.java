package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoPrecificacaoServicoAdic implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoPrecificacaoServicoA;

    /** persistent field */
    private DomainValue tpTipoPrecificacaoServicoA;

    /** persistent field */
    private YearMonthDay dtVigencia;

    private BigDecimal vlCustoPadrao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional;

    public Long getIdTipoPrecificacaoServicoA() {
        return this.idTipoPrecificacaoServicoA;
    }

    public void setIdTipoPrecificacaoServicoA(Long idTipoPrecificacaoServicoA) {
        this.idTipoPrecificacaoServicoA = idTipoPrecificacaoServicoA;
    }

    public DomainValue getTpTipoPrecificacaoServicoA() {
        return this.tpTipoPrecificacaoServicoA;
    }

	public void setTpTipoPrecificacaoServicoA(
			DomainValue tpTipoPrecificacaoServicoA) {
        this.tpTipoPrecificacaoServicoA = tpTipoPrecificacaoServicoA;
    }

    public YearMonthDay getDtVigencia() {
        return this.dtVigencia;
    }

    public void setDtVigencia(YearMonthDay dtVigencia) {
        this.dtVigencia = dtVigencia;
    }

    public com.mercurio.lms.configuracoes.model.ServicoAdicional getServicoAdicional() {
        return this.servicoAdicional;
    }

	public void setServicoAdicional(
			com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional) {
        this.servicoAdicional = servicoAdicional;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoPrecificacaoServicoA",
				getIdTipoPrecificacaoServicoA()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoPrecificacaoServicoAdic))
			return false;
        TipoPrecificacaoServicoAdic castOther = (TipoPrecificacaoServicoAdic) other;
		return new EqualsBuilder().append(this.getIdTipoPrecificacaoServicoA(),
				castOther.getIdTipoPrecificacaoServicoA()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoPrecificacaoServicoA())
            .toHashCode();
    }

	public BigDecimal getVlCustoPadrao() {
		return vlCustoPadrao;
}

	public void setVlCustoPadrao(BigDecimal vlCustoPadrao) {
		this.vlCustoPadrao = vlCustoPadrao;
	}
}
