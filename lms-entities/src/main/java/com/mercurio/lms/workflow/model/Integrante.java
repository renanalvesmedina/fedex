package com.mercurio.lms.workflow.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class Integrante implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIntegrante;

    /** persistent field */
    private Byte nrOrdemAprovacao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.workflow.model.Comite comite;

    /** persistent field */
    private Perfil perfil;

    /** persistent field */
    private List acoes;

    /** persistent field */
    private List substitutoFaltas;

    public Long getIdIntegrante() {
        return this.idIntegrante;
    }

    public void setIdIntegrante(Long idIntegrante) {
        this.idIntegrante = idIntegrante;
    }

    public Byte getNrOrdemAprovacao() {
        return this.nrOrdemAprovacao;
    }

    public void setNrOrdemAprovacao(Byte nrOrdemAprovacao) {
        this.nrOrdemAprovacao = nrOrdemAprovacao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.workflow.model.Comite getComite() {
        return this.comite;
    }

    public void setComite(com.mercurio.lms.workflow.model.Comite comite) {
        this.comite = comite;
    }
    
    public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

    @ParametrizedAttribute(type = com.mercurio.lms.workflow.model.Acao.class)     
    public List getAcoes() {
        return this.acoes;
    }

    public void setAcoes(List acoes) {
        this.acoes = acoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.workflow.model.SubstitutoFalta.class)     
    public List getSubstitutoFaltas() {
        return this.substitutoFaltas;
    }

    public void setSubstitutoFaltas(List substitutoFaltas) {
        this.substitutoFaltas = substitutoFaltas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idIntegrante",
				getIdIntegrante()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Integrante))
			return false;
        Integrante castOther = (Integrante) other;
		return new EqualsBuilder().append(this.getIdIntegrante(),
				castOther.getIdIntegrante()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIntegrante()).toHashCode();
    }

}
