package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "FORNECEDOR_ESCOLTA_RANKING")
@SequenceGenerator(name = "FORNECEDOR_ESCOLTA_RANKING_SQ", sequenceName = "FORNECEDOR_ESCOLTA_RANKING_SQ")
public class FornecedorEscoltaRanking implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FORNECEDOR_ESCOLTA_RANKING", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORNECEDOR_ESCOLTA_RANKING_SQ")
	private Long idFornecedorEscoltaRanking;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE")
	private Cliente cliente;

	@ManyToOne(fetch = FetchType.LAZY)  
	@JoinColumn(name = "ID_FILIAL")
	private Filial filial;
	
	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
		
	@OneToMany(mappedBy = "fornecedorEscoltaRanking")
	private List<FornecEscoltaRankingItem> fornecedoresEscoltaRankingItem;
	
	
	public Long getIdFornecedorEscoltaRanking() {
		return idFornecedorEscoltaRanking;
	}

	public void setIdFornecedorEscoltaRanking(Long idFornecedorEscoltaRanking) {
		this.idFornecedorEscoltaRanking = idFornecedorEscoltaRanking;
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

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public List<FornecEscoltaRankingItem> getFornecedoresEscoltaRankingItem() {
		return fornecedoresEscoltaRankingItem;
	}

	public void setFornecedoresEscoltaRankingItem(List<FornecEscoltaRankingItem> fornecedoresEscoltaRankingItem) {
		this.fornecedoresEscoltaRankingItem = fornecedoresEscoltaRankingItem;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFornecedorEscoltaRanking)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FornecedorEscoltaRanking)) {
			return false;
		}
		FornecedorEscoltaRanking cast = (FornecedorEscoltaRanking) other;
		return new EqualsBuilder()
				.append(idFornecedorEscoltaRanking, cast.idFornecedorEscoltaRanking)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(idFornecedorEscoltaRanking)
				.toString();
	}

}
