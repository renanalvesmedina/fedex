package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class NcParcelaSimulacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNcParcelaSimulacao;

    /** persistent field */
    private BigDecimal qtNcParcelaSimulacao;

    /** persistent field */
    private BigDecimal vlOriginal;

    /** persistent field */
    private BigDecimal vlReajustado;

    /** persistent field */
    private DomainValue tpParcela;

    /** nullable persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.ParamSimulacaoHistorica paramSimulacaoHistorica;

    public Long getIdNcParcelaSimulacao() {
        return this.idNcParcelaSimulacao;
    }

    public void setIdNcParcelaSimulacao(Long idNcParcelaSimulacao) {
        this.idNcParcelaSimulacao = idNcParcelaSimulacao;
    }

    public BigDecimal getQtNcParcelaSimulacao() {
        return this.qtNcParcelaSimulacao;
    }

    public void setQtNcParcelaSimulacao(BigDecimal qtNcParcelaSimulacao) {
        this.qtNcParcelaSimulacao = qtNcParcelaSimulacao;
    }

    public BigDecimal getVlOriginal() {
        return this.vlOriginal;
    }

    public void setVlOriginal(BigDecimal vlOriginal) {
        this.vlOriginal = vlOriginal;
    }

    public BigDecimal getVlReajustado() {
        return this.vlReajustado;
    }

    public void setVlReajustado(BigDecimal pcReajuste) {
        this.vlReajustado = pcReajuste;
    }

    public DomainValue getTpParcela() {
        return this.tpParcela;
    }

    public void setTpParcela(DomainValue tpParcela) {
        this.tpParcela = tpParcela;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.ParamSimulacaoHistorica getParamSimulacaoHistorica() {
        return this.paramSimulacaoHistorica;
    }

	public void setParamSimulacaoHistorica(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.ParamSimulacaoHistorica paramSimulacaoHistorica) {
        this.paramSimulacaoHistorica = paramSimulacaoHistorica;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNcParcelaSimulacao",
				getIdNcParcelaSimulacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NcParcelaSimulacao))
			return false;
        NcParcelaSimulacao castOther = (NcParcelaSimulacao) other;
		return new EqualsBuilder().append(this.getIdNcParcelaSimulacao(),
				castOther.getIdNcParcelaSimulacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNcParcelaSimulacao())
            .toHashCode();
    }

}
