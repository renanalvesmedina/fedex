package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class RHSituacaoFuncionario implements Serializable {

	private static final long serialVersionUID = 1L;

    private String codInterno;

    private String descricao;
    
    public String getCodInterno() {
        return codInterno;
    }

    public void setCodInterno(String codInterno) {
        this.codInterno = codInterno;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("codInterno", getCodInterno())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RHSituacaoFuncionario))
			return false;
        RHSituacaoFuncionario castOther = (RHSituacaoFuncionario) other;
		return new EqualsBuilder().append(this.getCodInterno(),
				castOther.getCodInterno()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getCodInterno()).toHashCode();
    }

}
