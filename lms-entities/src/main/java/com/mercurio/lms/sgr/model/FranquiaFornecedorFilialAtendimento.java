package com.mercurio.lms.sgr.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "FRANQUIA_FORN_FILIAL_ATEND")
@SequenceGenerator(name = "FRANQUIA_FORN_FILIAL_ATEND_SQ", sequenceName = "FRANQUIA_FORN_FILIAL_ATEND_SQ")
public class FranquiaFornecedorFilialAtendimento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FRANQUIA_FORN_FILIAL_ATEND", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FRANQUIA_FORN_FILIAL_ATEND_SQ")
	private Long idFranquiaEscoltaFilialAtendimento;

	@ManyToOne
	@JoinColumn(name = "ID_FRANQUIA_FORNEC_ESCOLTA", nullable = false)
	private FranquiaFornecedorEscolta franquiaFornecedorEscolta;

	@ManyToOne
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	private Filial filial;

	public Long getIdFranquiaEscoltaFilialAtendimento() {
		return idFranquiaEscoltaFilialAtendimento;
	}

	public void setIdFranquiaEscoltaFilialAtendimento(Long idFranquiaEscoltaFilialAtendimento) {
		this.idFranquiaEscoltaFilialAtendimento = idFranquiaEscoltaFilialAtendimento;
	}

	public FranquiaFornecedorEscolta getFranquiaFornecedorEscolta() {
		return franquiaFornecedorEscolta;
	}

	public void setFranquiaFornecedorEscolta(FranquiaFornecedorEscolta franquiaFornecedorEscolta) {
		this.franquiaFornecedorEscolta = franquiaFornecedorEscolta;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFranquiaEscoltaFilialAtendimento)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FranquiaFornecedorFilialAtendimento)) {
			return false;
		}
		FranquiaFornecedorFilialAtendimento cast = (FranquiaFornecedorFilialAtendimento) other;
		return new EqualsBuilder()
				.append(idFranquiaEscoltaFilialAtendimento, cast.idFranquiaEscoltaFilialAtendimento)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(idFranquiaEscoltaFilialAtendimento)
				.toString();
	}

}
