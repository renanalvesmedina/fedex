package com.mercurio.lms.vol.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolGrfsVeiculos implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idGruVeic;

    /** persistent field */
    private VolGruposFrotas volGruposFrota;

    /** persistent field */
    private MeioTransporte meioTransporte;

    public Long getIdGruVeic() {
        return this.idGruVeic;
    }

    public void setIdGruVeic(Long idGruVeic) {
        this.idGruVeic = idGruVeic;
    }

    public VolGruposFrotas getVolGruposFrota() {
        return this.volGruposFrota;
    }

    public void setVolGruposFrota(VolGruposFrotas volGruposFrota) {
        this.volGruposFrota = volGruposFrota;
    }

    public MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

    public void setMeioTransporte(MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idGruVeic", getIdGruVeic())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolGrfsVeiculos))
			return false;
        VolGrfsVeiculos castOther = (VolGrfsVeiculos) other;
		return new EqualsBuilder().append(this.getIdGruVeic(),
				castOther.getIdGruVeic()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGruVeic()).toHashCode();
    }

}
