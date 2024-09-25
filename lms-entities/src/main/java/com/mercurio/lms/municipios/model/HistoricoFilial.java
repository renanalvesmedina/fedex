package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistoricoFilial implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHistoricoFilial;

    /** persistent field */
    private DomainValue tpFilial;

    /** persistent field */
    private YearMonthDay dtPrevisaoOperacaoInicial;

    /** nullable persistent field */
    private YearMonthDay dtPrevisaoOperacaoFinal;

    /** nullable persistent field */
    private YearMonthDay dtRealOperacaoInicial; 

    /** nullable persistent field */
    private YearMonthDay dtRealOperacaoFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdHistoricoFilial() {
        return this.idHistoricoFilial;
    }

    public void setIdHistoricoFilial(Long idHistoricoFilial) {
        this.idHistoricoFilial = idHistoricoFilial;
    }

    public DomainValue getTpFilial() {
        return this.tpFilial;
    }

    public void setTpFilial(DomainValue tpFilial) {
        this.tpFilial = tpFilial;
    }

    public YearMonthDay getDtPrevisaoOperacaoInicial() {
        return this.dtPrevisaoOperacaoInicial;
    }

	public void setDtPrevisaoOperacaoInicial(
			YearMonthDay dtPrevisaoOperacaoInicial) {
        this.dtPrevisaoOperacaoInicial = dtPrevisaoOperacaoInicial;
    }

    public YearMonthDay getDtPrevisaoOperacaoFinal() {
        return this.dtPrevisaoOperacaoFinal;
    }

    public void setDtPrevisaoOperacaoFinal(YearMonthDay dtPrevisaoOperacaoFinal) {
        this.dtPrevisaoOperacaoFinal = dtPrevisaoOperacaoFinal;
    }

    public YearMonthDay getDtRealOperacaoInicial() {
        return this.dtRealOperacaoInicial;
    }

    public void setDtRealOperacaoInicial(YearMonthDay dtRealOperacaoInicial) {
        this.dtRealOperacaoInicial = dtRealOperacaoInicial;
    }

    public YearMonthDay getDtRealOperacaoFinal() {
        return this.dtRealOperacaoFinal;
    }

    public void setDtRealOperacaoFinal(YearMonthDay dtRealOperacaoFinal) {
        this.dtRealOperacaoFinal = dtRealOperacaoFinal;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHistoricoFilial",
				getIdHistoricoFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HistoricoFilial))
			return false;
        HistoricoFilial castOther = (HistoricoFilial) other;
		return new EqualsBuilder().append(this.getIdHistoricoFilial(),
				castOther.getIdHistoricoFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHistoricoFilial())
            .toHashCode();
    }

}
