package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class PontoParada implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPontoParada;

    /** persistent field */
    private Boolean blAduana;

    /** nullable persistent field */
    private Integer nrKm;

    /** nullable persistent field */
    private String sgAduana;

    /** nullable persistent field */
    private Integer cdAduana;
    
    /** persistent field */
    private String nmPontoParada;

    /** nullable persistent field */
    private Integer nrDistanciaAduana;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Rodovia rodovia;

    /** persistent field */
    private List aduanaCtoInts;

    /** persistent field */
    private List pontoParadaTrechos;

    /** persistent field */
    private List localTrocas;

    /** persistent field */
    private List ctoInternacionaisByIdAduanaOrigem;

    /** persistent field */
    private List ctoInternacionaisByIdAduanaDestino;

    public Long getIdPontoParada() {
        return this.idPontoParada;
    }

    public void setIdPontoParada(Long idPontoParada) {
        this.idPontoParada = idPontoParada;
    }

    public Boolean getBlAduana() {
        return this.blAduana;
    }

    public void setBlAduana(Boolean blAduana) {
        this.blAduana = blAduana;
    }

    public Integer getNrKm() {
        return this.nrKm;
    }

    public void setNrKm(Integer nrKm) {
        this.nrKm = nrKm;
    }

    public String getSgAduana() {
        return this.sgAduana;
    }

    public void setSgAduana(String sgAduana) {
        this.sgAduana = sgAduana;
    }

    public String getNmPontoParada() {
        return nmPontoParada;
    }

    public void setNmPontoParada(String nmPontoParada) {
        this.nmPontoParada = nmPontoParada;
    }
    
    public Integer getCdAduana() {
        return this.cdAduana;
    }

    public void setCdAduana(Integer cdAduana) {
        this.cdAduana = cdAduana;
    }

    public Integer getNrDistanciaAduana() {
        return this.nrDistanciaAduana;
    }

    public void setNrDistanciaAduana(Integer nrDistanciaAduana) {
        this.nrDistanciaAduana = nrDistanciaAduana;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.municipios.model.Rodovia getRodovia() {
        return this.rodovia;
    }

    public void setRodovia(com.mercurio.lms.municipios.model.Rodovia rodovia) {
        this.rodovia = rodovia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.AduanaCtoInt.class)     
    public List getAduanaCtoInts() {
        return this.aduanaCtoInts;
    }

    public void setAduanaCtoInts(List aduanaCtoInts) {
        this.aduanaCtoInts = aduanaCtoInts;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PontoParadaTrecho.class)     
    public List getPontoParadaTrechos() {
        return this.pontoParadaTrechos;
    }

    public void setPontoParadaTrechos(List pontoParadaTrechos) {
        this.pontoParadaTrechos = pontoParadaTrechos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.LocalTroca.class)     
    public List getLocalTrocas() {
        return this.localTrocas;
    }

    public void setLocalTrocas(List localTrocas) {
        this.localTrocas = localTrocas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoInternacional.class)     
    public List getCtoInternacionaisByIdAduanaOrigem() {
        return this.ctoInternacionaisByIdAduanaOrigem;
    }

	public void setCtoInternacionaisByIdAduanaOrigem(
			List ctoInternacionaisByIdAduanaOrigem) {
        this.ctoInternacionaisByIdAduanaOrigem = ctoInternacionaisByIdAduanaOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoInternacional.class)     
    public List getCtoInternacionaisByIdAduanaDestino() {
        return this.ctoInternacionaisByIdAduanaDestino;
    }

	public void setCtoInternacionaisByIdAduanaDestino(
			List ctoInternacionaisByIdAduanaDestino) {
        this.ctoInternacionaisByIdAduanaDestino = ctoInternacionaisByIdAduanaDestino;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPontoParada",
				getIdPontoParada()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PontoParada))
			return false;
        PontoParada castOther = (PontoParada) other;
		return new EqualsBuilder().append(this.getIdPontoParada(),
				castOther.getIdPontoParada()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPontoParada()).toHashCode();
    }

}
