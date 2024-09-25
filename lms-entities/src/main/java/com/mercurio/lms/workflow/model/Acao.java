package com.mercurio.lms.workflow.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class Acao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAcao;
    
    /** persistent field */
    private Byte nrOrdemAprovacao;    

    /** persistent field */
    private DateTime dhInclusao;

    /** persistent field */
    private DomainValue tpSituacaoAcao;

    /** persistent field */
    private Boolean blLiberada;

    /** persistent field */
    private String obAcao;

    /** nullable persistent field */
    private DateTime dhLiberacao;

    /** nullable persistent field */
    private DateTime dhAcao;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private Pendencia pendencia;

    /** persistent field */
    private Integrante integrante;

    /** persistent field */
    private List substitutoFaltaAcoes;

    public Long getIdAcao() {
        return this.idAcao;
    }

    public String getAprovador(){
    	Integrante integrante = getIntegrante();
    	if (integrante != null && Hibernate.isInitialized(integrante)){
    		Perfil perfil = integrante.getPerfil();
    		Usuario usuario = integrante.getUsuario();
    		if (perfil != null && Hibernate.isInitialized(perfil)){
    			return perfil.getDsPerfil();
    		}else if (usuario != null && Hibernate.isInitialized(usuario)){
    			return usuario.getNmUsuario();
    		}
    	}
    	return null;
    }
    
    public void setIdAcao(Long idAcao) {
        this.idAcao = idAcao;
    }

    public DateTime getDhInclusao() {
        return this.dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public DomainValue getTpSituacaoAcao() {
        return this.tpSituacaoAcao;
    }

    public void setTpSituacaoAcao(DomainValue tpSituacaoAcao) {
        this.tpSituacaoAcao = tpSituacaoAcao;
    }

    public Boolean getBlLiberada() {
        return this.blLiberada;
    }

    public void setBlLiberada(Boolean blLiberada) {
        this.blLiberada = blLiberada;
    }

    public String getObAcao() {
        return this.obAcao;
    }

    public void setObAcao(String obAcao) {
        this.obAcao = obAcao;
    }

    public DateTime getDhLiberacao() {
        return this.dhLiberacao;
    }

    public void setDhLiberacao(DateTime dhLiberacao) {
        this.dhLiberacao = dhLiberacao;
    }

    public DateTime getDhAcao() {
        return this.dhAcao;
    }

    public void setDhAcao(DateTime dhAcao) {
        this.dhAcao = dhAcao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pendencia getPendencia() {
        return this.pendencia;
    }

    public void setPendencia(Pendencia pendencia) {
        this.pendencia = pendencia;
    }

    public Integrante getIntegrante() {
        return this.integrante;
    }

    public void setIntegrante(Integrante integrante) {
        this.integrante = integrante;
    }

    @ParametrizedAttribute(type = SubstitutoFaltaAcao.class)     
    public List getSubstitutoFaltaAcoes() {
        return this.substitutoFaltaAcoes;
    }

    public void setSubstitutoFaltaAcoes(List substitutoFaltaAcoes) {
        this.substitutoFaltaAcoes = substitutoFaltaAcoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAcao", getIdAcao())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Acao))
			return false;
        Acao castOther = (Acao) other;
		return new EqualsBuilder().append(this.getIdAcao(),
				castOther.getIdAcao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAcao()).toHashCode();
    }

	public Byte getNrOrdemAprovacao() {
		return nrOrdemAprovacao;
	}

	public void setNrOrdemAprovacao(Byte nrOrdemAprovacao) {
		this.nrOrdemAprovacao = nrOrdemAprovacao;
	}

}
