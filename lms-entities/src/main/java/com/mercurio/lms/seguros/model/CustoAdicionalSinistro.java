package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class CustoAdicionalSinistro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCustoAdicionalSinistro;

    /** persistent field */
    private YearMonthDay dtCustoAdicional;

    /** persistent field */
    private String dsCustoAdicional;
    
    /** nullable persistent field */
    private YearMonthDay dtReembolsado;
    
    /** persistent field */
    private BigDecimal vlCustoAdicional;

    /** nullable persistent field */
    private BigDecimal vlReembolsado;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro;

    public Long getIdCustoAdicionalSinistro() {
        return this.idCustoAdicionalSinistro;
    }

    public void setIdCustoAdicionalSinistro(Long idCustoAdicionalSinistro) {
        this.idCustoAdicionalSinistro = idCustoAdicionalSinistro;
    }

    public YearMonthDay getDtCustoAdicional() {
        return this.dtCustoAdicional;
    }

    public void setDtCustoAdicional(YearMonthDay dtCustoAdicional) {
        this.dtCustoAdicional = dtCustoAdicional;
    }

    public YearMonthDay getDtReembolsado() {
		return dtReembolsado;
	}

	public void setDtReembolsado(YearMonthDay dtReembolsado) {
		this.dtReembolsado = dtReembolsado;
	}

	public String getDsCustoAdicional() {
        return this.dsCustoAdicional;
    }

    public void setDsCustoAdicional(String dsCustoAdicional) {
        this.dsCustoAdicional = dsCustoAdicional;
    }

    public BigDecimal getVlCustoAdicional() {
        return this.vlCustoAdicional;
    }

    public void setVlCustoAdicional(BigDecimal vlCustoAdicional) {
        this.vlCustoAdicional = vlCustoAdicional;
    }

    public BigDecimal getVlReembolsado() {
        return this.vlReembolsado;
    }

    public void setVlReembolsado(BigDecimal vlReembolsado) {
        this.vlReembolsado = vlReembolsado;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.seguros.model.ProcessoSinistro getProcessoSinistro() {
        return this.processoSinistro;
    }

	public void setProcessoSinistro(
			com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro) {
        this.processoSinistro = processoSinistro;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCustoAdicionalSinistro",
				getIdCustoAdicionalSinistro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CustoAdicionalSinistro))
			return false;
        CustoAdicionalSinistro castOther = (CustoAdicionalSinistro) other;
		return new EqualsBuilder().append(this.getIdCustoAdicionalSinistro(),
				castOther.getIdCustoAdicionalSinistro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCustoAdicionalSinistro())
            .toHashCode();
    }

}
