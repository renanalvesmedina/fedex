package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class MunicipioFilialUFOrigem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMunicipioFilialUFOrigem;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial;

    public Long getIdMunicipioFilialUFOrigem() {
        return this.idMunicipioFilialUFOrigem;
    }

    public void setIdMunicipioFilialUFOrigem(Long idMunicipioFilialUFOrigem) {
        this.idMunicipioFilialUFOrigem = idMunicipioFilialUFOrigem;
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

    public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
        return this.unidadeFederativa;
    }

	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilial() {
        return this.municipioFilial;
    }

	public void setMunicipioFilial(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial) {
        this.municipioFilial = municipioFilial;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idMunicipioFilialUFOrigem",
				getIdMunicipioFilialUFOrigem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioFilialUFOrigem))
			return false;
        MunicipioFilialUFOrigem castOther = (MunicipioFilialUFOrigem) other;
		return new EqualsBuilder().append(this.getIdMunicipioFilialUFOrigem(),
				castOther.getIdMunicipioFilialUFOrigem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioFilialUFOrigem())
            .toHashCode();
    }
 
}
