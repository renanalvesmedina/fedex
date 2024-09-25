package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTranspRodoMotorista implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTranspRodoMotorista;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal; 

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Motorista motorista;

    public Long getIdMeioTranspRodoMotorista() {
        return this.idMeioTranspRodoMotorista;
    }

    public void setIdMeioTranspRodoMotorista(Long idMeioTranspRodoMotorista) {
        this.idMeioTranspRodoMotorista = idMeioTranspRodoMotorista;
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

    public com.mercurio.lms.contratacaoveiculos.model.Motorista getMotorista() {
        return this.motorista;
    }

	public void setMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista motorista) {
        this.motorista = motorista;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTranspRodoMotorista",
				getIdMeioTranspRodoMotorista()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTranspRodoMotorista))
			return false;
        MeioTranspRodoMotorista castOther = (MeioTranspRodoMotorista) other;
		return new EqualsBuilder().append(this.getIdMeioTranspRodoMotorista(),
				castOther.getIdMeioTranspRodoMotorista()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTranspRodoMotorista())
            .toHashCode();
    }

}
