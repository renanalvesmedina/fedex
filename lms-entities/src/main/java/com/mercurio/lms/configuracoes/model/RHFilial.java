package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class RHFilial implements Serializable {

	private static final long serialVersionUID = 1L;

    private Integer codFilial;

    private String nome;

    public Integer getCodFilial() {
		return codFilial;
	}

	public void setCodFilial(Integer codFilial) {
		this.codFilial = codFilial;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
    
    public String toString() {
		return new ToStringBuilder(this).append("codFilial", getCodFilial())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RHFilial))
			return false;
        RHFilial castOther = (RHFilial) other;
		return new EqualsBuilder().append(this.getCodFilial(),
				castOther.getCodFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getCodFilial()).toHashCode();
    }

}
