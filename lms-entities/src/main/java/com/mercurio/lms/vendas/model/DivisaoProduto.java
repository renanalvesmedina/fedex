package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DivisaoProduto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDivisaoProduto;

    /** nullable persistent field */
    private BigDecimal vlMedioKg;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Produto produto;

    /** persistent field */
    private com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    public Long getIdDivisaoProduto() {
        return this.idDivisaoProduto;
    }

    public void setIdDivisaoProduto(Long idDivisaoProduto) {
        this.idDivisaoProduto = idDivisaoProduto;
    }

    public BigDecimal getVlMedioKg() {
        return this.vlMedioKg;
    }

    public void setVlMedioKg(BigDecimal vlMedioKg) {
        this.vlMedioKg = vlMedioKg;
    }

    public com.mercurio.lms.expedicao.model.NaturezaProduto getNaturezaProduto() {
        return this.naturezaProduto;
    }

	public void setNaturezaProduto(
			com.mercurio.lms.expedicao.model.NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

    public com.mercurio.lms.expedicao.model.Produto getProduto() {
        return this.produto;
    }

    public void setProduto(com.mercurio.lms.expedicao.model.Produto produto) {
        this.produto = produto;
    }

    public com.mercurio.lms.vendas.model.DivisaoCliente getDivisaoCliente() {
        return this.divisaoCliente;
    }

	public void setDivisaoCliente(
			com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente) {
        this.divisaoCliente = divisaoCliente;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDivisaoProduto",
				getIdDivisaoProduto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DivisaoProduto))
			return false;
        DivisaoProduto castOther = (DivisaoProduto) other;
		return new EqualsBuilder().append(this.getIdDivisaoProduto(),
				castOther.getIdDivisaoProduto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDivisaoProduto()).toHashCode();
    }

}
