package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class CiaFilialMercurio implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCiaFilialMercurio;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private Integer nrUltimoAwb;

    /** nullable persistent field */
    private Integer dvUltimoAwb;

    /** nullable persistent field */
    private Integer nrSerieAwb;

    /** nullable persistent field */
    private Long nrPrestacaoContas;

    /** nullable persistent field */
    private String dsIdentificadorCiaAerea;

    /** nullable persistent field */
    private Long vlIdentificadorCiaAerea;

    /** nullable persistent field */
    private DomainValue tpUso;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.ObservacaoCiaAerea observacaoCiaAerea;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List filialMercurioFilialCias;

    /** persistent field */
    private List awbs;

    public Long getIdCiaFilialMercurio() {
        return this.idCiaFilialMercurio;
    }

    public void setIdCiaFilialMercurio(Long idCiaFilialMercurio) {
        this.idCiaFilialMercurio = idCiaFilialMercurio;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public Integer getNrUltimoAwb() {
        return this.nrUltimoAwb;
    }

    public Integer getDvUltimoAwb() {
    	return this.dvUltimoAwb;
    }

    public void setNrUltimoAwb(Integer nrUltimoAwb) {
        this.nrUltimoAwb = nrUltimoAwb;
    }

    public void setDvUltimoAwb(Integer dvUltimoAwb) {
    	this.dvUltimoAwb = dvUltimoAwb;
    }

    public Integer getNrSerieAwb() {
        return this.nrSerieAwb;
    }

    public void setNrSerieAwb(Integer nrSerieAwb) {
        this.nrSerieAwb = nrSerieAwb;
    }

    public Long getNrPrestacaoContas() {
        return this.nrPrestacaoContas;
    }

    public void setNrPrestacaoContas(Long nrPrestacaoContas) {
        this.nrPrestacaoContas = nrPrestacaoContas;
    }

    public String getDsIdentificadorCiaAerea() {
        return this.dsIdentificadorCiaAerea;
    }

    public void setDsIdentificadorCiaAerea(String dsIdentificadorCiaAerea) {
        this.dsIdentificadorCiaAerea = dsIdentificadorCiaAerea;
    }

    public Long getVlIdentificadorCiaAerea() {
        return this.vlIdentificadorCiaAerea;
    }

    public void setVlIdentificadorCiaAerea(Long vlIdentificadorCiaAerea) {
        this.vlIdentificadorCiaAerea = vlIdentificadorCiaAerea;
    }

    public DomainValue getTpUso() {
        return this.tpUso;
    }

    public void setTpUso(DomainValue tpUso) {
        this.tpUso = tpUso;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.ObservacaoCiaAerea getObservacaoCiaAerea() {
        return this.observacaoCiaAerea;
    }

	public void setObservacaoCiaAerea(
			com.mercurio.lms.municipios.model.ObservacaoCiaAerea observacaoCiaAerea) {
        this.observacaoCiaAerea = observacaoCiaAerea;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.FilialMercurioFilialCia.class)     
    public List getFilialMercurioFilialCias() {
        return this.filialMercurioFilialCias;
    }

    public void setFilialMercurioFilialCias(List filialMercurioFilialCias) {
        this.filialMercurioFilialCias = filialMercurioFilialCias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Awb.class)     
    public List getAwbs() {
        return this.awbs;
    }

    public void setAwbs(List awbs) {
        this.awbs = awbs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCiaFilialMercurio",
				getIdCiaFilialMercurio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CiaFilialMercurio))
			return false;
        CiaFilialMercurio castOther = (CiaFilialMercurio) other;
		return new EqualsBuilder().append(this.getIdCiaFilialMercurio(),
				castOther.getIdCiaFilialMercurio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCiaFilialMercurio())
            .toHashCode();
    }

}
