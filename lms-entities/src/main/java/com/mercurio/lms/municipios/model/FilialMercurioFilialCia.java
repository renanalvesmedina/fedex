package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class FilialMercurioFilialCia implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFilialMercurioFilialCia;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private Byte nrOrdemUso;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.CiaFilialMercurio ciaFilialMercurio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.FilialCiaAerea filialCiaAerea;

    public Long getIdFilialMercurioFilialCia() {
        return this.idFilialMercurioFilialCia;
    }

    public void setIdFilialMercurioFilialCia(Long idFilialMercurioFilialCia) {
        this.idFilialMercurioFilialCia = idFilialMercurioFilialCia;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public Byte getNrOrdemUso() {
        return this.nrOrdemUso;
    }

    public void setNrOrdemUso(Byte nrOrdemUso) {
        this.nrOrdemUso = nrOrdemUso;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.CiaFilialMercurio getCiaFilialMercurio() {
        return this.ciaFilialMercurio;
    }

	public void setCiaFilialMercurio(
			com.mercurio.lms.municipios.model.CiaFilialMercurio ciaFilialMercurio) {
        this.ciaFilialMercurio = ciaFilialMercurio;
    }

    public com.mercurio.lms.municipios.model.FilialCiaAerea getFilialCiaAerea() {
        return this.filialCiaAerea;
    }

	public void setFilialCiaAerea(
			com.mercurio.lms.municipios.model.FilialCiaAerea filialCiaAerea) {
        this.filialCiaAerea = filialCiaAerea;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idFilialMercurioFilialCia",
				getIdFilialMercurioFilialCia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialMercurioFilialCia))
			return false;
        FilialMercurioFilialCia castOther = (FilialMercurioFilialCia) other;
		return new EqualsBuilder().append(this.getIdFilialMercurioFilialCia(),
				castOther.getIdFilialMercurioFilialCia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialMercurioFilialCia())
            .toHashCode();
    }

}
