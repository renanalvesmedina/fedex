package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class Alinea implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAlinea;

    /** persistent field */
    private Byte cdAlinea;

    /** persistent field */
    private VarcharI18n dsAlinea;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List cheques;

    public Long getIdAlinea() {
        return this.idAlinea;
    }

    public void setIdAlinea(Long idAlinea) {
        this.idAlinea = idAlinea;
    }

    public Byte getCdAlinea() {
        return this.cdAlinea;
    }

    public void setCdAlinea(Byte cdAlinea) {
        this.cdAlinea = cdAlinea;
    }

    public VarcharI18n getDsAlinea() {
		return dsAlinea;
    }

	public void setDsAlinea(VarcharI18n dsAlinea) {
        this.dsAlinea = dsAlinea;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Cheque.class)     
    public List getCheques() {
        return this.cheques;
    }

    public void setCheques(List cheques) {
        this.cheques = cheques;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAlinea", getIdAlinea())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Alinea))
			return false;
        Alinea castOther = (Alinea) other;
		return new EqualsBuilder().append(this.getIdAlinea(),
				castOther.getIdAlinea()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAlinea()).toHashCode();
    }

}
