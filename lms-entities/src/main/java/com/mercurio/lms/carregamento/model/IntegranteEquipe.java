package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class IntegranteEquipe implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIntegranteEquipe;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.Equipe equipe;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.CargoOperacional cargoOperacional;

    /** persistent field */
    private DomainValue tpIntegrante;

    private Integer versao;

    public Long getIdIntegranteEquipe() {
        return this.idIntegranteEquipe;
    }

    public void setIdIntegranteEquipe(Long idIntegranteEquipe) {
        this.idIntegranteEquipe = idIntegranteEquipe;
    }

    public com.mercurio.lms.carregamento.model.Equipe getEquipe() {
        return this.equipe;
    }

    public void setEquipe(com.mercurio.lms.carregamento.model.Equipe equipe) {
        this.equipe = equipe;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public DomainValue getTpIntegrante() {
        return tpIntegrante;
    }
    
    public void setTpIntegrante(DomainValue tpIntegrante) {
        this.tpIntegrante = tpIntegrante;
    }

    public com.mercurio.lms.carregamento.model.CargoOperacional getCargoOperacional() {
        return cargoOperacional;
    }

	public void setCargoOperacional(
			com.mercurio.lms.carregamento.model.CargoOperacional cargoOperacional) {
        this.cargoOperacional = cargoOperacional;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idIntegranteEquipe",
				getIdIntegranteEquipe()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof IntegranteEquipe))
			return false;
        IntegranteEquipe castOther = (IntegranteEquipe) other;
		return new EqualsBuilder().append(this.getIdIntegranteEquipe(),
				castOther.getIdIntegranteEquipe()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIntegranteEquipe())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
