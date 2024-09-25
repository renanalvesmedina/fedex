package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class CotacaoIndicadorFinanceiro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCotacaoIndFinanceiro;

    /** persistent field */
    private BigDecimal vlCotacaoIndFinanceiro;

    /** persistent field */
    private YearMonthDay dtCotacaoIndFinanceiro;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.IndicadorFinanceiro indicadorFinanceiro;

    public Long getIdCotacaoIndFinanceiro() {
        return this.idCotacaoIndFinanceiro;
    }

    public void setIdCotacaoIndFinanceiro(Long idCotacaoIndFinanceiro) {
        this.idCotacaoIndFinanceiro = idCotacaoIndFinanceiro;
    }

    public BigDecimal getVlCotacaoIndFinanceiro() {
        return this.vlCotacaoIndFinanceiro;
    }

    public void setVlCotacaoIndFinanceiro(BigDecimal vlCotacaoIndFinanceiro) {
        this.vlCotacaoIndFinanceiro = vlCotacaoIndFinanceiro;
    }

    public YearMonthDay getDtCotacaoIndFinanceiro() {
        return this.dtCotacaoIndFinanceiro;
    }

    public void setDtCotacaoIndFinanceiro(YearMonthDay dtCotacaoIndFinanceiro) {
        this.dtCotacaoIndFinanceiro = dtCotacaoIndFinanceiro;
    }

    public com.mercurio.lms.configuracoes.model.IndicadorFinanceiro getIndicadorFinanceiro() {
        return this.indicadorFinanceiro;
    }

	public void setIndicadorFinanceiro(
			com.mercurio.lms.configuracoes.model.IndicadorFinanceiro indicadorFinanceiro) {
        this.indicadorFinanceiro = indicadorFinanceiro;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCotacaoIndFinanceiro",
				getIdCotacaoIndFinanceiro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CotacaoIndicadorFinanceiro))
			return false;
        CotacaoIndicadorFinanceiro castOther = (CotacaoIndicadorFinanceiro) other;
		return new EqualsBuilder().append(this.getIdCotacaoIndFinanceiro(),
				castOther.getIdCotacaoIndFinanceiro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCotacaoIndFinanceiro())
            .toHashCode();
    }

}
