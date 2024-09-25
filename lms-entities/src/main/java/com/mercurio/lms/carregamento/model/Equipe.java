package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Equipe implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEquipe;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private String dsEquipe;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Setor setor;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List integranteEquipes;

    /** persistent field */
    private List equipeOperacoes;

    private Integer versao;

    public Long getIdEquipe() {
        return this.idEquipe;
    }

    public void setIdEquipe(Long idEquipe) {
        this.idEquipe = idEquipe;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getDsEquipe() {
        return this.dsEquipe;
    }

    public void setDsEquipe(String dsEquipe) {
        this.dsEquipe = dsEquipe;
    }

    public com.mercurio.lms.configuracoes.model.Setor getSetor() {
        return this.setor;
    }

    public void setSetor(com.mercurio.lms.configuracoes.model.Setor setor) {
        this.setor = setor;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.IntegranteEquipe.class)     
    public List getIntegranteEquipes() {
        return this.integranteEquipes;
    }

    public void setIntegranteEquipes(List integranteEquipes) {
        this.integranteEquipes = integranteEquipes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.EquipeOperacao.class)     
    public List getEquipeOperacoes() {
        return this.equipeOperacoes;
    }

    public void setEquipeOperacoes(List equipeOperacoes) {
        this.equipeOperacoes = equipeOperacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEquipe", getIdEquipe())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Equipe))
			return false;
        Equipe castOther = (Equipe) other;
		return new EqualsBuilder().append(this.getIdEquipe(),
				castOther.getIdEquipe()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEquipe()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
