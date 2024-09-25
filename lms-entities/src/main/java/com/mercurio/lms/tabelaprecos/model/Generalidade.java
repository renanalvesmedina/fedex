package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class Generalidade implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idGeneralidade;

	/** persistent field */
	private BigDecimal vlGeneralidade;

	/** nullable persistent field */
	private BigDecimal vlMinimo;

	/** nullable persistent field */
	private BigDecimal psMinimo;

	/** nullable persistent field */
	private TabelaPrecoParcela tabelaPrecoParcela;

	public Long getIdGeneralidade() {
		return this.idGeneralidade;
	}

	public void setIdGeneralidade(Long idGeneralidade) {
		this.idGeneralidade = idGeneralidade;
	}

	public BigDecimal getVlGeneralidade() {
		return this.vlGeneralidade;
	}

	public void setVlGeneralidade(BigDecimal vlGeneralidade) {
		this.vlGeneralidade = vlGeneralidade;
	}

	public BigDecimal getVlMinimo() {
		return this.vlMinimo;
	}

	public void setVlMinimo(BigDecimal vlMinimo) {
		this.vlMinimo = vlMinimo;
	}

	public BigDecimal getPsMinimo() {
		return psMinimo;
	}

	public void setPsMinimo(BigDecimal psMinimo) {
		this.psMinimo = psMinimo;
	}

	public TabelaPrecoParcela getTabelaPrecoParcela() {
		return this.tabelaPrecoParcela;
	}

	public void setTabelaPrecoParcela(TabelaPrecoParcela tabelaPrecoParcela) {
		this.tabelaPrecoParcela = tabelaPrecoParcela;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idGeneralidade",
				getIdGeneralidade()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Generalidade))
			return false;
		Generalidade castOther = (Generalidade) other;
		return new EqualsBuilder().append(this.getIdGeneralidade(),
				castOther.getIdGeneralidade()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdGeneralidade()).toHashCode();
	}

}
