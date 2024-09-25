package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoAgrupamento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoAgrupamento;

    /** persistent field */
    private String cdTipoAgrupamento;

    /** persistent field */
    private String dsTipoAgrupamento;

    /** persistent field */
    private com.mercurio.lms.vendas.model.AgrupamentoCliente agrupamentoCliente;

    /** persistent field */
    private List faturas;

    public Long getIdTipoAgrupamento() {
        return this.idTipoAgrupamento;
    }

    public void setIdTipoAgrupamento(Long idTipoAgrupamento) {
        this.idTipoAgrupamento = idTipoAgrupamento;
    }

    public String getCdTipoAgrupamento() {
        return this.cdTipoAgrupamento;
    }

    public void setCdTipoAgrupamento(String cdTipoAgrupamento) {
        this.cdTipoAgrupamento = cdTipoAgrupamento;
    }

    public String getDsTipoAgrupamento() {
        return this.dsTipoAgrupamento;
    }

    public void setDsTipoAgrupamento(String dsTipoAgrupamento) {
        this.dsTipoAgrupamento = dsTipoAgrupamento;
    }

    public com.mercurio.lms.vendas.model.AgrupamentoCliente getAgrupamentoCliente() {
        return this.agrupamentoCliente;
    }

	public void setAgrupamentoCliente(
			com.mercurio.lms.vendas.model.AgrupamentoCliente agrupamentoCliente) {
        this.agrupamentoCliente = agrupamentoCliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class)     
    public List getFaturas() {
        return this.faturas;
    }

    public void setFaturas(List faturas) {
        this.faturas = faturas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoAgrupamento",
				getIdTipoAgrupamento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoAgrupamento))
			return false;
        TipoAgrupamento castOther = (TipoAgrupamento) other;
		return new EqualsBuilder().append(this.getIdTipoAgrupamento(),
				castOther.getIdTipoAgrupamento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoAgrupamento())
            .toHashCode();
    }

}
