package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class AgrupamentoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAgrupamentoCliente;

    /** persistent field */
    private com.mercurio.lms.vendas.model.FormaAgrupamento formaAgrupamento;

    /** persistent field */
    private com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente;

    /** persistent field */
    private List tipoAgrupamentos;

    /** persistent field */
    private List faturas;

    public Long getIdAgrupamentoCliente() {
        return this.idAgrupamentoCliente;
    }

    public void setIdAgrupamentoCliente(Long idAgrupamentoCliente) {
        this.idAgrupamentoCliente = idAgrupamentoCliente;
    }

    public com.mercurio.lms.vendas.model.FormaAgrupamento getFormaAgrupamento() {
        return this.formaAgrupamento;
    }

	public void setFormaAgrupamento(
			com.mercurio.lms.vendas.model.FormaAgrupamento formaAgrupamento) {
        this.formaAgrupamento = formaAgrupamento;
    }

    public com.mercurio.lms.vendas.model.DivisaoCliente getDivisaoCliente() {
        return this.divisaoCliente;
    }

	public void setDivisaoCliente(
			com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente) {
        this.divisaoCliente = divisaoCliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.TipoAgrupamento.class)     
    public List getTipoAgrupamentos() {
        return this.tipoAgrupamentos;
    }

    public void setTipoAgrupamentos(List tipoAgrupamentos) {
        this.tipoAgrupamentos = tipoAgrupamentos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class)     
    public List getFaturas() {
        return this.faturas;
    }

    public void setFaturas(List faturas) {
        this.faturas = faturas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAgrupamentoCliente",
				getIdAgrupamentoCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AgrupamentoCliente))
			return false;
        AgrupamentoCliente castOther = (AgrupamentoCliente) other;
		return new EqualsBuilder().append(this.getIdAgrupamentoCliente(),
				castOther.getIdAgrupamentoCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAgrupamentoCliente())
            .toHashCode();
    }

}
