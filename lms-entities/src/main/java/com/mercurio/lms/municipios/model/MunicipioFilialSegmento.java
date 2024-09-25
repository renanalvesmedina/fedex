package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class MunicipioFilialSegmento implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMunicipioFilialSegmento;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.vendas.model.SegmentoMercado segmentoMercado;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial;

    public Long getIdMunicipioFilialSegmento() {
        return this.idMunicipioFilialSegmento;
    }

    public void setIdMunicipioFilialSegmento(Long idMunicipioFilialSegmento) {
        this.idMunicipioFilialSegmento = idMunicipioFilialSegmento;
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

    public com.mercurio.lms.vendas.model.SegmentoMercado getSegmentoMercado() {
        return this.segmentoMercado;
    }

	public void setSegmentoMercado(
			com.mercurio.lms.vendas.model.SegmentoMercado segmentoMercado) {
        this.segmentoMercado = segmentoMercado;
    }

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilial() {
        return this.municipioFilial;
    }

	public void setMunicipioFilial(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial) {
        this.municipioFilial = municipioFilial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMunicipioFilialSegmento",
				getIdMunicipioFilialSegmento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MunicipioFilialSegmento))
			return false;
        MunicipioFilialSegmento castOther = (MunicipioFilialSegmento) other;
		return new EqualsBuilder().append(this.getIdMunicipioFilialSegmento(),
				castOther.getIdMunicipioFilialSegmento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMunicipioFilialSegmento())
            .toHashCode();
    }

}
