package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Pais;

/** @author LMS Custom Hibernate CodeGenerator */
public class PotencialComercialCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPotencialComercialCliente;

    /** nullable persistent field */
    private DomainValue tpFrete;

    /** nullable persistent field */
    private DomainValue tpModal;

    /** nullable persistent field */
    private DomainValue tpAbrangencia;

    /** nullable persistent field */
    private BigDecimal pcDetencao;

    /** nullable persistent field */
    private Integer nrNotasFiscais;

    /** nullable persistent field */
    private Integer nrVolumes;

    /** nullable persistent field */
    private BigDecimal psTotal;

    /** nullable persistent field */
    private BigDecimal vlFaturamentoPotencial;

    /** nullable persistent field */
    private BigDecimal vlTotalMercadoria;

    /** nullable persistent field */
    private String dsTransportadora;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUfOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUfDestino;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
    
    /** persistent field */
    private Pais paisOrigem;
    
    /** persistent field */
    private Pais paisDestino;

    public Long getIdPotencialComercialCliente() {
        return this.idPotencialComercialCliente;
    }

    public void setIdPotencialComercialCliente(Long idPotencialComercialCliente) {
        this.idPotencialComercialCliente = idPotencialComercialCliente;
    }

    public DomainValue getTpFrete() {
        return this.tpFrete;
    }

    public void setTpFrete(DomainValue tpFrete) {
        this.tpFrete = tpFrete;
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

    public BigDecimal getPcDetencao() {
        return this.pcDetencao;
    }

    public void setPcDetencao(BigDecimal pcDetencao) {
        this.pcDetencao = pcDetencao;
    }

    public Integer getNrNotasFiscais() {
        return this.nrNotasFiscais;
    }

    public void setNrNotasFiscais(Integer nrNotasFiscais) {
        this.nrNotasFiscais = nrNotasFiscais;
    }

    public Integer getNrVolumes() {
        return this.nrVolumes;
    }

    public void setNrVolumes(Integer nrVolumes) {
        this.nrVolumes = nrVolumes;
    }

    public BigDecimal getPsTotal() {
        return this.psTotal;
    }

    public void setPsTotal(BigDecimal psTotal) {
        this.psTotal = psTotal;
    }

    public BigDecimal getVlFaturamentoPotencial() {
        return this.vlFaturamentoPotencial;
    }

    public void setVlFaturamentoPotencial(BigDecimal vlFaturamentoPotencial) {
        this.vlFaturamentoPotencial = vlFaturamentoPotencial;
    }

    public BigDecimal getVlTotalMercadoria() {
        return this.vlTotalMercadoria;
    }

    public void setVlTotalMercadoria(BigDecimal vlTotalMercadoria) {
        this.vlTotalMercadoria = vlTotalMercadoria;
    }

    public String getDsTransportadora() {
        return this.dsTransportadora;
    }

    public void setDsTransportadora(String dsTransportadora) {
        this.dsTransportadora = dsTransportadora;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaByIdUfOrigem() {
        return this.unidadeFederativaByIdUfOrigem;
    }

	public void setUnidadeFederativaByIdUfOrigem(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUfOrigem) {
        this.unidadeFederativaByIdUfOrigem = unidadeFederativaByIdUfOrigem;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativaByIdUfDestino() {
        return this.unidadeFederativaByIdUfDestino;
    }

	public void setUnidadeFederativaByIdUfDestino(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUfDestino) {
        this.unidadeFederativaByIdUfDestino = unidadeFederativaByIdUfDestino;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem() {
        return this.tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem;
    }

	public void setTipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem(
			com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem) {
        this.tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem = tipoLocalizacaoMunicipioByOdTipoLocalizacaoOrigem;
    }

    public com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() {
        return this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
    }

	public void setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(
			com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino) {
        this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino = tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
    }
    
    /**
	 * Retorna o valor do potencial de faturamento formatado junto com o símbolo
	 * da moeda correspondente Formato : R$ 111.111.111.111,00
	 * 
     * @return String do potencial faturamento formatada
     */
    public String getVlFaturamentoPotencialAndSgMoeda(){
    	
    	String retorno = null;
    	
    	try{
    		
			DecimalFormat df = new DecimalFormat("###,###,###,###,##0.00",
					new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
    		
	    	if( getMoeda() != null && getMoeda().getDsSimbolo() != null ){
				retorno = getMoeda().getDsSimbolo() + " "
						+ df.format(getVlFaturamentoPotencial());
	    	} else {
	    		retorno = df.format(getVlFaturamentoPotencial()).toString();    		
	    	}
	    	
    	} catch (Exception e) {
    		return null;
		}
    	
    	return retorno;
    		
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPotencialComercialCliente",
				getIdPotencialComercialCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PotencialComercialCliente))
			return false;
        PotencialComercialCliente castOther = (PotencialComercialCliente) other;
		return new EqualsBuilder().append(
				this.getIdPotencialComercialCliente(),
				castOther.getIdPotencialComercialCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPotencialComercialCliente())
            .toHashCode();
    }

    /**
     * @return Returns the paisDestino.
     */
    public Pais getPaisDestino() {
        return paisDestino;
    }

    /**
	 * @param paisDestino
	 *            The paisDestino to set.
     */
    public void setPaisDestino(Pais paisDestino) {
        this.paisDestino = paisDestino;
    }

    /**
     * @return Returns the paisOrigem.
     */
    public Pais getPaisOrigem() {
        return paisOrigem;
    }

    /**
	 * @param paisOrigem
	 *            The paisOrigem to set.
     */
    public void setPaisOrigem(Pais paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

}
