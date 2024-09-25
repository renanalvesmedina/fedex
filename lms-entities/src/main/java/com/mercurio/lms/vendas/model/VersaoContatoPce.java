package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class VersaoContatoPce implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idVersaoContatoPce;

    /** persistent field */
    private DomainValue tpFormaComunicacao;

    /** persistent field */
    private String dsRegiaoAtuacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Contato contato;

    /** persistent field */
    private com.mercurio.lms.vendas.model.VersaoDescritivoPce versaoDescritivoPce;
    
    private Usuario usuario;

    public Long getIdVersaoContatoPce() {
        return this.idVersaoContatoPce;
    }

    public void setIdVersaoContatoPce(Long idVersaoContatoPce) {
        this.idVersaoContatoPce = idVersaoContatoPce;
    }

    public DomainValue getTpFormaComunicacao() {
        return this.tpFormaComunicacao;
    }

    public void setTpFormaComunicacao(DomainValue tpFormaComunicacao) {
        this.tpFormaComunicacao = tpFormaComunicacao;
    }

    public String getDsRegiaoAtuacao() {
        return this.dsRegiaoAtuacao;
    }

    public void setDsRegiaoAtuacao(String dsRegiaoAtuacao) {
        this.dsRegiaoAtuacao = dsRegiaoAtuacao;
    }

    public com.mercurio.lms.configuracoes.model.Contato getContato() {
        return this.contato;
    }

    public void setContato(com.mercurio.lms.configuracoes.model.Contato contato) {
        this.contato = contato;
    }

    public com.mercurio.lms.vendas.model.VersaoDescritivoPce getVersaoDescritivoPce() {
        return this.versaoDescritivoPce;
    }

	public void setVersaoDescritivoPce(
			com.mercurio.lms.vendas.model.VersaoDescritivoPce versaoDescritivoPce) {
        this.versaoDescritivoPce = versaoDescritivoPce;
    }
   
    public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idVersaoContatoPce",
				getIdVersaoContatoPce()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VersaoContatoPce))
			return false;
        VersaoContatoPce castOther = (VersaoContatoPce) other;
		return new EqualsBuilder().append(this.getIdVersaoContatoPce(),
				castOther.getIdVersaoContatoPce()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdVersaoContatoPce())
            .toHashCode();
    }

}
