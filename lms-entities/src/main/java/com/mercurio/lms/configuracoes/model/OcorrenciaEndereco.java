package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/**
 * Pojo da tabela OCORRENCIA_ENDERECO
 * 
 * @author Rafael Andrade de Oliveira
 * @since 13/04/2006
 * 
 */
public class OcorrenciaEndereco implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idOcorrenciaEndereco;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private YearMonthDay dtOcorrencia;

    /** persistent field */
    private DomainValue tpOcorrencia;

    public YearMonthDay getDtOcorrencia() {
		return dtOcorrencia;
	}

	public void setDtOcorrencia(YearMonthDay dtOcorrencia) {
		this.dtOcorrencia = dtOcorrencia;
	}

	public Long getIdOcorrenciaEndereco() {
		return idOcorrenciaEndereco;
	}

	public void setIdOcorrenciaEndereco(Long idOcorrenciaEndereco) {
		this.idOcorrenciaEndereco = idOcorrenciaEndereco;
	}

	public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DomainValue getTpOcorrencia() {
		return tpOcorrencia;
	}

	public void setTpOcorrencia(DomainValue tpOcorrencia) {
		this.tpOcorrencia = tpOcorrencia;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idEnderecoPessoa",
				getIdOcorrenciaEndereco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EnderecoPessoa))
			return false;
        EnderecoPessoa castOther = (EnderecoPessoa) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaEndereco(),
				castOther.getIdEnderecoPessoa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaEndereco())
            .toHashCode();
    }

}