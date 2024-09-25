package com.mercurio.lms.vendas.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;

@Entity
@Table(name = "FAIXA_PROGRESSIVA_PROPOSTA")
@SequenceGenerator(name = "FAIXA_PROGRESSIVA_PROPOSTA_SEQ", sequenceName = "FAIXA_PROGRESSIVA_PROPOSTA_SQ", allocationSize = 1)
public class FaixaProgressivaProposta implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FAIXA_PROGRESSIVA_PROPOSTA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAIXA_PROGRESSIVA_PROPOSTA_SEQ")
	private Long idFaixaProgressivaProposta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PRODUTO_ESPECIFICO", nullable = true)
	private ProdutoEspecifico produtoEspecifico;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SIMULACAO", nullable = false)
	private Simulacao simulacao;
	
	@Column(name = "VALOR_FAIXA", nullable = true)
	private BigDecimal vlFaixa;
	
	public String toString() {
		return new ToStringBuilder(this).append("idFaixaProgressivaProposta", getIdFaixaProgressivaProposta()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaixaProgressivaProposta))
			return false;
		FaixaProgressivaProposta castOther = (FaixaProgressivaProposta) other;
		return new EqualsBuilder().append(this.getIdFaixaProgressivaProposta(), castOther.getIdFaixaProgressivaProposta()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdFaixaProgressivaProposta()).toHashCode();
	}
	
	public Long getIdFaixaProgressivaProposta() {
		return idFaixaProgressivaProposta;
	}

	public void setIdFaixaProgressivaProposta(Long idFaixaProgressivaProposta) {
		this.idFaixaProgressivaProposta = idFaixaProgressivaProposta;
	}

	public ProdutoEspecifico getProdutoEspecifico() {
		return produtoEspecifico;
	}

	public void setProdutoEspecifico(ProdutoEspecifico produtoEspecifico) {
		this.produtoEspecifico = produtoEspecifico;
	}

	public Simulacao getSimulacao() {
		return simulacao;
	}

	public void setSimulacao(Simulacao simulacao) {
		this.simulacao = simulacao;
	}

	public BigDecimal getVlFaixa() {
		return vlFaixa;
	}

	public void setVlFaixa(BigDecimal vlFaixa) {
		this.vlFaixa = vlFaixa;
	}
}
