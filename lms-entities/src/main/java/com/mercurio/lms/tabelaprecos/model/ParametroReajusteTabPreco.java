package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="PARAMETRO_REAJUSTE_TAB_PRECO")
@SequenceGenerator(name = "PARAMETRO_REAJ_TAB_PRECO_SQ", sequenceName = "PARAMETRO_REAJ_TAB_PRECO_SQ")
public class ParametroReajusteTabPreco implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_PARAMETRO_REAJ_TAB_PRECO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETRO_REAJ_TAB_PRECO_SQ")
	private Long id;

	@Column(name="ID_REAJUSTE_TAB_PRECO_PARCELA")
	private Long idReajusteTabPrecoParcela;
	
	@Column(name="ID_PRECO_FRETE")
	private Long idPrecoFrete;
	
	@Column(name="ID_VALOR_FAIXA_PROGRESSIVA")
	private Long idValorFaixaProgressiva;
	
	@Column(name="PC_REAJUSTE_VALOR_PARCELA")
	private BigDecimal percentualParcela;
	
	@Column(name="PC_REAJUSTE_VAL_MIN_PARCELA")
	private BigDecimal percentualMinimoParcela;
	
	@Column(name="VALOR_PARCELA")
	private BigDecimal valorParcela;

	@Column(name="VALOR_MINIMO_PARCELA")
	private BigDecimal valorMinimoParcela;
	
	
	
	public ParametroReajusteTabPreco() { 
	}
	
	public ParametroReajusteTabPreco(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdReajusteTabPrecoParcela() {
		return idReajusteTabPrecoParcela;
	}

	public void setIdReajusteTabPrecoParcela(Long idReajusteTabPrecoParcela) {
		this.idReajusteTabPrecoParcela = idReajusteTabPrecoParcela;
	}

	public Long getIdPrecoFrete() {
		return idPrecoFrete;
	}

	public void setIdPrecoFrete(Long idPrecoFrete) {
		this.idPrecoFrete = idPrecoFrete;
	}

	public Long getIdValorFaixaProgressiva() {
		return idValorFaixaProgressiva;
	}

	public void setIdValorFaixaProgressiva(Long idValorFaixaProgressiva) {
		this.idValorFaixaProgressiva = idValorFaixaProgressiva;
	}

	public BigDecimal getPercentualParcela() {
		return percentualParcela;
	}

	public void setPercentualParcela(BigDecimal percentualParcela) {
		this.percentualParcela = percentualParcela;
	}

	public BigDecimal getPercentualMinimoParcela() {
		return percentualMinimoParcela;
	}

	public void setPercentualMinimoParcela(BigDecimal percentualMinimoParcela) {
		this.percentualMinimoParcela = percentualMinimoParcela;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public BigDecimal getValorMinimoParcela() {
		return valorMinimoParcela;
	}

	public void setValorMinimoParcela(BigDecimal valorMinimoParcela) {
		this.valorMinimoParcela = valorMinimoParcela;
	}

}
