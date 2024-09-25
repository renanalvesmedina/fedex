package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoCancelamento implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idMotivoCancelamento;

    /** persistent field */
    private VarcharI18n dsMotivoCancelamento;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List conhecimentos;

    public Long getIdMotivoCancelamento() {
        return this.idMotivoCancelamento;
    }

    public void setIdMotivoCancelamento(Long idMotivoCancelamento) {
        this.idMotivoCancelamento = idMotivoCancelamento;
    }

    public VarcharI18n getDsMotivoCancelamento() {
		return dsMotivoCancelamento;
    }

	public void setDsMotivoCancelamento(VarcharI18n dsMotivoCancelamento) {
        this.dsMotivoCancelamento = dsMotivoCancelamento;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Conhecimento.class)     
    public List getConhecimentos() {
        return this.conhecimentos;
    }

    public void setConhecimentos(List conhecimentos) {
        this.conhecimentos = conhecimentos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoCancelamento",
				getIdMotivoCancelamento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoCancelamento))
			return false;
        MotivoCancelamento castOther = (MotivoCancelamento) other;
		return new EqualsBuilder().append(this.getIdMotivoCancelamento(),
				castOther.getIdMotivoCancelamento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoCancelamento())
            .toHashCode();
    }

}
