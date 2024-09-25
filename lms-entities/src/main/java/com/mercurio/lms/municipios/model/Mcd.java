package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class Mcd implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMcd;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private List mcdMunicipioFiliais;

    public Long getIdMcd() {
        return this.idMcd;
    }

    public void setIdMcd(Long idMcd) {
        this.idMcd = idMcd;
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

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.McdMunicipioFilial.class)     
    public List getMcdMunicipioFiliais() {
        return this.mcdMunicipioFiliais;
    }

    public void setMcdMunicipioFiliais(List mcdMunicipioFiliais) {
        this.mcdMunicipioFiliais = mcdMunicipioFiliais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMcd", getIdMcd()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Mcd))
			return false;
        Mcd castOther = (Mcd) other;
        return new EqualsBuilder()
				.append(this.getIdMcd(), castOther.getIdMcd()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMcd()).toHashCode();
    }

}
