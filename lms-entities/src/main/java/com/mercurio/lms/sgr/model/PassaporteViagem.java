package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

public class PassaporteViagem implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idPassaporteViagem;
	private String nrLiberacao;
	private List<PostoPassaporte> postoPassaportes;

	public Long getIdPassaporteViagem() {
		return this.idPassaporteViagem;
	}

	public void setIdPassaporteViagem(Long idPassaporteViagem) {
		this.idPassaporteViagem = idPassaporteViagem;
	}

	public String getNrLiberacao() {
		return this.nrLiberacao;
	}

	public void setNrLiberacao(String nrLiberacao) {
		this.nrLiberacao = nrLiberacao;
	}

	@ParametrizedAttribute(type = PostoPassaporte.class)
	public List<PostoPassaporte> getPostoPassaportes() {
		return this.postoPassaportes;
	}

	public void setPostoPassaportes(List<PostoPassaporte> postoPassaportes) {
		this.postoPassaportes = postoPassaportes;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idPassaporteViagem)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof PassaporteViagem)) {
			return false;
		}
		PassaporteViagem cast = (PassaporteViagem) other;
		return new EqualsBuilder()
				.append(idPassaporteViagem, cast.idPassaporteViagem)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idPassaporteViagem)
				.toHashCode();
	}

}
