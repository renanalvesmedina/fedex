package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.vendas.model.DivisaoCliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class DivisaoClienteNaturezaProduto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idDivisaoClienteNaturezaProduto;

	private DivisaoCliente divisaoCliente;
	private NaturezaProduto naturezaProduto;
	private String dsNaturezaProdutoCliente;

	public Long getIdDivisaoClienteNaturezaProduto() {
		return idDivisaoClienteNaturezaProduto;
	}

	public void setIdDivisaoClienteNaturezaProduto(
			Long idDivisaoClienteNaturezaProduto) {
		this.idDivisaoClienteNaturezaProduto = idDivisaoClienteNaturezaProduto;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public NaturezaProduto getNaturezaProduto() {
		return naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public String getDsNaturezaProdutoCliente() {
		return dsNaturezaProdutoCliente;
	}

	public void setDsNaturezaProdutoCliente(String dsNaturezaProdutoCliente) {
		this.dsNaturezaProdutoCliente = dsNaturezaProdutoCliente;
	}

	public String toString() {
		return new ToStringBuilder(this).append(
				"idDivisaoClienteNaturezaProduto",
				getIdDivisaoClienteNaturezaProduto()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DivisaoClienteNaturezaProduto))
			return false;
		DivisaoClienteNaturezaProduto castOther = (DivisaoClienteNaturezaProduto) other;
		return new EqualsBuilder().append(
				this.getIdDivisaoClienteNaturezaProduto(),
				castOther.getIdDivisaoClienteNaturezaProduto()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(
				getIdDivisaoClienteNaturezaProduto()).toHashCode();
	}

}
