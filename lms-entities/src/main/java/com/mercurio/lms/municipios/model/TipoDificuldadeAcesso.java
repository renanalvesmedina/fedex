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
public class TipoDificuldadeAcesso implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoDificuldadeAcesso;

    /** persistent field */
    private VarcharI18n dsTipoDificuldadeAcesso;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List rotaIntervaloCeps;

    public Long getIdTipoDificuldadeAcesso() {
        return this.idTipoDificuldadeAcesso;
    }

    public void setIdTipoDificuldadeAcesso(Long idTipoDificuldadeAcesso) {
        this.idTipoDificuldadeAcesso = idTipoDificuldadeAcesso;
    }

    public VarcharI18n getDsTipoDificuldadeAcesso() {
		return dsTipoDificuldadeAcesso;
    }

	public void setDsTipoDificuldadeAcesso(VarcharI18n dsTipoDificuldadeAcesso) {
        this.dsTipoDificuldadeAcesso = dsTipoDificuldadeAcesso;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaIntervaloCep.class)     
    public List getRotaIntervaloCeps() {
        return this.rotaIntervaloCeps;
    }

    public void setRotaIntervaloCeps(List rotaIntervaloCeps) {
        this.rotaIntervaloCeps = rotaIntervaloCeps;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoDificuldadeAcesso",
				getIdTipoDificuldadeAcesso()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoDificuldadeAcesso))
			return false;
        TipoDificuldadeAcesso castOther = (TipoDificuldadeAcesso) other;
		return new EqualsBuilder().append(this.getIdTipoDificuldadeAcesso(),
				castOther.getIdTipoDificuldadeAcesso()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoDificuldadeAcesso())
            .toHashCode();
    }
}
