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
public class RotaTipoMeioTransporte implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRotaTipoMeioTransporte;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;
    
    /** persistent field */
    private List rotaMeioTransporteRodovs;

    public Long getIdRotaTipoMeioTransporte() {
        return this.idRotaTipoMeioTransporte;
    }

    public void setIdRotaTipoMeioTransporte(Long idRotaTipoMeioTransporte) {
        this.idRotaTipoMeioTransporte = idRotaTipoMeioTransporte;
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

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRotaTipoMeioTransporte",
				getIdRotaTipoMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RotaTipoMeioTransporte))
			return false;
        RotaTipoMeioTransporte castOther = (RotaTipoMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdRotaTipoMeioTransporte(),
				castOther.getIdRotaTipoMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRotaTipoMeioTransporte())
            .toHashCode();
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaMeioTransporteRodov.class)     
	public List getRotaMeioTransporteRodovs() {
		return rotaMeioTransporteRodovs;
	}

	public void setRotaMeioTransporteRodovs(List rotaMeioTransporteRodovs) {
		this.rotaMeioTransporteRodovs = rotaMeioTransporteRodovs;
	}
}