package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author Hibernate CodeGenerator */
public class Concessionaria implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idConcessionaria;

    /** nullable persistent field */
    private String dsHomePage;

    /** persistent field */
    private DomainValue tpSituacao;
    
    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

	/**
	 * persistent field private
	 * com.mercurio.lms.configuracoes.model.InscricaoEstadual inscricaoEstadual;
    */
    
    /** persistent field */
    private List postoPassagems;

    public Long getIdConcessionaria() {
        return this.idConcessionaria;
    }

    public void setIdConcessionaria(Long idConcessionaria) {
        this.idConcessionaria = idConcessionaria;
    }

    public String getDsHomePage() {
        return this.dsHomePage;
    }

    public void setDsHomePage(String dsHomepage) {
        this.dsHomePage = dsHomepage;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }


    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PostoPassagem.class)     
    public List getPostoPassagems() {
        return this.postoPassagems;
    }

    public void setPostoPassagems(List postoPassagems) {
        this.postoPassagems = postoPassagems;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idConcessionaria",
				getIdConcessionaria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Concessionaria))
			return false;
        Concessionaria castOther = (Concessionaria) other;
		return new EqualsBuilder().append(this.getIdConcessionaria(),
				castOther.getIdConcessionaria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdConcessionaria()).toHashCode();
    }

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

}
