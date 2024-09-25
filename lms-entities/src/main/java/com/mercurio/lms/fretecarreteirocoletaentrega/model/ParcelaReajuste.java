package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParcelaReajuste implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParcelaReajuste;

    /** persistent field */
    private DomainValue tpParcela;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private BigDecimal vlBruto;

    /** nullable persistent field */
    private BigDecimal vlReajustado;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe simulacaoReajusteFreteCe;

    public Long getIdParcelaReajuste() {
        return this.idParcelaReajuste;
    }

    public void setIdParcelaReajuste(Long idParcelaReajuste) {
        this.idParcelaReajuste = idParcelaReajuste;
    }

    public DomainValue getTpParcela() {
        return this.tpParcela;
    }

    public void setTpParcela(DomainValue tpParcela) {
        this.tpParcela = tpParcela;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public BigDecimal getVlBruto() {
        return this.vlBruto;
    }

    public void setVlBruto(BigDecimal vlBruto) {
        this.vlBruto = vlBruto;
    }

    public BigDecimal getVlReajustado() {
        return this.vlReajustado;
    }

    public void setVlReajustado(BigDecimal pcPercentual) {
        this.vlReajustado = pcPercentual;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe getSimulacaoReajusteFreteCe() {
        return this.simulacaoReajusteFreteCe;
    }

	public void setSimulacaoReajusteFreteCe(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe simulacaoReajusteFreteCe) {
        this.simulacaoReajusteFreteCe = simulacaoReajusteFreteCe;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idParcelaReajuste",
				getIdParcelaReajuste()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParcelaReajuste))
			return false;
        ParcelaReajuste castOther = (ParcelaReajuste) other;
		return new EqualsBuilder().append(this.getIdParcelaReajuste(),
				castOther.getIdParcelaReajuste()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParcelaReajuste())
            .toHashCode();
    }

}
