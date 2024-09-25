package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

public class PostoPassaporte implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idPostoPassaporte;
	private DateTime dhChegada;
	private DateTime dhPrevisaoChegada;
	private DateTime dhLiberacao;
	private PassaporteViagem passaporteViagem;
	private PostoControle postoControle;

	public Long getIdPostoPassaporte() {
		return idPostoPassaporte;
	}

	public void setIdPostoPassaporte(Long idPostoPassaporte) {
		this.idPostoPassaporte = idPostoPassaporte;
	}

	public DateTime getDhChegada() {
		return dhChegada;
	}

	public void setDhChegada(DateTime dhChegada) {
		this.dhChegada = dhChegada;
	}

	public DateTime getDhPrevisaoChegada() {
		return dhPrevisaoChegada;
	}

	public void setDhPrevisaoChegada(DateTime dhPrevisaoChegada) {
		this.dhPrevisaoChegada = dhPrevisaoChegada;
	}

	public DateTime getDhLiberacao() {
		return dhLiberacao;
	}

	public void setDhLiberacao(DateTime dhLiberacao) {
		this.dhLiberacao = dhLiberacao;
	}

	public PassaporteViagem getPassaporteViagem() {
		return passaporteViagem;
	}

	public void setPassaporteViagem(PassaporteViagem passaporteViagem) {
		this.passaporteViagem = passaporteViagem;
	}

	public PostoControle getPostoControle() {
		return postoControle;
	}

	public void setPostoControle(PostoControle postoControle) {
		this.postoControle = postoControle;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idPostoPassaporte)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof PostoPassaporte)) {
			return false;
		}
		PostoPassaporte cast = (PostoPassaporte) other;
		return new EqualsBuilder()
				.append(idPostoPassaporte, cast.idPostoPassaporte)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idPostoPassaporte)
				.toHashCode();
	}

}
