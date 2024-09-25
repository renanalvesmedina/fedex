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
public class ParametroFilial implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParametroFilial;

    /** persistent field */
    private Short nrTamanho;

    /** persistent field */
    private String nmParametroFilial;

    /** persistent field */
    private VarcharI18n dsParametroFilial;

    /** persistent field */
    private DomainValue tpFormato;

    /** persistent field */
    private List conteudoParametroFiliais;

    public Long getIdParametroFilial() {
        return this.idParametroFilial;
    }

    public void setIdParametroFilial(Long idParametroFilial) {
        this.idParametroFilial = idParametroFilial;
    }

    public Short getNrTamanho() {
        return this.nrTamanho;
    }

    public void setNrTamanho(Short nrTamanho) {
        this.nrTamanho = nrTamanho;
    }

    public String getNmParametroFilial() {
        return this.nmParametroFilial;
    }

    public void setNmParametroFilial(String nmParametroFilial) {
        this.nmParametroFilial = nmParametroFilial;
    }

    public VarcharI18n getDsParametroFilial() {
		return dsParametroFilial;
    }

	public void setDsParametroFilial(VarcharI18n dsParametroFilial) {
        this.dsParametroFilial = dsParametroFilial;
    }

    public DomainValue getTpFormato() {
        return this.tpFormato;
    }

    public void setTpFormato(DomainValue tpFormato) {
        this.tpFormato = tpFormato;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ConteudoParametroFilial.class)     
    public List getConteudoParametroFiliais() {
        return this.conteudoParametroFiliais;
    }

    public void setConteudoParametroFiliais(List conteudoParametroFiliais) {
        this.conteudoParametroFiliais = conteudoParametroFiliais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idParametroFilial",
				getIdParametroFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParametroFilial))
			return false;
        ParametroFilial castOther = (ParametroFilial) other;
		return new EqualsBuilder().append(this.getIdParametroFilial(),
				castOther.getIdParametroFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParametroFilial())
            .toHashCode();
    }

}
