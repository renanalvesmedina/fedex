package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTransporteRotaViagem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTransporteRotaViagem;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaViagem rotaViagem;

    public Long getIdMeioTransporteRotaViagem() {
        return this.idMeioTransporteRotaViagem;
    }

    public void setIdMeioTransporteRotaViagem(Long idMeioTransporteRotaViagem) {
        this.idMeioTransporteRotaViagem = idMeioTransporteRotaViagem;
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

    public com.mercurio.lms.municipios.model.RotaViagem getRotaViagem() {
        return this.rotaViagem;
    }

	public void setRotaViagem(
			com.mercurio.lms.municipios.model.RotaViagem rotaViagem) {
        this.rotaViagem = rotaViagem;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTransporteRotaViagem",
				getIdMeioTransporteRotaViagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTransporteRotaViagem))
			return false;
        MeioTransporteRotaViagem castOther = (MeioTransporteRotaViagem) other;
		return new EqualsBuilder().append(this.getIdMeioTransporteRotaViagem(),
				castOther.getIdMeioTransporteRotaViagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTransporteRotaViagem())
            .toHashCode();
    }

}
