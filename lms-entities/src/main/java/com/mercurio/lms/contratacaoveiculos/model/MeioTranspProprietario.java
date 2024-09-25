package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTranspProprietario implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTranspProprietario;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */ 
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario;

    public Long getIdMeioTranspProprietario() {
        return this.idMeioTranspProprietario;
    }

    public void setIdMeioTranspProprietario(Long idMeioTranspProprietario) {
        this.idMeioTranspProprietario = idMeioTranspProprietario;
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

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Proprietario getProprietario() {
        return this.proprietario;
    }

	public void setProprietario(
			com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTranspProprietario",
				getIdMeioTranspProprietario()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTranspProprietario))
			return false;
        MeioTranspProprietario castOther = (MeioTranspProprietario) other;
		return new EqualsBuilder().append(this.getIdMeioTranspProprietario(),
				castOther.getIdMeioTranspProprietario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTranspProprietario())
            .toHashCode();
    }

}
