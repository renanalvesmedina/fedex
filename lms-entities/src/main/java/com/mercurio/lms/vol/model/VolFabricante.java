package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolFabricante implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFabricante;

    /** nullable persistent field */
    private Pessoa pessoa;

    /** persistent field */
    private Contato contato;

    /** persistent field */
    private List volModeloseqps;

    private DomainValue tpSituacao;
    
    public Long getIdFabricante() {
        return this.idFabricante;
    }

    public void setIdFabricante(Long idFabricante) {
        this.idFabricante = idFabricante;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Contato getContato() {
        return this.contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vol.model.VolModeloseqps.class)     
    public List getVolModeloseqps() {
        return this.volModeloseqps;
    }

    public void setVolModeloseqps(List volModeloseqps) {
        this.volModeloseqps = volModeloseqps;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFabricante",
				getIdFabricante()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolFabricante))
			return false;
        VolFabricante castOther = (VolFabricante) other;
		return new EqualsBuilder().append(this.getIdFabricante(),
				castOther.getIdFabricante()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFabricante()).toHashCode();
    }

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

}
