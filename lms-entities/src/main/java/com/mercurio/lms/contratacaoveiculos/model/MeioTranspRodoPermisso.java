package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTranspRodoPermisso implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTranspRodoPermisso;

    /** persistent field */
    private Long nrPermisso;

    /** persistent field */ 
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais pais;

    public Long getIdMeioTranspRodoPermisso() {
        return this.idMeioTranspRodoPermisso;
    }

    public void setIdMeioTranspRodoPermisso(Long idMeioTranspRodoPermisso) {
        this.idMeioTranspRodoPermisso = idMeioTranspRodoPermisso;
    }

    public Long getNrPermisso() {
        return this.nrPermisso;
    }

    public void setNrPermisso(Long nrPermisso) {
        this.nrPermisso = nrPermisso;
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

    public com.mercurio.lms.municipios.model.Pais getPais() {
        return this.pais;
    }

    public void setPais(com.mercurio.lms.municipios.model.Pais pais) {
        this.pais = pais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTranspRodoPermisso",
				getIdMeioTranspRodoPermisso()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTranspRodoPermisso))
			return false;
        MeioTranspRodoPermisso castOther = (MeioTranspRodoPermisso) other;
		return new EqualsBuilder().append(this.getIdMeioTranspRodoPermisso(),
				castOther.getIdMeioTranspRodoPermisso()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTranspRodoPermisso())
            .toHashCode();
    }

}
