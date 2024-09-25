package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParametroSimulacaoRota implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParametroSimulacaoRota;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private BigDecimal pcReajuste;

    /** nullable persistent field */
    private String vlReajuste;

    /** persistent field */
    private com.mercurio.lms.fretecarreteiroviagem.model.SimulacaoReajusteRota simulacaoReajusteRota;

    /** persistent field */
    private List criterioAplicSimulacoes;

    public Long getIdParametroSimulacaoRota() {
        return this.idParametroSimulacaoRota;
    }

    public void setIdParametroSimulacaoRota(Long idParametroSimulacaoRota) {
        this.idParametroSimulacaoRota = idParametroSimulacaoRota;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public BigDecimal getPcReajuste() {
        return this.pcReajuste;
    }

    public void setPcReajuste(BigDecimal pcReajuste) {
        this.pcReajuste = pcReajuste;
    }

    public String getVlReajuste() {
        return this.vlReajuste;
    }

    public void setVlReajuste(String vlReajuste) {
        this.vlReajuste = vlReajuste;
    }

    public com.mercurio.lms.fretecarreteiroviagem.model.SimulacaoReajusteRota getSimulacaoReajusteRota() {
        return this.simulacaoReajusteRota;
    }

	public void setSimulacaoReajusteRota(
			com.mercurio.lms.fretecarreteiroviagem.model.SimulacaoReajusteRota simulacaoReajusteRota) {
        this.simulacaoReajusteRota = simulacaoReajusteRota;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.CriterioAplicSimulacao.class)     
    public List getCriterioAplicSimulacoes() {
        return this.criterioAplicSimulacoes;
    }

    public void setCriterioAplicSimulacoes(List criterioAplicSimulacoes) {
        this.criterioAplicSimulacoes = criterioAplicSimulacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idParametroSimulacaoRota",
				getIdParametroSimulacaoRota()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroSimulacaoRota))
			return false;
        ParametroSimulacaoRota castOther = (ParametroSimulacaoRota) other;
		return new EqualsBuilder().append(this.getIdParametroSimulacaoRota(),
				castOther.getIdParametroSimulacaoRota()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroSimulacaoRota())
            .toHashCode();
    }

}
