package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class EixosTipoMeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEixosTipoMeioTransporte;

    /** persistent field */
    private Integer qtEixos;

    /** persistent field */
    private TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private List meioTransporteRodoviarios;
    
    public Long getIdEixosTipoMeioTransporte() {
        return this.idEixosTipoMeioTransporte;
    }

    public void setIdEixosTipoMeioTransporte(Long idEixosTipoMeioTransporte) {
        this.idEixosTipoMeioTransporte = idEixosTipoMeioTransporte;
    }

    public Integer getQtEixos() {
        return this.qtEixos;
    }

    public void setQtEixos(Integer qtEixos) {
        this.qtEixos = qtEixos;
    }

    public TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

    public void setTipoMeioTransporte(TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario.class)     
    public List getMeioTransporteRodoviarios() {
        return this.meioTransporteRodoviarios;
    }

    public void setMeioTransporteRodoviarios(List meioTransporteRodoviarios) {
        this.meioTransporteRodoviarios = meioTransporteRodoviarios;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEixosTipoMeioTransporte",
				getIdEixosTipoMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EixosTipoMeioTransporte))
			return false;
        EixosTipoMeioTransporte castOther = (EixosTipoMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdEixosTipoMeioTransporte(),
				castOther.getIdEixosTipoMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEixosTipoMeioTransporte())
            .toHashCode();
    }
}