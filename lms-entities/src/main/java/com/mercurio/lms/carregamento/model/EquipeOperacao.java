package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class EquipeOperacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEquipeOperacao;

    /** identifier field */
    private Integer versao;
    
    /** persistent field */
    private DateTime dhInicioOperacao;

    /** nullable persistent field */
    private DateTime dhFimOperacao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.Equipe equipe;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga;

    /** persistent field */
    private List carregamentoPreManifestos;

    /** persistent field */
    private List descargaManifestos;

    /** persistent field */
    private List integranteEqOperacs;

    /** persistent field */
    private List eventoControleCargas;
    
    public Long getIdEquipeOperacao() {
        return this.idEquipeOperacao;
    }

    public void setIdEquipeOperacao(Long idEquipeOperacao) {
        this.idEquipeOperacao = idEquipeOperacao;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}
	
    public DateTime getDhInicioOperacao() {
        return this.dhInicioOperacao;
    }

    public void setDhInicioOperacao(DateTime dhInicioOperacao) {
        this.dhInicioOperacao = dhInicioOperacao;
    }

    public DateTime getDhFimOperacao() {
        return this.dhFimOperacao;
    }

    public void setDhFimOperacao(DateTime dhFimOperacao) {
        this.dhFimOperacao = dhFimOperacao;
    }

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.carregamento.model.Equipe getEquipe() {
        return this.equipe;
    }

    public void setEquipe(com.mercurio.lms.carregamento.model.Equipe equipe) {
        this.equipe = equipe;
    }

    public com.mercurio.lms.carregamento.model.CarregamentoDescarga getCarregamentoDescarga() {
        return this.carregamentoDescarga;
    }

	public void setCarregamentoDescarga(
			com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga) {
        this.carregamentoDescarga = carregamentoDescarga;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.CarregamentoPreManifesto.class)     
    public List getCarregamentoPreManifestos() {
        return this.carregamentoPreManifestos;
    }

    public void setCarregamentoPreManifestos(List carregamentoPreManifestos) {
        this.carregamentoPreManifestos = carregamentoPreManifestos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DescargaManifesto.class)     
    public List getDescargaManifestos() {
        return this.descargaManifestos;
    }

    public void setDescargaManifestos(List descargaManifestos) {
        this.descargaManifestos = descargaManifestos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.IntegranteEqOperac.class)     
    public List getIntegranteEqOperacs() {
        return this.integranteEqOperacs;
    }

    public void setIntegranteEqOperacs(List integranteEqOperacs) {
        this.integranteEqOperacs = integranteEqOperacs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEquipeOperacao",
				getIdEquipeOperacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EquipeOperacao))
			return false;
        EquipeOperacao castOther = (EquipeOperacao) other;
		return new EqualsBuilder().append(this.getIdEquipeOperacao(),
				castOther.getIdEquipeOperacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEquipeOperacao()).toHashCode();
    }

    public List getEventoControleCargas() {
        return eventoControleCargas;
    }

    public void setEventoControleCargas(List eventoControleCargas) {
        this.eventoControleCargas = eventoControleCargas;
    }

}
