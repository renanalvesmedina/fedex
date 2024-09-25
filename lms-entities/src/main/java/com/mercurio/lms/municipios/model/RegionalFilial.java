package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RegionalFilial implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegionalFilial;

    /** persistent field */
    private Boolean blSedeRegional;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Regional regional;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdRegionalFilial() {
        return this.idRegionalFilial;
    }

    public void setIdRegionalFilial(Long idRegionalFilial) {
        this.idRegionalFilial = idRegionalFilial;
    }

    public Boolean getBlSedeRegional() {
        return this.blSedeRegional;
    }

    public void setBlSedeRegional(Boolean blSedeRegional) {
        this.blSedeRegional = blSedeRegional;
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

    public com.mercurio.lms.municipios.model.Regional getRegional() {
        return this.regional;
    }

    public void setRegional(com.mercurio.lms.municipios.model.Regional regional) {
        this.regional = regional;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idRegionalFilial",
				getIdRegionalFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegionalFilial))
			return false;
        RegionalFilial castOther = (RegionalFilial) other;
		return new EqualsBuilder().append(this.getIdRegionalFilial(),
				castOther.getIdRegionalFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegionalFilial()).toHashCode();
    }

}
