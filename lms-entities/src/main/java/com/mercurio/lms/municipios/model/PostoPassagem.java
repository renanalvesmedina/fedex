package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class PostoPassagem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPostoPassagem;

    /** persistent field */
    private DomainValue tpPostoPassagem;
    
    /** persistent field */
    private Integer nrKm;

    /** persistent field */
    private DomainValue tpSentidoCobranca;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private BigDecimal nrLatitude;

    /** nullable persistent field */
    private BigDecimal nrLongitude;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Concessionaria concessionaria;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Rodovia rodovia;

    /** persistent field */
    private List postoPassagemCcs;

    /** persistent field */
    private List tarifaPostoPassagems;

    /** persistent field */
    private List tipoPagamentoPostos;

    /** persistent field */
    private List postoPassagemMunicipios;

    /** persistent field */
    private List postoPassagemRotaColEnts;

    public Long getIdPostoPassagem() {
        return this.idPostoPassagem;
    }

    public void setIdPostoPassagem(Long idPostoPassagem) {
        this.idPostoPassagem = idPostoPassagem;
    }

    public DomainValue getTpPostoPassagem() {
        return this.tpPostoPassagem;
    }

    public void setTpPostoPassagem(DomainValue tpPostoPassagem) {
        this.tpPostoPassagem = tpPostoPassagem;
    }

    public Integer getNrKm() {
        return this.nrKm;
    }

    public void setNrKm(Integer nrKm) {
        this.nrKm = nrKm;
    }

    public DomainValue getTpSentidoCobranca() {
        return this.tpSentidoCobranca;
    }

    public void setTpSentidoCobranca(DomainValue tpSentidoCobranca) {
        this.tpSentidoCobranca = tpSentidoCobranca;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public BigDecimal getNrLatitude() {
        return this.nrLatitude;
    }

    public void setNrLatitude(BigDecimal nrLatitude) {
        this.nrLatitude = nrLatitude;
    }

    public BigDecimal getNrLongitude() {
        return this.nrLongitude;
    }

    public void setNrLongitude(BigDecimal nrLongitude) {
        this.nrLongitude = nrLongitude;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.municipios.model.Concessionaria getConcessionaria() {
        return this.concessionaria;
    }

	public void setConcessionaria(
			com.mercurio.lms.municipios.model.Concessionaria concessionaria) {
        this.concessionaria = concessionaria;
    }

    public com.mercurio.lms.municipios.model.Rodovia getRodovia() {
        return this.rodovia;
    }

    public void setRodovia(com.mercurio.lms.municipios.model.Rodovia rodovia) {
        this.rodovia = rodovia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoPassagemCc.class)     
    public List getPostoPassagemCcs() {
        return this.postoPassagemCcs;
    }

    public void setPostoPassagemCcs(List postoPassagemCcs) {
        this.postoPassagemCcs = postoPassagemCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TarifaPostoPassagem.class)     
    public List getTarifaPostoPassagems() {
        return this.tarifaPostoPassagems;
    }

    public void setTarifaPostoPassagems(List tarifaPostoPassagems) {
        this.tarifaPostoPassagems = tarifaPostoPassagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TipoPagamentoPosto.class)     
    public List getTipoPagamentoPostos() {
        return this.tipoPagamentoPostos;
    }

    public void setTipoPagamentoPostos(List tipoPagamentoPostos) {
        this.tipoPagamentoPostos = tipoPagamentoPostos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PostoPassagemMunicipio.class)     
    public List getPostoPassagemMunicipios() {
        return this.postoPassagemMunicipios;
    }

    public void setPostoPassagemMunicipios(List postoPassagemMunicipios) {
        this.postoPassagemMunicipios = postoPassagemMunicipios;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PostoPassagemRotaColEnt.class)     
    public List getPostoPassagemRotaColEnts() {
        return this.postoPassagemRotaColEnts;
    }

    public void setPostoPassagemRotaColEnts(List postoPassagemRotaColEnts) {
        this.postoPassagemRotaColEnts = postoPassagemRotaColEnts;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPostoPassagem",
				getIdPostoPassagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PostoPassagem))
			return false;
        PostoPassagem castOther = (PostoPassagem) other;
		return new EqualsBuilder().append(this.getIdPostoPassagem(),
				castOther.getIdPostoPassagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPostoPassagem()).toHashCode();
    }

}
