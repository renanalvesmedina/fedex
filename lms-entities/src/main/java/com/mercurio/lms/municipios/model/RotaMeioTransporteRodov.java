package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RotaMeioTransporteRodov implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRotaMeioTransporteRodov;

    /** persistent field */ 
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaTipoMeioTransporte rotaTipoMeioTransporte;
    
    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    public Long getIdRotaMeioTransporteRodov() {
        return this.idRotaMeioTransporteRodov;
    }

    public void setIdRotaMeioTransporteRodov(Long idRotaMeioTransporteRodov) {
        this.idRotaMeioTransporteRodov = idRotaMeioTransporteRodov;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public com.mercurio.lms.municipios.model.RotaTipoMeioTransporte getRotaTipoMeioTransporte() {
		return rotaTipoMeioTransporte;
	}

	public void setRotaTipoMeioTransporte(
			com.mercurio.lms.municipios.model.RotaTipoMeioTransporte rotaTipoMeioTransporte) {
		this.rotaTipoMeioTransporte = rotaTipoMeioTransporte;
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

    public String toString() {
		return new ToStringBuilder(this).append("idRotaMeioTransporteRodov",
				getIdRotaMeioTransporteRodov()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RotaMeioTransporteRodov))
			return false;
        RotaMeioTransporteRodov castOther = (RotaMeioTransporteRodov) other;
		return new EqualsBuilder().append(this.getIdRotaMeioTransporteRodov(),
				castOther.getIdRotaMeioTransporteRodov()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRotaMeioTransporteRodov())
            .toHashCode();
    }

}
