package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author Hibernate CodeGenerator */
public class ObservacaoCiaAerea implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idObservacaoCiaAerea;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private VarcharI18n  dsObservacaoCiaAerea;

    /** persistent field */
    private List ciaFilialMercurios;

    public Long getIdObservacaoCiaAerea() {
        return this.idObservacaoCiaAerea;
    }

    public void setIdObservacaoCiaAerea(Long idObservacaoCiaAerea) {
        this.idObservacaoCiaAerea = idObservacaoCiaAerea;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public VarcharI18n getDsObservacaoCiaAerea() {
		return dsObservacaoCiaAerea;
    }

	public void setDsObservacaoCiaAerea(VarcharI18n dsObservacaoCiaAerea) {
        this.dsObservacaoCiaAerea = dsObservacaoCiaAerea;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.CiaFilialMercurio.class)     
    public List getCiaFilialMercurios() {
        return this.ciaFilialMercurios;
    }

    public void setCiaFilialMercurios(List ciaFilialMercurios) {
        this.ciaFilialMercurios = ciaFilialMercurios;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idObservacaoCiaAerea",
				getIdObservacaoCiaAerea()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ObservacaoCiaAerea))
			return false;
        ObservacaoCiaAerea castOther = (ObservacaoCiaAerea) other;
		return new EqualsBuilder().append(this.getIdObservacaoCiaAerea(),
				castOther.getIdObservacaoCiaAerea()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdObservacaoCiaAerea())
            .toHashCode();
    }

}
