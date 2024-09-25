package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Densidade implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idDensidade;

	/** persistent field */
	private BigDecimal vlFator;

	/** persistent field */
	private DomainValue tpDensidade;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private com.mercurio.lms.municipios.model.Empresa empresa;

	public Long getIdDensidade() {
		return this.idDensidade;
	}

	public void setIdDensidade(Long idDensidade) {
		this.idDensidade = idDensidade;
	}

	public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
		this.empresa = empresa;
	}

	public BigDecimal getVlFator() {
		return this.vlFator;
	}

	public void setVlFator(BigDecimal vlFator) {
		this.vlFator = vlFator;
	}

	public DomainValue getTpDensidade() {
		return this.tpDensidade;
	}

	public void setTpDensidade(DomainValue tpDensidade) {
		this.tpDensidade = tpDensidade;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("idDensidade", getIdDensidade()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Densidade))
			return false;
		Densidade castOther = (Densidade) other;
		return new EqualsBuilder().append(this.getIdDensidade(),
				castOther.getIdDensidade()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdDensidade()).toHashCode();
	}

}
