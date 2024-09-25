package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;


/** @author LMS Custom Hibernate CodeGenerator */
public class ApoliceSeguro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idApoliceSeguro;

    /** persistent field */
    private String nrApolice;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private BigDecimal vlLimiteApolice;

    /** persistent field */
    private com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro;

    /** persistent field */
    private com.mercurio.lms.seguros.model.ReguladoraSeguro reguladoraSeguro;

    /** persistent field */
    private com.mercurio.lms.seguros.model.Seguradora seguradora;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa segurado;
    
    /** persistent field */
    private String dsCobertura;
    
    /** persistent field */
    private String dsLimite;
    
    /** persistent field */
    private String dsFranquia;
    
    /** persistent field */
    private BigDecimal vlFranquia;

	// LMS-7285
	private BigDecimal vlLimiteControleCarga;

    public Long getIdApoliceSeguro() {
        return this.idApoliceSeguro;
    }

    public void setIdApoliceSeguro(Long idApoliceSeguro) {
        this.idApoliceSeguro = idApoliceSeguro;
    }

    public String getNrApolice() {
        return this.nrApolice;
    }

    public void setNrApolice(String nrApolice) {
        this.nrApolice = nrApolice;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public BigDecimal getVlLimiteApolice() {
        return this.vlLimiteApolice;
    }

    public void setVlLimiteApolice(BigDecimal vlLimiteApolice) {
        this.vlLimiteApolice = vlLimiteApolice;
    }

    public com.mercurio.lms.seguros.model.TipoSeguro getTipoSeguro() {
        return this.tipoSeguro;
    }

	public void setTipoSeguro(
			com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public com.mercurio.lms.seguros.model.ReguladoraSeguro getReguladoraSeguro() {
        return this.reguladoraSeguro;
    }

	public void setReguladoraSeguro(
			com.mercurio.lms.seguros.model.ReguladoraSeguro reguladoraSeguro) {
        this.reguladoraSeguro = reguladoraSeguro;
    }

    public com.mercurio.lms.seguros.model.Seguradora getSeguradora() {
        return this.seguradora;
    }

	public void setSeguradora(
			com.mercurio.lms.seguros.model.Seguradora seguradora) {
        this.seguradora = seguradora;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getSegurado() {
		return segurado;
	}

	public void setSegurado(com.mercurio.lms.configuracoes.model.Pessoa segurado) {
		this.segurado = segurado;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idApoliceSeguro",
				getIdApoliceSeguro()).toString();
    }

    public String getDsCobertura() {
		return dsCobertura;
	}

	public void setDsCobertura(String dsCobertura) {
		this.dsCobertura = dsCobertura;
	}

	public String getDsLimite() {
		return dsLimite;
	}

	public void setDsLimite(String dsLimite) {
		this.dsLimite = dsLimite;
	}

	public String getDsFranquia() {
		return dsFranquia;
	}

	public void setDsFranquia(String dsFranquia) {
		this.dsFranquia = dsFranquia;
	}

	public BigDecimal getVlFranquia() {
		return vlFranquia;
	}

	public void setVlFranquia(BigDecimal vlFranquia) {
		this.vlFranquia = vlFranquia;
	}

	public BigDecimal getVlLimiteControleCarga() {
		return vlLimiteControleCarga;
	}

	public void setVlLimiteControleCarga(BigDecimal vlLimiteControleCarga) {
		this.vlLimiteControleCarga = vlLimiteControleCarga;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ApoliceSeguro))
			return false;
        ApoliceSeguro castOther = (ApoliceSeguro) other;
		return new EqualsBuilder().append(this.getIdApoliceSeguro(),
				castOther.getIdApoliceSeguro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdApoliceSeguro()).toHashCode();
    }

}
