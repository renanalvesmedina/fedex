package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Feriado implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */ 
    private Long idFeriado;

    /** persistent field */
    private YearMonthDay dtFeriado;

    /** persistent field */
    private DomainValue tpFeriado;

    /** persistent field */
    private Boolean blFacultativo;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private String dsFeriado;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    public Long getIdFeriado() {
        return this.idFeriado;
    }

    public void setIdFeriado(Long idFeriado) {
        this.idFeriado = idFeriado;
    }

    public YearMonthDay getDtFeriado() {
        return this.dtFeriado;
    }

    public void setDtFeriado(YearMonthDay dtFeriado) {
        this.dtFeriado = dtFeriado;
    }

    public DomainValue getTpFeriado() {
        return this.tpFeriado;
    }
    
	public Feriado() {
	}

    public Feriado(Long idFeriado,YearMonthDay dtFeriado) {
    	this.idFeriado = idFeriado;
    	this.dtFeriado = dtFeriado;
    }

    public void setTpFeriado(DomainValue tpFeriado) {
        this.tpFeriado = tpFeriado;
    }

    public Boolean getBlFacultativo() {
        return this.blFacultativo;
    }

    public void setBlFacultativo(Boolean blFacultativo) {
        this.blFacultativo = blFacultativo;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public String getDsFeriado() {
        return this.dsFeriado;
    }

    public void setDsFeriado(String dsFeriado) {
        this.dsFeriado = dsFeriado;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFeriado", getIdFeriado())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Feriado))
			return false;
        Feriado castOther = (Feriado) other;
		return new EqualsBuilder().append(this.getIdFeriado(),
				castOther.getIdFeriado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFeriado()).toHashCode();
    }

}
