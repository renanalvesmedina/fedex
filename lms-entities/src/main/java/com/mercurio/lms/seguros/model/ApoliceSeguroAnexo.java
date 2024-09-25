package com.mercurio.lms.seguros.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

public class ApoliceSeguroAnexo implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	/** identifier field */
    private Long idApoliceSeguroAnexo;
    
    /** persistent field */
    private com.mercurio.lms.seguros.model.ApoliceSeguro apoliceSeguro;
    
    private com.mercurio.lms.configuracoes.model.Usuario usuario;
    
    /** persistent field */
    private DateTime dhCriacao;
    
    /** persistent field */
    private String dsAnexo;
    
    /** persistent field */
    private byte[] dcArquivo;

	public Long getIdApoliceSeguroAnexo() {
		return idApoliceSeguroAnexo;
	}

	public void setIdApoliceSeguroAnexo(Long idApoliceSeguroAnexo) {
		this.idApoliceSeguroAnexo = idApoliceSeguroAnexo;
	}

	public com.mercurio.lms.seguros.model.ApoliceSeguro getApoliceSeguro() {
		return apoliceSeguro;
	}

	public void setApoliceSeguro(
			com.mercurio.lms.seguros.model.ApoliceSeguro apoliceSeguro) {
		this.apoliceSeguro = apoliceSeguro;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idApoliceSeguroAnexo",
				getIdApoliceSeguroAnexo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ApoliceSeguroAnexo))
			return false;
        ApoliceSeguroAnexo castOther = (ApoliceSeguroAnexo) other;
		return new EqualsBuilder().append(this.getIdApoliceSeguroAnexo(),
				castOther.getIdApoliceSeguroAnexo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdApoliceSeguroAnexo()).toHashCode();
    }
    
}
