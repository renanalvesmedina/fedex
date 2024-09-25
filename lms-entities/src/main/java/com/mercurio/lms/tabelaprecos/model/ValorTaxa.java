package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorTaxa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idValorTaxa;

    /** persistent field */
    private BigDecimal vlTaxa;

    /** nullable persistent field */
    private BigDecimal psTaxado;

    /** nullable persistent field */
    private BigDecimal vlExcedente;

    /** nullable persistent field */
    private com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela tabelaPrecoParcela;

    public Long getIdValorTaxa() {
        return this.idValorTaxa;
    }

    public void setIdValorTaxa(Long idValorTaxa) {
        this.idValorTaxa = idValorTaxa;
    }

    public BigDecimal getVlTaxa() {
        return this.vlTaxa;
    }

    public void setVlTaxa(BigDecimal vlTaxa) {
        this.vlTaxa = vlTaxa;
    }

    public BigDecimal getPsTaxado() {
        return this.psTaxado;
    }

    public void setPsTaxado(BigDecimal psTaxado) {
        this.psTaxado = psTaxado;
    }

    public BigDecimal getVlExcedente() {
        return this.vlExcedente;
    }

    public void setVlExcedente(BigDecimal vlExcedente) {
        this.vlExcedente = vlExcedente;
    }

    public com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela getTabelaPrecoParcela() {
        return this.tabelaPrecoParcela;
    }

	public void setTabelaPrecoParcela(
			com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela tabelaPrecoParcela) {
        this.tabelaPrecoParcela = tabelaPrecoParcela;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idValorTaxa", getIdValorTaxa()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorTaxa))
			return false;
        ValorTaxa castOther = (ValorTaxa) other;
		return new EqualsBuilder().append(this.getIdValorTaxa(),
				castOther.getIdValorTaxa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorTaxa()).toHashCode();
    }

}
