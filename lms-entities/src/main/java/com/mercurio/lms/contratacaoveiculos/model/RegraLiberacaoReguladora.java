package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RegraLiberacaoReguladora implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegraLiberacaoReguladora;

    /** persistent field */
    private DomainValue tpVinculo;

    /** persistent field */
    private Byte qtMesesValidade;

    /** persistent field */
    private Boolean blLiberacaoPorViagem;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */ 
    private Integer qtViagensAnoLiberacao;

    /** persistent field */
    private com.mercurio.lms.seguros.model.ReguladoraSeguro reguladoraSeguro;

    public Long getIdRegraLiberacaoReguladora() {
        return this.idRegraLiberacaoReguladora;
    }

    public void setIdRegraLiberacaoReguladora(Long idRegraLiberacaoReguladora) {
        this.idRegraLiberacaoReguladora = idRegraLiberacaoReguladora;
    }

    public DomainValue getTpVinculo() {
        return this.tpVinculo;
    }

    public void setTpVinculo(DomainValue tpVinculo) {
        this.tpVinculo = tpVinculo;
    }

    public Byte getQtMesesValidade() {
        return this.qtMesesValidade;
    }

    public void setQtMesesValidade(Byte qtMesesValidade) {
        this.qtMesesValidade = qtMesesValidade;
    }

    public Boolean getBlLiberacaoPorViagem() {
        return this.blLiberacaoPorViagem;
    }

    public void setBlLiberacaoPorViagem(Boolean blLiberacaoPorViagem) {
        this.blLiberacaoPorViagem = blLiberacaoPorViagem;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public Integer getQtViagensAnoLiberacao() {
        return this.qtViagensAnoLiberacao;
    }

    public void setQtViagensAnoLiberacao(Integer qtViagensAnoLiberacao) {
        this.qtViagensAnoLiberacao = qtViagensAnoLiberacao;
    }

    public com.mercurio.lms.seguros.model.ReguladoraSeguro getReguladoraSeguro() {
        return this.reguladoraSeguro;
    }

	public void setReguladoraSeguro(
			com.mercurio.lms.seguros.model.ReguladoraSeguro reguladoraSeguro) {
        this.reguladoraSeguro = reguladoraSeguro;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRegraLiberacaoReguladora",
				getIdRegraLiberacaoReguladora()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegraLiberacaoReguladora))
			return false;
        RegraLiberacaoReguladora castOther = (RegraLiberacaoReguladora) other;
		return new EqualsBuilder().append(this.getIdRegraLiberacaoReguladora(),
				castOther.getIdRegraLiberacaoReguladora()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegraLiberacaoReguladora())
            .toHashCode();
    }

}
