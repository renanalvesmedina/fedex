package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoParadaPontoTrecho implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoParadaPontoTrecho;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MotivoParada motivoParada;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PontoParadaTrecho pontoParadaTrecho;

    public Long getIdMotivoParadaPontoTrecho() {
        return this.idMotivoParadaPontoTrecho;
    }

    public void setIdMotivoParadaPontoTrecho(Long idMotivoParadaPontoTrecho) {
        this.idMotivoParadaPontoTrecho = idMotivoParadaPontoTrecho;
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

    public com.mercurio.lms.municipios.model.MotivoParada getMotivoParada() {
        return this.motivoParada;
    }

	public void setMotivoParada(
			com.mercurio.lms.municipios.model.MotivoParada motivoParada) {
        this.motivoParada = motivoParada;
    }

    public com.mercurio.lms.municipios.model.PontoParadaTrecho getPontoParadaTrecho() {
        return this.pontoParadaTrecho;
    }

	public void setPontoParadaTrecho(
			com.mercurio.lms.municipios.model.PontoParadaTrecho pontoParadaTrecho) {
        this.pontoParadaTrecho = pontoParadaTrecho;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoParadaPontoTrecho",
				getIdMotivoParadaPontoTrecho()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoParadaPontoTrecho))
			return false;
        MotivoParadaPontoTrecho castOther = (MotivoParadaPontoTrecho) other;
		return new EqualsBuilder().append(this.getIdMotivoParadaPontoTrecho(),
				castOther.getIdMotivoParadaPontoTrecho()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoParadaPontoTrecho())
            .toHashCode();
    }

}
