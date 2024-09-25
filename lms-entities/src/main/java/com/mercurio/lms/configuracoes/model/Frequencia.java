package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class Frequencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFrequencia;

    /** persistent field */
    private VarcharI18n dsFrequencia;

    /** persistent field */
    private DomainValue tpFrequenciaIndicadorFinanc;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private Byte ddCorte;

    /** persistent field */
    private List indicadorFinanceiros;

    public Long getIdFrequencia() {
        return this.idFrequencia;
    }

    public void setIdFrequencia(Long idFrequencia) {
        this.idFrequencia = idFrequencia;
    }

    public VarcharI18n getDsFrequencia() {
		return dsFrequencia;
    }

	public void setDsFrequencia(VarcharI18n dsFrequencia) {
        this.dsFrequencia = dsFrequencia;
    }

    public DomainValue getTpFrequenciaIndicadorFinanc() {
        return this.tpFrequenciaIndicadorFinanc;
    }

	public void setTpFrequenciaIndicadorFinanc(
			DomainValue tpFrequenciaIndicadorFinanc) {
        this.tpFrequenciaIndicadorFinanc = tpFrequenciaIndicadorFinanc;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Byte getDdCorte() {
        return this.ddCorte;
    }

    public void setDdCorte(Byte ddCorte) {
        this.ddCorte = ddCorte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.IndicadorFinanceiro.class)     
    public List getIndicadorFinanceiros() {
        return this.indicadorFinanceiros;
    }

    public void setIndicadorFinanceiros(List indicadorFinanceiros) {
        this.indicadorFinanceiros = indicadorFinanceiros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFrequencia",
				getIdFrequencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Frequencia))
			return false;
        Frequencia castOther = (Frequencia) other;
		return new EqualsBuilder().append(this.getIdFrequencia(),
				castOther.getIdFrequencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFrequencia()).toHashCode();
    }

}
