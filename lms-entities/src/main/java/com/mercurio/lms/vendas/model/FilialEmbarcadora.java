package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class FilialEmbarcadora implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idFilialEmbarcadora;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private Filial filial;

	public Long getIdFilialEmbarcadora() {
		return idFilialEmbarcadora;
	}

	public void setIdFilialEmbarcadora(Long idFilialEmbarcadora) {
		this.idFilialEmbarcadora = idFilialEmbarcadora;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idFilialEmbarcadora",
				getIdFilialEmbarcadora()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialEmbarcadora))
			return false;
		FilialEmbarcadora castOther = (FilialEmbarcadora) other;
		return new EqualsBuilder().append(this.getIdFilialEmbarcadora(),
				castOther.getIdFilialEmbarcadora()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialEmbarcadora())
			.toHashCode();
	}
}
