package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class PrazoEntregaCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPrazoEntregaCliente;

    /** persistent field */
    private Long nrPrazo;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUfOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativaByIdUfDestino;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Aeroporto aeroportoByIdAeroportoDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Aeroporto aeroportoByIdAeroportoOrigem;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais paisByIdPaisDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais paisByIdPaisOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */    
    private com.mercurio.lms.municipios.model.Zona zonaByIdZonaDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Zona zonaByIdZonaOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;    

    /** persistent field */
    private DomainValue tpResponsavelFrete;
    
    public Long getIdPrazoEntregaCliente() {
        return this.idPrazoEntregaCliente;
    }

    public void setIdPrazoEntregaCliente(Long idPrazoEntregaCliente) {
        this.idPrazoEntregaCliente = idPrazoEntregaCliente;
    }

    public Long getNrPrazo() {
        return this.nrPrazo;
    }

    public void setNrPrazo(Long nrPrazo) {
        this.nrPrazo = nrPrazo;
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

    public com.mercurio.lms.municipios.model.Municipio getMunicipioByIdMunicipioOrigem() {
        return this.municipioByIdMunicipioOrigem;
    }

	public void setMunicipioByIdMunicipioOrigem(
			com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioOrigem) {
        this.municipioByIdMunicipioOrigem = municipioByIdMunicipioOrigem;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipioByIdMunicipioDestino() {
        return this.municipioByIdMunicipioDestino;
    }

	public void setMunicipioByIdMunicipioDestino(
			com.mercurio.lms.municipios.model.Municipio municipioByIdMunicipioDestino) {
        this.municipioByIdMunicipioDestino = municipioByIdMunicipioDestino;
    }

    public com.mercurio.lms.municipios.model.Aeroporto getAeroportoByIdAeroportoDestino() {
        return this.aeroportoByIdAeroportoDestino;
    }

	public void setAeroportoByIdAeroportoDestino(
			com.mercurio.lms.municipios.model.Aeroporto aeroportoByIdAeroportoDestino) {
        this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
    }

    public com.mercurio.lms.municipios.model.Aeroporto getAeroportoByIdAeroportoOrigem() {
        return this.aeroportoByIdAeroportoOrigem;
    }

	public void setAeroportoByIdAeroportoOrigem(
			com.mercurio.lms.municipios.model.Aeroporto aeroportoByIdAeroportoOrigem) {
        this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.municipios.model.Pais getPaisByIdPaisDestino() {
        return this.paisByIdPaisDestino;
    }

	public void setPaisByIdPaisDestino(
			com.mercurio.lms.municipios.model.Pais paisByIdPaisDestino) {
        this.paisByIdPaisDestino = paisByIdPaisDestino;
    }

    public com.mercurio.lms.municipios.model.Pais getPaisByIdPaisOrigem() {
        return this.paisByIdPaisOrigem;
    }

	public void setPaisByIdPaisOrigem(
			com.mercurio.lms.municipios.model.Pais paisByIdPaisOrigem) {
        this.paisByIdPaisOrigem = paisByIdPaisOrigem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestino() {
        return this.filialByIdFilialDestino;
    }

	public void setFilialByIdFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino) {
        this.filialByIdFilialDestino = filialByIdFilialDestino;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
        return this.filialByIdFilialOrigem;
    }

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
    }
    
    public com.mercurio.lms.municipios.model.Zona getZonaByIdZonaDestino() {
        return this.zonaByIdZonaDestino;
    }

	public void setZonaByIdZonaDestino(
			com.mercurio.lms.municipios.model.Zona zonaByIdZonaDestino) {
        this.zonaByIdZonaDestino = zonaByIdZonaDestino;
    }

    public com.mercurio.lms.municipios.model.Zona getZonaByIdZonaOrigem() {
        return this.zonaByIdZonaOrigem;
    }

	public void setZonaByIdZonaOrigem(
			com.mercurio.lms.municipios.model.Zona zonaByIdZonaOrigem) {
        this.zonaByIdZonaOrigem = zonaByIdZonaOrigem;
    }

    public com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() {
        return this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
    }

	public void setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(
			com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino) {
        this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino = tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino;
    }

    public com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() {
        return this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;
    }

	public void setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(
			com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem) {
        this.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem = tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem;
    }    

	public DomainValue getTpResponsavelFrete() {
		return tpResponsavelFrete;
	}
	
	public void setTpResponsavelFrete(DomainValue tpResponsavelFrete) {
		this.tpResponsavelFrete = tpResponsavelFrete;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idPrazoEntregaCliente",
				getIdPrazoEntregaCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PrazoEntregaCliente))
			return false;
        PrazoEntregaCliente castOther = (PrazoEntregaCliente) other;
		return new EqualsBuilder().append(this.getIdPrazoEntregaCliente(),
				castOther.getIdPrazoEntregaCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPrazoEntregaCliente())
            .toHashCode();
    }

}
