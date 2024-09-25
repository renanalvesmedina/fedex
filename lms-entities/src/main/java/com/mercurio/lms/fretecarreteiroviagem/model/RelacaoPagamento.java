package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class RelacaoPagamento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRelacaoPagamento;

    /** persistent field */
    private Long nrRelacaoPagamento;

    /** persistent field */
    private YearMonthDay dtGeracao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private YearMonthDay dtPrevistaPagto;

    /** nullable persistent field */
    private YearMonthDay dtRealPagto;

    /** persistent field */
    private List reciboFreteCarreteiros;

    public Long getIdRelacaoPagamento() {
        return this.idRelacaoPagamento;
    }

    public void setIdRelacaoPagamento(Long idRelacaoPagamento) {
        this.idRelacaoPagamento = idRelacaoPagamento;
    }

    public Long getNrRelacaoPagamento() {
        return this.nrRelacaoPagamento;
    }

    public void setNrRelacaoPagamento(Long nrRelacaoPagamento) {
        this.nrRelacaoPagamento = nrRelacaoPagamento;
    }

    public YearMonthDay getDtGeracao() {
        return this.dtGeracao;
    }

    public void setDtGeracao(YearMonthDay dtGeracao) {
        this.dtGeracao = dtGeracao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public YearMonthDay getdtPrevistaPagto() {
        return this.dtPrevistaPagto;
    }

    public void setdtPrevistaPagto(YearMonthDay dtPrevistaPagto) {
        this.dtPrevistaPagto = dtPrevistaPagto;
    }

    public YearMonthDay getdtRealPagto() {
        return this.dtRealPagto;
    }

    public void setdtRealPagto(YearMonthDay dtRealPagto) {
        this.dtRealPagto = dtRealPagto;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro.class)     
    public List getReciboFreteCarreteiros() {
        return this.reciboFreteCarreteiros;
    }

    public void setReciboFreteCarreteiros(List reciboFreteCarreteiros) {
        this.reciboFreteCarreteiros = reciboFreteCarreteiros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRelacaoPagamento",
				getIdRelacaoPagamento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RelacaoPagamento))
			return false;
        RelacaoPagamento castOther = (RelacaoPagamento) other;
		return new EqualsBuilder().append(this.getIdRelacaoPagamento(),
				castOther.getIdRelacaoPagamento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRelacaoPagamento())
            .toHashCode();
    }

}
