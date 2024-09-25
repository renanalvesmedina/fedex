package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ImpostoCalculado implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idImpostoCalculado;

    /** persistent field */
    private BigDecimal vlBaseCalculo;

    /** persistent field */
    private BigDecimal vlImpostoCalculado;

    /** persistent field */
    private BigDecimal vlOutrosRecebimentos;

    /** persistent field */
    private YearMonthDay dtCompetenciaAnoMes;

    /** persistent field */
    private DomainValue tpImposto;

    /** persistent field */
    private DomainValue tpRecolhimento;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** full constructor */
	public ImpostoCalculado(BigDecimal vlBaseCalculo,
			BigDecimal vlImpostoCalculado, BigDecimal vlOutrosRecebimentos,
			YearMonthDay dtCompetenciaAnoMes, DomainValue tpImposto,
			DomainValue tpRecolhimento,
			com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.vlBaseCalculo = vlBaseCalculo;
        this.vlImpostoCalculado = vlImpostoCalculado;
        this.vlOutrosRecebimentos = vlOutrosRecebimentos;
        this.dtCompetenciaAnoMes = dtCompetenciaAnoMes;
        this.tpImposto = tpImposto;
        this.tpRecolhimento = tpRecolhimento;
        this.pessoa = pessoa;
    }

    /** default constructor */
    public ImpostoCalculado() {
    }

    public Long getIdImpostoCalculado() {
        return this.idImpostoCalculado;
    }

    public void setIdImpostoCalculado(Long idImpostoCalculado) {
        this.idImpostoCalculado = idImpostoCalculado;
    }

    public BigDecimal getVlBaseCalculo() {
        return this.vlBaseCalculo;
    }

    public void setVlBaseCalculo(BigDecimal vlBaseCalculo) {
        this.vlBaseCalculo = vlBaseCalculo;
    }

    public BigDecimal getVlImpostoCalculado() {
        return this.vlImpostoCalculado;
    }

    public void setVlImpostoCalculado(BigDecimal vlImpostoCalculado) {
        this.vlImpostoCalculado = vlImpostoCalculado;
    }

    public BigDecimal getVlOutrosRecebimentos() {
        return this.vlOutrosRecebimentos;
    }

    public void setVlOutrosRecebimentos(BigDecimal vlOutrosRecebimentos) {
        this.vlOutrosRecebimentos = vlOutrosRecebimentos;
    }

    public YearMonthDay getDtCompetenciaAnoMes() {
        return this.dtCompetenciaAnoMes;
    }

    public void setDtCompetenciaAnoMes(YearMonthDay dtCompetenciaAnoMes) {
        this.dtCompetenciaAnoMes = dtCompetenciaAnoMes;
    }

    public DomainValue getTpImposto() {
        return this.tpImposto;
    }

    public void setTpImposto(DomainValue tpImposto) {
        this.tpImposto = tpImposto;
    }

    public DomainValue getTpRecolhimento() {
        return this.tpRecolhimento;
    }

    public void setTpRecolhimento(DomainValue tpRecolhimento) {
        this.tpRecolhimento = tpRecolhimento;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idImpostoCalculado",
				getIdImpostoCalculado()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ImpostoCalculado))
			return false;
        ImpostoCalculado castOther = (ImpostoCalculado) other;
		return new EqualsBuilder().append(this.getIdImpostoCalculado(),
				castOther.getIdImpostoCalculado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdImpostoCalculado())
            .toHashCode();
    }

}
