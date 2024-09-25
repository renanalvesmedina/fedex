package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class MunicipioFilialFilOrigem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMunicipioFilialFilOrigem;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdMunicipioFilialFilOrigem() {
        return this.idMunicipioFilialFilOrigem;
    }

    public void setIdMunicipioFilialFilOrigem(Long idMunicipioFilialFilOrigem) {
        this.idMunicipioFilialFilOrigem = idMunicipioFilialFilOrigem;
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

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMunicipioFilialFilOrigem",
				getIdMunicipioFilialFilOrigem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioFilialFilOrigem))
			return false;
        MunicipioFilialFilOrigem castOther = (MunicipioFilialFilOrigem) other;
		return new EqualsBuilder().append(this.getIdMunicipioFilialFilOrigem(),
				castOther.getIdMunicipioFilialFilOrigem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioFilialFilOrigem())
            .toHashCode();
    }
}
