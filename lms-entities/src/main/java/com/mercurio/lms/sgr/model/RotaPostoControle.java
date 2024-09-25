package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.municipios.model.Rota;

public class RotaPostoControle implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idRotaPostoControle;
	private Short nrOrdem;
	private Long nrTempoProximoPosto;
	private Integer nrKmProximoPosto;
	private Rota rota;
	private PostoControle postoControle;

	public Long getIdRotaPostoControle() {
		return idRotaPostoControle;
	}

	public void setIdRotaPostoControle(Long idRotaPostoControle) {
		this.idRotaPostoControle = idRotaPostoControle;
	}

	public Short getNrOrdem() {
		return nrOrdem;
	}

	public void setNrOrdem(Short nrOrdem) {
		this.nrOrdem = nrOrdem;
	}

	public Long getNrTempoProximoPosto() {
		return nrTempoProximoPosto;
	}

	public void setNrTempoProximoPosto(Long nrTempoProximoPosto) {
		this.nrTempoProximoPosto = nrTempoProximoPosto;
	}

	public Integer getNrKmProximoPosto() {
		return nrKmProximoPosto;
	}

	public void setNrKmProximoPosto(Integer nrKmProximoPosto) {
		this.nrKmProximoPosto = nrKmProximoPosto;
	}

	public Rota getRota() {
		return rota;
	}

	public void setRota(Rota rota) {
		this.rota = rota;
	}

	public PostoControle getPostoControle() {
		return postoControle;
	}

	public void setPostoControle(PostoControle postoControle) {
		this.postoControle = postoControle;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idRotaPostoControle)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof RotaPostoControle)) {
			return false;
		}
		RotaPostoControle cast = (RotaPostoControle) other;
		return new EqualsBuilder()
				.append(idRotaPostoControle, cast.idRotaPostoControle)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idRotaPostoControle)
				.toHashCode();
	}

}
