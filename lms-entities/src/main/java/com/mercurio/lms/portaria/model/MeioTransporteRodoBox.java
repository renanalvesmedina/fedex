package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTransporteRodoBox implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTransporteRodoBox;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial; 

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Box box;

    public Long getIdMeioTransporteRodoBox() {
        return this.idMeioTransporteRodoBox;
    }

    public void setIdMeioTransporteRodoBox(Long idMeioTransporteRodoBox) {
        this.idMeioTransporteRodoBox = idMeioTransporteRodoBox;
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

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return this.meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }

    public com.mercurio.lms.portaria.model.Box getBox() {
        return this.box;
    }

    public void setBox(com.mercurio.lms.portaria.model.Box box) {
        this.box = box;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTransporteRodoBox",
				getIdMeioTransporteRodoBox()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTransporteRodoBox))
			return false;
        MeioTransporteRodoBox castOther = (MeioTransporteRodoBox) other;
		return new EqualsBuilder().append(this.getIdMeioTransporteRodoBox(),
				castOther.getIdMeioTransporteRodoBox()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTransporteRodoBox())
            .toHashCode();
    }

}
