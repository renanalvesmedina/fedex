package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class RHCargo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private String codigo;
    
    private String idCodigo;

    /** persistent field */
    private String nome;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
        this.idCodigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String toString() {
		return new ToStringBuilder(this).append("codigo", getCodigo())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RHCargo))
			return false;
        RHCargo castOther = (RHCargo) other;
		return new EqualsBuilder().append(this.getCodigo(),
				castOther.getCodigo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getCodigo()).toHashCode();
    }

	public String getIdCodigo() {
		return idCodigo;
	}

}