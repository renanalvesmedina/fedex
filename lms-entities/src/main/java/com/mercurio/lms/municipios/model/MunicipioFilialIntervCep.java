package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class MunicipioFilialIntervCep implements Serializable, Vigencia{

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMunicipioFilialIntervCep;

    /** persistent field */
    private String nrCepInicial;

    /** persistent field */
    private String nrCepFinal;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial;

    public Long getIdMunicipioFilialIntervCep() {
        return this.idMunicipioFilialIntervCep;
    }

    public void setIdMunicipioFilialIntervCep(Long idMunicipioFilialIntervCep) {
        this.idMunicipioFilialIntervCep = idMunicipioFilialIntervCep;
    }

    public String getNrCepInicial() {
        return this.nrCepInicial;
    }

    public void setNrCepInicial(String nrCepInicial) {
        this.nrCepInicial = nrCepInicial;
    }

    public String getNrCepFinal() {
        return this.nrCepFinal;
    }

    public void setNrCepFinal(String nrCepFinal) {
        this.nrCepFinal = nrCepFinal;
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

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilial() {
        return this.municipioFilial;
    }

	public void setMunicipioFilial(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial) {
        this.municipioFilial = municipioFilial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMunicipioFilialIntervCep",
				getIdMunicipioFilialIntervCep()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioFilialIntervCep))
			return false;
        MunicipioFilialIntervCep castOther = (MunicipioFilialIntervCep) other;
		return new EqualsBuilder().append(this.getIdMunicipioFilialIntervCep(),
				castOther.getIdMunicipioFilialIntervCep()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioFilialIntervCep())
            .toHashCode();
    }

}
