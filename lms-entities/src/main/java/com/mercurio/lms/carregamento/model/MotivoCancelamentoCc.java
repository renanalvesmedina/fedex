package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoCancelamentoCc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoCancelamentoCc;

    /** persistent field */
    private VarcharI18n dsMotivoCancelamentoCc;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List controleCargas;

    public Long getIdMotivoCancelamentoCc() {
        return this.idMotivoCancelamentoCc;
    }

    public void setIdMotivoCancelamentoCc(Long idMotivoCancelamentoCc) {
        this.idMotivoCancelamentoCc = idMotivoCancelamentoCc;
    }

    public VarcharI18n getDsMotivoCancelamentoCc() {
		return dsMotivoCancelamentoCc;
    }

	public void setDsMotivoCancelamentoCc(VarcharI18n dsMotivoCancelamentoCc) {
        this.dsMotivoCancelamentoCc = dsMotivoCancelamentoCc;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return this.controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoCancelamentoCc",
				getIdMotivoCancelamentoCc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoCancelamentoCc))
			return false;
        MotivoCancelamentoCc castOther = (MotivoCancelamentoCc) other;
		return new EqualsBuilder().append(this.getIdMotivoCancelamentoCc(),
				castOther.getIdMotivoCancelamentoCc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoCancelamentoCc())
            .toHashCode();
    }

}
