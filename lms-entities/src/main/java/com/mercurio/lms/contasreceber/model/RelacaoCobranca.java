package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class RelacaoCobranca implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRelacaoCobranca;

    /** persistent field */
    private Long nrRelacaoCobrancaFilial;

    /** persistent field */
    private BigDecimal vlDesconto;

    /** persistent field */
    private BigDecimal vlJuros;

    /** persistent field */
    private BigDecimal vlTarifa;

    /** persistent field */
    private BigDecimal vlCsll;

    /** persistent field */
    private BigDecimal vlPis;

    /** persistent field */
    private BigDecimal vlCofins;

    /** persistent field */
    private BigDecimal vlIr;

    /** persistent field */
    private BigDecimal vlFrete;

    /** persistent field */
    private DomainValue tpSituacaoRelacaoCobranca;

    /** persistent field */
    private String dsOrigem;

    /** persistent field */
    private YearMonthDay dtLiquidacao;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Redeco redeco;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List faturas;

    public Long getIdRelacaoCobranca() {
        return this.idRelacaoCobranca;
    }

    public void setIdRelacaoCobranca(Long idRelacaoCobranca) {
        this.idRelacaoCobranca = idRelacaoCobranca;
    }

    public Long getNrRelacaoCobrancaFilial() {
        return this.nrRelacaoCobrancaFilial;
    }

    public void setNrRelacaoCobrancaFilial(Long nrRelacaoCobrancaFilial) {
        this.nrRelacaoCobrancaFilial = nrRelacaoCobrancaFilial;
    }

    public BigDecimal getVlDesconto() {
        return this.vlDesconto;
    }

    public void setVlDesconto(BigDecimal vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public BigDecimal getVlJuros() {
        return this.vlJuros;
    }

    public void setVlJuros(BigDecimal vlJuros) {
        this.vlJuros = vlJuros;
    }

    public BigDecimal getVlTarifa() {
        return this.vlTarifa;
    }

    public void setVlTarifa(BigDecimal vlTarifa) {
        this.vlTarifa = vlTarifa;
    }

    public BigDecimal getVlCsll() {
        return this.vlCsll;
    }

    public void setVlCsll(BigDecimal vlCsll) {
        this.vlCsll = vlCsll;
    }

    public BigDecimal getVlPis() {
        return this.vlPis;
    }

    public void setVlPis(BigDecimal vlPis) {
        this.vlPis = vlPis;
    }

    public BigDecimal getVlCofins() {
        return this.vlCofins;
    }

    public void setVlCofins(BigDecimal vlCofins) {
        this.vlCofins = vlCofins;
    }

    public BigDecimal getVlIr() {
        return this.vlIr;
    }

    public void setVlIr(BigDecimal vlIr) {
        this.vlIr = vlIr;
    }

    public BigDecimal getVlFrete() {
        return this.vlFrete;
    }

    public void setVlFrete(BigDecimal vlFrete) {
        this.vlFrete = vlFrete;
    }

    public DomainValue getTpSituacaoRelacaoCobranca() {
        return this.tpSituacaoRelacaoCobranca;
    }

	public void setTpSituacaoRelacaoCobranca(
			DomainValue tpSituacaoRelacaoCobranca) {
        this.tpSituacaoRelacaoCobranca = tpSituacaoRelacaoCobranca;
    }

    public String getDsOrigem() {
        return this.dsOrigem;
    }

    public void setDsOrigem(String dsOrigem) {
        this.dsOrigem = dsOrigem;
    }

    public YearMonthDay getDtLiquidacao() {
        return this.dtLiquidacao;
    }

    public void setDtLiquidacao(YearMonthDay dtLiquidacao) {
        this.dtLiquidacao = dtLiquidacao;
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

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class)     
    public List getFaturas() {
        return this.faturas;
    }

    public void setFaturas(List faturas) {
        this.faturas = faturas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRelacaoCobranca",
				getIdRelacaoCobranca()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RelacaoCobranca))
			return false;
        RelacaoCobranca castOther = (RelacaoCobranca) other;
		return new EqualsBuilder().append(this.getIdRelacaoCobranca(),
				castOther.getIdRelacaoCobranca()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRelacaoCobranca())
            .toHashCode();
    }

}
