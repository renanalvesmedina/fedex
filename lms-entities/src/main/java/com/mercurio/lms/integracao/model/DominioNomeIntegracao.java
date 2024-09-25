package com.mercurio.lms.integracao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

public class DominioNomeIntegracao implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idDominioNomeIntegracao;

    /** persistent field */
    private String nmDominio;
    
    /** persistent field */
    private String dsDominio;
    
    /** persistent field */
    private DomainValue tpSituacao;

	public Long getIdDominioNomeIntegracao() {
		return idDominioNomeIntegracao;
	}

	public void setIdDominioNomeIntegracao(Long idDominioNomeIntegracao) {
		this.idDominioNomeIntegracao = idDominioNomeIntegracao;
	}

	public String getNmDominio() {
		return nmDominio;
	}

	public void setNmDominio(String nmDominio) {
		this.nmDominio = nmDominio;
	}
	
	public String getDsDominio() {
		return dsDominio;
	}

	public void setDsDominio(String dsDominio) {
		this.dsDominio = dsDominio;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idDominioNomeIntegracao",
				getIdDominioNomeIntegracao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DominioNomeIntegracao))
			return false;
        DominioNomeIntegracao castOther = (DominioNomeIntegracao) other;
		return new EqualsBuilder().append(this.getIdDominioNomeIntegracao(),
				castOther.getIdDominioNomeIntegracao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDominioNomeIntegracao())
            .toHashCode();
    }

}
