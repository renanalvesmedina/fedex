package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ImpressoraComputador implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idImpressoraComputador;

	/** nullable persistent field */
	private String dsMac;

	/** persistent field */
	private Impressora impressora;
	
	private Boolean blEstacaoAutomatizada;
	
	/**
	 * @return the idImpressoraComputador
	 */
	public Long getIdImpressoraComputador() {
		return idImpressoraComputador;
	}

	/**
	 * @param idImpressoraComputador
	 *            the idImpressoraComputador to set
	 */
	public void setIdImpressoraComputador(Long idImpressoraComputador) {
		this.idImpressoraComputador = idImpressoraComputador;
	}

	/**
	 * @return the dsMac
	 */
	public String getDsMac() {
		return dsMac;
	}

	/**
	 * @param dsMac
	 *            the dsMac to set
	 */
	public void setDsMac(String dsMac) {
		this.dsMac = dsMac;
	}

	/**
	 * @return the impressora
	 */
	public Impressora getImpressora() {
		return impressora;
	}

	/**
	 * @param impressora
	 *            the impressora to set
	 */
	public void setImpressora(Impressora impressora) {
		this.impressora = impressora;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idImpressoraComputador",
				getIdImpressoraComputador()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ImpressoraComputador))
			return false;
		ImpressoraComputador castOther = (ImpressoraComputador) other;
		return new EqualsBuilder().append(this.getIdImpressoraComputador(),
				castOther.getIdImpressoraComputador()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdImpressoraComputador())
			.toHashCode();
	}

	public Boolean getBlEstacaoAutomatizada() {
		return blEstacaoAutomatizada;
}

	public void setBlEstacaoAutomatizada(Boolean blEstacaoAutomatizada) {
		this.blEstacaoAutomatizada = blEstacaoAutomatizada;
	}
}
