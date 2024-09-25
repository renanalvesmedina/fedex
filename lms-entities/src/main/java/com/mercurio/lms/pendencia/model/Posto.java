package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class Posto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPosto;

    /** persistent field */
    private String descricao;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;
    
    /** persistent field */
	private List enderecoArmazems;

    public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getIdPosto() {
		return idPosto;
	}

	public void setIdPosto(Long idPosto) {
		this.idPosto = idPosto;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.EnderecoArmazem.class)     
    public List getEnderecoArmazems() {
        return this.enderecoArmazems;
    }

    public void setEnderecoArmazems(List enderecoArmazems) {
        this.enderecoArmazems = enderecoArmazems;
    }   	
	
	public String toString() {
		return new ToStringBuilder(this).append("idPosto", getIdPosto())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Posto))
			return false;
        Posto castOther = (Posto) other;
		return new EqualsBuilder().append(this.getIdPosto(),
				castOther.getIdPosto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPosto()).toHashCode();
    }

}
