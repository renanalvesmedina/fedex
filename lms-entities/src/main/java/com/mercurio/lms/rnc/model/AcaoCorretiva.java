package com.mercurio.lms.rnc.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class AcaoCorretiva implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAcaoCorretiva;

    /** persistent field */
    private VarcharI18n dsAcaoCorretiva;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List causaAcaoCorretivas;

    public Long getIdAcaoCorretiva() {
        return this.idAcaoCorretiva;
    }

    public void setIdAcaoCorretiva(Long idAcaoCorretiva) {
        this.idAcaoCorretiva = idAcaoCorretiva;
    }

    public VarcharI18n getDsAcaoCorretiva() {
		return dsAcaoCorretiva;
    }

	public void setDsAcaoCorretiva(VarcharI18n dsAcaoCorretiva) {
        this.dsAcaoCorretiva = dsAcaoCorretiva;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.CausaAcaoCorretiva.class)     
    public List getCausaAcaoCorretivas() {
        return this.causaAcaoCorretivas;
    }

    public void setCausaAcaoCorretivas(List causaAcaoCorretivas) {
        this.causaAcaoCorretivas = causaAcaoCorretivas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAcaoCorretiva",
				getIdAcaoCorretiva()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AcaoCorretiva))
			return false;
        AcaoCorretiva castOther = (AcaoCorretiva) other;
		return new EqualsBuilder().append(this.getIdAcaoCorretiva(),
				castOther.getIdAcaoCorretiva()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAcaoCorretiva()).toHashCode();
    }

}
