package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class PontoParadaTrecho implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPontoParadaTrecho;

    /** persistent field */
    private Integer nrTempoParada;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private Short nrOrdem;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TrechoRotaIdaVolta trechoRotaIdaVolta;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PontoParada pontoParada;

    /** persistent field */
    private List motivoParadaPontoTrechos;

    /** persistent field */
    private List eventoMeioTransportes;

    public Long getIdPontoParadaTrecho() {
        return this.idPontoParadaTrecho;
    }

    public void setIdPontoParadaTrecho(Long idPontoParadaTrecho) {
        this.idPontoParadaTrecho = idPontoParadaTrecho;
    }

    public Integer getNrTempoParada() {
        return this.nrTempoParada;
    }

    public void setNrTempoParada(Integer nrTempoParada) {
        this.nrTempoParada = nrTempoParada;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public Short getNrOrdem() {
        return this.nrOrdem;
    }

    public void setNrOrdem(Short nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }
    
    public com.mercurio.lms.municipios.model.TrechoRotaIdaVolta getTrechoRotaIdaVolta() {
        return this.trechoRotaIdaVolta;
    }

	public void setTrechoRotaIdaVolta(
			com.mercurio.lms.municipios.model.TrechoRotaIdaVolta trechoRotaIdaVolta) {
        this.trechoRotaIdaVolta = trechoRotaIdaVolta;
    }

    public com.mercurio.lms.municipios.model.PontoParada getPontoParada() {
        return this.pontoParada;
    }

	public void setPontoParada(
			com.mercurio.lms.municipios.model.PontoParada pontoParada) {
        this.pontoParada = pontoParada;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MotivoParadaPontoTrecho.class)     
    public List getMotivoParadaPontoTrechos() {
        return this.motivoParadaPontoTrechos;
    }

    public void setMotivoParadaPontoTrechos(List motivoParadaPontoTrechos) {
        this.motivoParadaPontoTrechos = motivoParadaPontoTrechos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte.class)     
    public List getEventoMeioTransportes() {
        return this.eventoMeioTransportes;
    }

    public void setEventoMeioTransportes(List eventoMeioTransportes) {
        this.eventoMeioTransportes = eventoMeioTransportes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPontoParadaTrecho",
				getIdPontoParadaTrecho()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PontoParadaTrecho))
			return false;
        PontoParadaTrecho castOther = (PontoParadaTrecho) other;
		return new EqualsBuilder().append(this.getIdPontoParadaTrecho(),
				castOther.getIdPontoParadaTrecho()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPontoParadaTrecho())
            .toHashCode();
    }
    
}
