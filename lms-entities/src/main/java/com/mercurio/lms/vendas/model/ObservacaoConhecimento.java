package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ObservacaoConhecimento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idObservacaoConhecimento;

    /** persistent field */
    private String dsObservacaoConhecimento;

    /** persistent field */
    private DomainValue tpSituacao;

    public String getDsObservacaoConhecimento() {
		return dsObservacaoConhecimento;
	}

	public void setDsObservacaoConhecimento(String dsObservacaoConhecimento) {
		this.dsObservacaoConhecimento = dsObservacaoConhecimento;
	}

	public Long getIdObservacaoConhecimento() {
		return idObservacaoConhecimento;
	}

	public void setIdObservacaoConhecimento(Long idObservacaoConhecimento) {
		this.idObservacaoConhecimento = idObservacaoConhecimento;
	}

	public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idObservacaoConhecimento",
				getIdObservacaoConhecimento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ObservacaoConhecimento))
			return false;
        ObservacaoConhecimento castOther = (ObservacaoConhecimento) other;
		return new EqualsBuilder().append(this.getIdObservacaoConhecimento(),
				castOther.getIdObservacaoConhecimento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdObservacaoConhecimento())
            .toHashCode();
    }

}
