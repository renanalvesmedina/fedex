package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class PrestadorServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPrestadorServico;
    
    private Boolean blTermoComp;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    private DomainValue tpSituacao;

    public Long getIdPrestadorServico() {
        return this.idPrestadorServico;
    }

    public void setIdPrestadorServico(Long idPrestadorServico) {
        this.idPrestadorServico = idPrestadorServico;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idPrestadorServico",
				getIdPrestadorServico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PrestadorServico))
			return false;
        PrestadorServico castOther = (PrestadorServico) other;
		return new EqualsBuilder().append(this.getIdPrestadorServico(),
				castOther.getIdPrestadorServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPrestadorServico())
            .toHashCode();
    }

	public Boolean getBlTermoComp() {
		return blTermoComp;
	}

	public void setBlTermoComp(Boolean blTermoComp) {
		this.blTermoComp = blTermoComp;
	}
}