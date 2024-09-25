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
public class CausaNaoConformidade implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCausaNaoConformidade;

    /** persistent field */
    private VarcharI18n dsCausaNaoConformidade;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List causaAcaoCorretivas;

    /** persistent field */
    private List ocorrenciaNaoConformidades;

    public Long getIdCausaNaoConformidade() {
        return this.idCausaNaoConformidade;
    }

    public void setIdCausaNaoConformidade(Long idCausaNaoConformidade) {
        this.idCausaNaoConformidade = idCausaNaoConformidade;
    }

    public VarcharI18n getDsCausaNaoConformidade() {
		return dsCausaNaoConformidade;
    }

	public void setDsCausaNaoConformidade(VarcharI18n dsCausaNaoConformidade) {
        this.dsCausaNaoConformidade = dsCausaNaoConformidade;
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

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade.class)     
    public List getOcorrenciaNaoConformidades() {
        return this.ocorrenciaNaoConformidades;
    }

    public void setOcorrenciaNaoConformidades(List ocorrenciaNaoConformidades) {
        this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCausaNaoConformidade",
				getIdCausaNaoConformidade()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CausaNaoConformidade))
			return false;
        CausaNaoConformidade castOther = (CausaNaoConformidade) other;
		return new EqualsBuilder().append(this.getIdCausaNaoConformidade(),
				castOther.getIdCausaNaoConformidade()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCausaNaoConformidade())
            .toHashCode();
    }

}
