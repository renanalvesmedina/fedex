package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class IntegranteEqOperac implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idIntegranteEqOperac;
    
    /** identifier field */
    private Integer versao;
    
    /** 
	 * Este atributo foi gerado decorrente de existir uma dependencia na DF2 de
	 * trabalhar na grid filho com apenas objetos do tipo do filho em sua list
     */
    private String nmIntegranteEquipe;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.EquipeOperacao equipeOperacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.CargoOperacional cargoOperacional;    
    
    /** identifier field */
    private DomainValue tpIntegrante;

    public Long getIdIntegranteEqOperac() {
        return this.idIntegranteEqOperac;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}
	
    public String getNmIntegranteEquipe() {
		return nmIntegranteEquipe;
	}

	public void setNmIntegranteEquipe(String nmIntegranteEquipe) {
		this.nmIntegranteEquipe = nmIntegranteEquipe;
	}

	public void setIdIntegranteEqOperac(Long idIntegranteEqOperac) {
        this.idIntegranteEqOperac = idIntegranteEqOperac;
    }

    public com.mercurio.lms.carregamento.model.EquipeOperacao getEquipeOperacao() {
        return this.equipeOperacao;
    }

	public void setEquipeOperacao(
			com.mercurio.lms.carregamento.model.EquipeOperacao equipeOperacao) {
        this.equipeOperacao = equipeOperacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * @return Returns the cargoOperacional.
     */
    public com.mercurio.lms.carregamento.model.CargoOperacional getCargoOperacional() {
        return cargoOperacional;
    }

    /**
	 * @param cargoOperacional
	 *            The cargoOperacional to set.
     */
	public void setCargoOperacional(
			com.mercurio.lms.carregamento.model.CargoOperacional cargoOperacional) {
        this.cargoOperacional = cargoOperacional;
    }

    /**
     * @return Returns the empresa.
     */
    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return empresa;
    }

    /**
	 * @param empresa
	 *            The empresa to set.
     */
    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }
    
    public DomainValue getTpIntegrante() {
        return tpIntegrante;
    }
    
    public void setTpIntegrante(DomainValue tpIntegrante) {
        this.tpIntegrante = tpIntegrante;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idIntegranteEqOperac",
				getIdIntegranteEqOperac()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof IntegranteEqOperac))
			return false;
        IntegranteEqOperac castOther = (IntegranteEqOperac) other;
		return new EqualsBuilder().append(this.getIdIntegranteEqOperac(),
				castOther.getIdIntegranteEqOperac()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdIntegranteEqOperac())
            .toHashCode();
    }

}
