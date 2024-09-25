package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idTipoProduto;

	/** persistent field */
	private VarcharI18n dsTipoProduto;

	/** persistent field */
	private DomainValue tpSituacao;

	public Long getIdTipoProduto() {
		return this.idTipoProduto;
	}

	public void setIdTipoProduto(Long idTipoProduto) {
		this.idTipoProduto = idTipoProduto;
	}

	public VarcharI18n getDsTipoProduto() {
		return dsTipoProduto;
	}

	public void setDsTipoProduto(VarcharI18n dsTipoProduto) {
		this.dsTipoProduto = dsTipoProduto;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idTipoProduto",
				getIdTipoProduto()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoProduto))
			return false;
		TipoProduto castOther = (TipoProduto) other;
		return new EqualsBuilder().append(this.getIdTipoProduto(),
				castOther.getIdTipoProduto()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoProduto()).toHashCode();
	}

}
