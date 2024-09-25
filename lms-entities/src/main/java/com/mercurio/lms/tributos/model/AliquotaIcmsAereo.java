package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class AliquotaIcmsAereo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAliquotaIcmsAereo;

    /** persistent field */
    private YearMonthDay dtInicioVigencia;

    /** persistent field */
    private BigDecimal pcAliquotaInterna;
    
    /** persistent field */
    private BigDecimal pcAliquotaDestNC;

    /** persistent field */
    private BigDecimal pcEmbuteInterno;
    
    /** persistent field */
    private BigDecimal pcEmbuteDestNC;

    /** persistent field */
    private BigDecimal pcAliquotaInterestadual;

    /** persistent field */
    private BigDecimal pcEmbuteInterestadual;

    /** persistent field */
    private String obInterno;

    /** persistent field */
    private String obInterestadual;
    
    /** persistent field */
    private String obDestNC;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;

    public Long getIdAliquotaIcmsAereo() {
        return this.idAliquotaIcmsAereo;
    }

    public void setIdAliquotaIcmsAereo(Long idAliquotaIcmsAereo) {
        this.idAliquotaIcmsAereo = idAliquotaIcmsAereo;
    }

    public YearMonthDay getDtInicioVigencia() {
        return this.dtInicioVigencia;
    }

    public void setDtInicioVigencia(YearMonthDay dtInicioVigencia) {
        this.dtInicioVigencia = dtInicioVigencia;
    }

    public BigDecimal getPcAliquotaInterna() {
        return this.pcAliquotaInterna;
    }

    public void setPcAliquotaInterna(BigDecimal pcAliquotaInterna) {
        this.pcAliquotaInterna = pcAliquotaInterna;
    }

    public BigDecimal getPcEmbuteInterno() {
        return this.pcEmbuteInterno;
    }

    public void setPcEmbuteInterno(BigDecimal pcEmbuteInterno) {
        this.pcEmbuteInterno = pcEmbuteInterno;
    }

    public BigDecimal getPcAliquotaInterestadual() {
        return this.pcAliquotaInterestadual;
    }

    public void setPcAliquotaInterestadual(BigDecimal pcAliquotaInterestadual) {
        this.pcAliquotaInterestadual = pcAliquotaInterestadual;
    }

    public BigDecimal getPcEmbuteInterestadual() {
        return this.pcEmbuteInterestadual;
    }

    public void setPcEmbuteInterestadual(BigDecimal pcEmbuteInterestadual) {
        this.pcEmbuteInterestadual = pcEmbuteInterestadual;
    }

    public String getObInterno() {
        return this.obInterno;
    }

    public void setObInterno(String obInterno) {
        this.obInterno = obInterno;
    }

    public String getObInterestadual() {
        return this.obInterestadual;
    }

    public void setObInterestadual(String obInterestadual) {
        this.obInterestadual = obInterestadual;
    }

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAliquotaIcmsAereo",
				getIdAliquotaIcmsAereo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaIcmsAereo))
			return false;
        AliquotaIcmsAereo castOther = (AliquotaIcmsAereo) other;
		return new EqualsBuilder().append(this.getIdAliquotaIcmsAereo(),
				castOther.getIdAliquotaIcmsAereo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaIcmsAereo())
            .toHashCode();
    }

    public String getObDestNC() {
        return obDestNC;
    }

    public void setObDestNC(String obDestNC) {
        this.obDestNC = obDestNC;
    }

    public BigDecimal getPcAliquotaDestNC() {
        return pcAliquotaDestNC;
    }

    public void setPcAliquotaDestNC(BigDecimal pcAliquotaDestNC) {
        this.pcAliquotaDestNC = pcAliquotaDestNC;
    }

    public BigDecimal getPcEmbuteDestNC() {
        return pcEmbuteDestNC;
    }

    public void setPcEmbuteDestNC(BigDecimal pcEmbuteDestNC) {
        this.pcEmbuteDestNC = pcEmbuteDestNC;
    }

}
