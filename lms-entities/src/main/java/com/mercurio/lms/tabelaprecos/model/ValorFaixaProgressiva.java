package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ValorFaixaProgressiva implements Serializable, Vigencia {
	
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_ID_FAIXA_PROGRESSIVA = "valorFaixaProgressiva.findValorFaixaProgressivaByFaixaProgressiva";

    /** identifier field */
    private Long idValorFaixaProgressiva;

    /** persistent field */
    private Boolean blPromocional;

    /** nullable persistent field */
    private BigDecimal nrFatorMultiplicacao;

    /** nullable persistent field */
    private BigDecimal vlFixo;

    /** nullable persistent field */
    private BigDecimal pcTaxa;

    /** nullable persistent field */
    private BigDecimal pcDesconto;

    /** nullable persistent field */
    private BigDecimal vlAcrescimo;
    
    /** persistent field */
    private BigDecimal psMinimo;

    /** nullable persistent field */
	@Temporal(TemporalType.DATE)
    private YearMonthDay dtVigenciaPromocaoInicial;

    /** nullable persistent field */
	@Temporal(TemporalType.DATE)
    private YearMonthDay dtVigenciaPromocaoFinal;

    private Integer versao = Integer.valueOf(1);

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.RotaPreco rotaPreco;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.FaixaProgressiva faixaProgressiva;

    /**
     * Campo auxiliar para implementação da lógica de vigência.
     */
    private boolean vigenciaFinalMaiorDataAtual;

    private BigDecimal vlTaxaFixa;

    private BigDecimal vlKmExtra;
    
    public Long getIdValorFaixaProgressiva() {
        return this.idValorFaixaProgressiva;
    }

    public void setIdValorFaixaProgressiva(Long idValorFaixaProgressiva) {
        this.idValorFaixaProgressiva = idValorFaixaProgressiva;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public Boolean getBlPromocional() {
        return this.blPromocional;
    }

    public void setBlPromocional(Boolean blPromocional) {
        this.blPromocional = blPromocional;
    }

    public BigDecimal getNrFatorMultiplicacao() {
        return this.nrFatorMultiplicacao;
    }

    public void setNrFatorMultiplicacao(BigDecimal nrFatorMultiplicacao) {
        this.nrFatorMultiplicacao = nrFatorMultiplicacao;
    }

    public BigDecimal getVlFixo() {
        return this.vlFixo;
    }

    public void setVlFixo(BigDecimal vlFixo) {
        this.vlFixo = vlFixo;
    }

    public BigDecimal getPcTaxa() {
        return this.pcTaxa;
    }

    public void setPcTaxa(BigDecimal pcTaxa) {
        this.pcTaxa = pcTaxa;
    }

    public BigDecimal getPcDesconto() {
        return this.pcDesconto;
    }

    public void setPcDesconto(BigDecimal pcDesconto) {
        this.pcDesconto = pcDesconto;
    }

    public BigDecimal getVlAcrescimo() {
        return this.vlAcrescimo;
    }

    public void setVlAcrescimo(BigDecimal vlAcrescimo) {
        this.vlAcrescimo = vlAcrescimo;
    }
    
    public BigDecimal getPsMinimo() {
        return this.psMinimo;
    }

    public void setPsMinimo(BigDecimal psMinimo) {
        this.psMinimo = psMinimo;
    }

    public YearMonthDay getDtVigenciaPromocaoInicial() {
        return this.dtVigenciaPromocaoInicial;
    }

	public void setDtVigenciaPromocaoInicial(
			YearMonthDay dtVigenciaPromocaoInicial) {
        this.dtVigenciaPromocaoInicial = dtVigenciaPromocaoInicial;
    }

    public YearMonthDay getDtVigenciaPromocaoFinal() {
        return this.dtVigenciaPromocaoFinal;
    }

    public void setDtVigenciaPromocaoFinal(YearMonthDay dtVigenciaPromocaoFinal) {
        this.dtVigenciaPromocaoFinal = dtVigenciaPromocaoFinal;
    }

    public com.mercurio.lms.tabelaprecos.model.TarifaPreco getTarifaPreco() {
        return this.tarifaPreco;
    }

	public void setTarifaPreco(
			com.mercurio.lms.tabelaprecos.model.TarifaPreco tarifaPreco) {
        this.tarifaPreco = tarifaPreco;
    }

    public com.mercurio.lms.tabelaprecos.model.RotaPreco getRotaPreco() {
        return this.rotaPreco;
    }

	public void setRotaPreco(
			com.mercurio.lms.tabelaprecos.model.RotaPreco rotaPreco) {
        this.rotaPreco = rotaPreco;
    }

    public com.mercurio.lms.tabelaprecos.model.FaixaProgressiva getFaixaProgressiva() {
        return this.faixaProgressiva;
    }

	public void setFaixaProgressiva(
			com.mercurio.lms.tabelaprecos.model.FaixaProgressiva faixaProgressiva) {
        this.faixaProgressiva = faixaProgressiva;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idValorFaixaProgressiva",
				getIdValorFaixaProgressiva()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorFaixaProgressiva))
			return false;
        ValorFaixaProgressiva castOther = (ValorFaixaProgressiva) other;
		return new EqualsBuilder().append(this.getIdValorFaixaProgressiva(),
				castOther.getIdValorFaixaProgressiva()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorFaixaProgressiva())
            .toHashCode();
    }

    /**
	 * @return Returns the vigenciaFinalMaiorQueDataAtual.
	 */
	public boolean isVigenciaFinalMaiorDataAtual() {
		return vigenciaFinalMaiorDataAtual;
	}

	/**
	 * @param vigenciaFinalMaiorQueDataAtual
	 *            The vigenciaFinalMaiorQueDataAtual to set.
	 */
	public void setVigenciaFinalMaiorDataAtual(
			boolean vigenciaFinalMaiorDataAtual) {
		this.vigenciaFinalMaiorDataAtual = vigenciaFinalMaiorDataAtual;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaPromocaoInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaPromocaoFinal;
	}

    public BigDecimal getVlTaxaFixa() {
        return vlTaxaFixa;
    }

    public void setVlTaxaFixa(BigDecimal vlTaxaFixa) {
        this.vlTaxaFixa = vlTaxaFixa;
    }

    public BigDecimal getVlKmExtra() {
        return vlKmExtra;
    }

    public void setVlKmExtra(BigDecimal vlKmExtra) {
        this.vlKmExtra = vlKmExtra;
    }
}
