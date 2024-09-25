package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

public class ClasseRisco implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long idClasseRisco;
	
	private Long nrClasseRisco;
	
	private String dsClasseRisco;
	
	private DomainValue tpSituacao;

    /** persistent field */
    private List produtos;
    
    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Produto.class)     
    public List getProdutos() {
        return this.produtos;
    }

    public void setProdutos(List produtos) {
        this.produtos = produtos;
    }
    
	public Long getIdClasseRisco() {
		return idClasseRisco;
	}

	public void setIdClasseRisco(Long idClasseRisco) {
		this.idClasseRisco = idClasseRisco;
	}

	public Long getNrClasseRisco() {
		return nrClasseRisco;
	}

	public void setNrClasseRisco(Long nrClasseRisco) {
		this.nrClasseRisco = nrClasseRisco;
	}

	public String getDsClasseRisco() {
		return dsClasseRisco;
	}

	public void setDsClasseRisco(String dsClasseRisco) {
		this.dsClasseRisco = dsClasseRisco;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idClasseRisco",
				getIdClasseRisco()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ClasseRisco))
			return false;
		ClasseRisco castOther = (ClasseRisco) other;
		return new EqualsBuilder().append(this.getIdClasseRisco(),
				castOther.getIdClasseRisco()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdClasseRisco()).toHashCode();
	}	
}
