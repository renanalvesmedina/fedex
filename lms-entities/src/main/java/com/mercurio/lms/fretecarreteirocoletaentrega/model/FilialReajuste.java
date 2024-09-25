package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class FilialReajuste implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFilialReajuste;

    /** persistent field */
    private Boolean blAjusta;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe simulacaoReajusteFreteCe;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdFilialReajuste() {
        return this.idFilialReajuste;
    }

    public void setIdFilialReajuste(Long filialReajuste) {
        this.idFilialReajuste = filialReajuste;
    }

    public Boolean getBlAjusta() {
        return this.blAjusta;
    }

    public void setBlAjusta(Boolean blAjusta) {
        this.blAjusta = blAjusta;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe getSimulacaoReajusteFreteCe() {
        return this.simulacaoReajusteFreteCe;
    }

	public void setSimulacaoReajusteFreteCe(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe simulacaoReajusteFreteCe) {
        this.simulacaoReajusteFreteCe = simulacaoReajusteFreteCe;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("filialReajuste",
				getIdFilialReajuste()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialReajuste))
			return false;
        FilialReajuste castOther = (FilialReajuste) other;
		return new EqualsBuilder().append(this.getIdFilialReajuste(),
				castOther.getIdFilialReajuste()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialReajuste()).toHashCode();
    }

}
