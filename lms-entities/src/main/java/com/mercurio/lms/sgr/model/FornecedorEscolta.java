package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import com.mercurio.lms.configuracoes.model.Pessoa;

@Entity
@Table(name = "FORNECEDOR_ESCOLTA")
@SequenceGenerator(name = "FORNECEDOR_ESCOLTA_SQ", sequenceName = "FORNECEDOR_ESCOLTA_SQ")
public class FornecedorEscolta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FORNECEDOR_ESCOLTA", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORNECEDOR_ESCOLTA_SQ")
	private Long idFornecedorEscolta;

	@ManyToOne
	@JoinColumn(name = "ID_PESSOA", nullable = false)
	private Pessoa pessoa;

	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;

	@Column(name = "DS_EMAIL_FORNECEDOR")
	private String dsEmailFornecedor;

	@Column(name = "DS_TELEFONE_1")
	private String dsTelefone1;

	@Column(name = "DS_TELEFONE_2")
	private String dsTelefone2;

	@Column(name = "DS_TELEFONE_3")
	private String dsTelefone3;

	@OneToMany(mappedBy = "fornecedorEscolta")
	private List<FranquiaFornecedorEscolta> franquiasFornecedorEscolta;

	@OneToMany(mappedBy = "fornecedorEscolta")
	private List<FornecedorEscoltaImpedido> fornecedoresEscoltaImpedidos;
	
	@OneToMany(mappedBy = "fornecedorEscolta")
	private List<FornecEscoltaRankingItem> fornecedoresEscoltaRankingItem;
	
	public Long getIdFornecedorEscolta() {
		return idFornecedorEscolta;
	}

	public void setIdFornecedorEscolta(Long idFornecedorEscolta) {
		this.idFornecedorEscolta = idFornecedorEscolta;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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

	public String getDsEmailFornecedor() {
		return dsEmailFornecedor;
	}

	public void setDsEmailFornecedor(String dsEmailFornecedor) {
		this.dsEmailFornecedor = dsEmailFornecedor;
	}

	public String getDsTelefone1() {
		return dsTelefone1;
	}

	public void setDsTelefone1(String dsTelefone1) {
		this.dsTelefone1 = dsTelefone1;
	}

	public String getDsTelefone2() {
		return dsTelefone2;
	}

	public void setDsTelefone2(String dsTelefone2) {
		this.dsTelefone2 = dsTelefone2;
	}

	public String getDsTelefone3() {
		return dsTelefone3;
	}

	public void setDsTelefone3(String dsTelefone3) {
		this.dsTelefone3 = dsTelefone3;
	}

	public List<FranquiaFornecedorEscolta> getFranquiasFornecedorEscolta() {
		return franquiasFornecedorEscolta;
	}

	public void setFranquiasFornecedorEscolta(List<FranquiaFornecedorEscolta> franquiasFornecedorEscolta) {
		this.franquiasFornecedorEscolta = franquiasFornecedorEscolta;
	}

	public List<FornecedorEscoltaImpedido> getFornecedoresEscoltaImpedidos() {
		return fornecedoresEscoltaImpedidos;
	}

	public void setFornecedoresEscoltaImpedidos(List<FornecedorEscoltaImpedido> fornecedoresEscoltaImpedidos) {
		this.fornecedoresEscoltaImpedidos = fornecedoresEscoltaImpedidos;
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
				.append(idFornecedorEscolta)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FornecedorEscolta)) {
			return false;
		}
		FornecedorEscolta cast = (FornecedorEscolta) other;
		return new EqualsBuilder()
				.append(idFornecedorEscolta, cast.idFornecedorEscolta)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(idFornecedorEscolta)
				.toString();
	}

}
