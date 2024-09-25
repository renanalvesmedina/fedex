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

@Entity
@Table(name = "FORNEC_ESCOLTA_RANKING_ITEM")
@SequenceGenerator(name = "FORNEC_ESCOLTA_RANKING_ITEM_SQ", sequenceName = "FORNEC_ESCOLTA_RANKING_ITEM_SQ")
public class FornecEscoltaRankingItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FORNEC_ESCOLTA_RANKING_ITEM", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORNEC_ESCOLTA_RANKING_ITEM_SQ")
	private Long idFornecEscoltaRankingItem;

	@ManyToOne
	@JoinColumn(name = "ID_FORNECEDOR_ESCOLTA_RANK", nullable = false)
	private FornecedorEscoltaRanking fornecedorEscoltaRanking;

	@ManyToOne
	@JoinColumn(name = "ID_FORNECEDOR_ESCOLTA")
	private FornecedorEscolta fornecedorEscolta;
	
	@Column(name = "NR_ORDEM")
	private Long nrOrdem;

	public Long getIdFornecEscoltaRankingItem() {
		return idFornecEscoltaRankingItem;
	}

	public void setIdFornecEscoltaRankingItem(Long idFornecEscoltaRankingItem) {
		this.idFornecEscoltaRankingItem = idFornecEscoltaRankingItem;
	}
	
	public FornecedorEscoltaRanking getFornecedorEscoltaRanking() {
		return fornecedorEscoltaRanking;
	}

	public void setFornecedorEscoltaRanking(FornecedorEscoltaRanking fornecedorEscoltaRanking) {
		this.fornecedorEscoltaRanking = fornecedorEscoltaRanking;
	}

	public FornecedorEscolta getFornecedorEscolta() {
		return fornecedorEscolta;
	}

	public void setFornecedorEscolta(FornecedorEscolta fornecedorEscolta) {
		this.fornecedorEscolta = fornecedorEscolta;
	}

	public Long getNrOrdem() {
		return nrOrdem;
	}

	public void setNrOrdem(Long nrOrdem) {
		this.nrOrdem = nrOrdem;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFornecEscoltaRankingItem)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FornecEscoltaRankingItem)) {
			return false;
		}
		FornecEscoltaRankingItem cast = (FornecEscoltaRankingItem) other;
		return new EqualsBuilder()
				.append(idFornecEscoltaRankingItem, cast.idFornecEscoltaRankingItem)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(idFornecEscoltaRankingItem)
				.toString();
	}

}
