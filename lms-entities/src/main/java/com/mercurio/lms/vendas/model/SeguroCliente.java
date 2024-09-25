package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class SeguroCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSeguroCliente;

    /** persistent field */
    private DomainValue tpModal;

    /** persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** persistent field */
    private BigDecimal percTaxa;

    /** persistent field */
    private BigDecimal vlLimite;

    /** nullable persistent field */
    private String dsApolice;

    /** nullable persistent field */
    private byte[] dcCartaIsencao;

    /** persistent field */
    private com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** nullable persistent field */
    private com.mercurio.lms.seguros.model.ReguladoraSeguro reguladoraSeguro;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;
    
    /** persistent field */
    private Boolean blFimVigenciaInformado;
    
    /** nullable persistent field */
    private DateTime dhEnvioAviso;
    
    //LMS-6148
    /** persistent field */
    private Boolean blEmEmissao;
    
    /** nullable persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioOrigem;
    
    /** nullable persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioDestino;
    
    /** nullable persistent field */
    private String nmCartaIsencao;
    
    /** nullable persistent field */
    private com.mercurio.lms.seguros.model.Seguradora seguradora;
    
    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioAviso;
    
    /** nullable persistent field */
    private String dsCobertura;
    
    /** nullable persistent field */
    private String dsMercadoria;
    
    /** nullable persistent field */
    private String dsEmbalagem;

	// LMS-7285
	private BigDecimal vlLimiteControleCarga;

    public Long getIdSeguroCliente() {
        return this.idSeguroCliente;
    }

    public void setIdSeguroCliente(Long idSeguroCliente) {
        this.idSeguroCliente = idSeguroCliente;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
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

    public BigDecimal getPercTaxa() {
		return percTaxa;
	}

	public void setPercTaxa(BigDecimal percTaxa) {
		this.percTaxa = percTaxa;
	}

	public BigDecimal getVlLimite() {
        return this.vlLimite;
    }

    public void setVlLimite(BigDecimal vlLimite) {
        this.vlLimite = vlLimite;
    }

    public String getDsApolice() {
        return this.dsApolice;
    }

    public void setDsApolice(String dsApolice) {
        this.dsApolice = dsApolice;
    }

    public byte[] getDcCartaIsencao() {
        return this.dcCartaIsencao;
    }

    public void setDcCartaIsencao(byte[] dcCartaIsencao) {
        this.dcCartaIsencao = dcCartaIsencao;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public com.mercurio.lms.seguros.model.TipoSeguro getTipoSeguro() {
		return tipoSeguro;
	}

	public void setTipoSeguro(com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro) {
		this.tipoSeguro = tipoSeguro;
	}

	public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.seguros.model.ReguladoraSeguro getReguladoraSeguro() {
        return this.reguladoraSeguro;
    }

	public void setReguladoraSeguro(
			com.mercurio.lms.seguros.model.ReguladoraSeguro reguladoraSeguro) {
        this.reguladoraSeguro = reguladoraSeguro;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }
    
	public Boolean getBlFimVigenciaInformado() {
		return blFimVigenciaInformado;
	}

	public void setBlFimVigenciaInformado(Boolean blFimVigenciaInformado) {
		this.blFimVigenciaInformado = blFimVigenciaInformado;
	}

    public DateTime getDhEnvioAviso() {
		return dhEnvioAviso;
	}

	public void setDhEnvioAviso(DateTime dhEnvioAviso) {
		this.dhEnvioAviso = dhEnvioAviso;
	}

	//LMS-6148
    public Boolean getBlEmEmissao() {
		return blEmEmissao;
	}

	public void setBlEmEmissao(Boolean blEmEmissao) {
		this.blEmEmissao = blEmEmissao;
	}

	public com.mercurio.lms.municipios.model.Municipio getMunicipioOrigem() {
		return municipioOrigem;
	}

	public void setMunicipioOrigem(com.mercurio.lms.municipios.model.Municipio municipioOrigem) {
		this.municipioOrigem = municipioOrigem;
	}

	public com.mercurio.lms.municipios.model.Municipio getMunicipioDestino() {
		return municipioDestino;
	}

	public void setMunicipioDestino(
			com.mercurio.lms.municipios.model.Municipio municipioDestino) {
		this.municipioDestino = municipioDestino;
	}

	public String getNmCartaIsencao() {
		return nmCartaIsencao;
	}

	public void setNmCartaIsencao(String nmCartaIsencao) {
		this.nmCartaIsencao = nmCartaIsencao;
	}

	public com.mercurio.lms.seguros.model.Seguradora getSeguradora() {
		return seguradora;
	}

	public void setSeguradora(com.mercurio.lms.seguros.model.Seguradora seguradora) {
		this.seguradora = seguradora;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioAviso() {
		return usuarioAviso;
	}

	public void setUsuarioAviso(com.mercurio.lms.configuracoes.model.Usuario usuarioAviso) {
		this.usuarioAviso = usuarioAviso;
	}

	public String getDsCobertura() {
		return dsCobertura;
	}

	public void setDsCobertura(String dsCobertura) {
		this.dsCobertura = dsCobertura;
	}

	public String getDsMercadoria() {
		return dsMercadoria;
	}

	public void setDsMercadoria(String dsMercadoria) {
		this.dsMercadoria = dsMercadoria;
	}

	public String getDsEmbalagem() {
		return dsEmbalagem;
	}

	public void setDsEmbalagem(String dsEmbalagem) {
		this.dsEmbalagem = dsEmbalagem;
	}

	public BigDecimal getVlLimiteControleCarga() {
		return vlLimiteControleCarga;
	}

	public void setVlLimiteControleCarga(BigDecimal vlLimiteControleCarga) {
		this.vlLimiteControleCarga = vlLimiteControleCarga;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idSeguroCliente",
				getIdSeguroCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SeguroCliente))
			return false;
        SeguroCliente castOther = (SeguroCliente) other;
		return new EqualsBuilder().append(this.getIdSeguroCliente(),
				castOther.getIdSeguroCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSeguroCliente()).toHashCode();
    }
}