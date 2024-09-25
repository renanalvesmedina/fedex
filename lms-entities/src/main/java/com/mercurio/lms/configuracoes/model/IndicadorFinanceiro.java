package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class IndicadorFinanceiro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIndicadorFinanceiro;

    /** persistent field */
    private String nmIndicadorFinanceiro;

    /** persistent field */
    private String sgIndicadorFinanceiro;

    /** persistent field */
    private DomainValue tpIndicadorFinanceiro;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Frequencia frequencia;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    /** persistent field */
    private List cotacaoIndicadorFinanceiros;

    public Long getIdIndicadorFinanceiro() {
        return this.idIndicadorFinanceiro;
    }

    public void setIdIndicadorFinanceiro(Long idIndicadorFinanceiro) {
        this.idIndicadorFinanceiro = idIndicadorFinanceiro;
    }

    public String getNmIndicadorFinanceiro() {
        return this.nmIndicadorFinanceiro;
    }

    public void setNmIndicadorFinanceiro(String nmIndicadorFinanceiro) {
        this.nmIndicadorFinanceiro = nmIndicadorFinanceiro;
    }

    public String getSgIndicadorFinanceiro() {
        return this.sgIndicadorFinanceiro;
    }

    public void setSgIndicadorFinanceiro(String sgIndicadorFinanceiro) {
        this.sgIndicadorFinanceiro = sgIndicadorFinanceiro;
    }

    public DomainValue getTpIndicadorFinanceiro() {
        return this.tpIndicadorFinanceiro;
    }

    public void setTpIndicadorFinanceiro(DomainValue tpIndicadorFinanceiro) {
        this.tpIndicadorFinanceiro = tpIndicadorFinanceiro;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.configuracoes.model.Frequencia getFrequencia() {
        return this.frequencia;
    }

	public void setFrequencia(
			com.mercurio.lms.configuracoes.model.Frequencia frequencia) {
        this.frequencia = frequencia;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.CotacaoIndicadorFinanceiro.class)     
    public List getCotacaoIndicadorFinanceiros() {
        return this.cotacaoIndicadorFinanceiros;
    }

    public void setCotacaoIndicadorFinanceiros(List cotacaoIndicadorFinanceiros) {
        this.cotacaoIndicadorFinanceiros = cotacaoIndicadorFinanceiros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idIndicadorFinanceiro",
				getIdIndicadorFinanceiro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof IndicadorFinanceiro))
			return false;
        IndicadorFinanceiro castOther = (IndicadorFinanceiro) other;
		return new EqualsBuilder().append(this.getIdIndicadorFinanceiro(),
				castOther.getIdIndicadorFinanceiro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIndicadorFinanceiro())
            .toHashCode();
    }

}
