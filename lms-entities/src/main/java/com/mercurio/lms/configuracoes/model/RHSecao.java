package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class RHSecao implements Serializable {

	private static final long serialVersionUID = 1L;

    private String codigo;

    private Integer codColigada;

    private String descricao;
    
    private String nroCenCustoCont;
    
    public Integer getCodColigada() {
        return codColigada;
    }

    public void setCodColigada(Integer codColigada) {
        this.codColigada = codColigada;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNroCenCustoCont() {
        return nroCenCustoCont;
    }

    public void setNroCenCustoCont(String nroCenCustoCont) {
        this.nroCenCustoCont = nroCenCustoCont;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("codigo", getCodigo())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RHSecao))
			return false;
        RHSecao castOther = (RHSecao) other;
		return new EqualsBuilder().append(this.getCodigo(),
				castOther.getCodigo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getCodigo()).toHashCode();
    }

}
