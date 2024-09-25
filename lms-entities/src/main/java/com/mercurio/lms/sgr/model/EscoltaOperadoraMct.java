package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.contratacaoveiculos.model.OperadoraMct;

public class EscoltaOperadoraMct implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idEscoltaOperadoraMct;
	private Long nrContaOrigem;
	private OperadoraMct operadoraMct;
	private Escolta escolta;

	public Long getIdEscoltaOperadoraMct() {
		return idEscoltaOperadoraMct;
	}

	public void setIdEscoltaOperadoraMct(Long idEscoltaOperadoraMct) {
		this.idEscoltaOperadoraMct = idEscoltaOperadoraMct;
	}

	public Long getNrContaOrigem() {
		return nrContaOrigem;
	}

	public void setNrContaOrigem(Long nrContaOrigem) {
		this.nrContaOrigem = nrContaOrigem;
	}

	public OperadoraMct getOperadoraMct() {
		return operadoraMct;
	}

	public void setOperadoraMct(OperadoraMct operadoraMct) {
		this.operadoraMct = operadoraMct;
	}

	public Escolta getEscolta() {
		return escolta;
	}

	public void setEscolta(Escolta escolta) {
		this.escolta = escolta;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idEscoltaOperadoraMct)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof EscoltaOperadoraMct)) {
			return false;
		}
		EscoltaOperadoraMct cast = (EscoltaOperadoraMct) other;
		return new EqualsBuilder()
				.append(idEscoltaOperadoraMct, cast.idEscoltaOperadoraMct)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idEscoltaOperadoraMct)
				.toHashCode();
	}

}
