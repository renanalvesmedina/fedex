package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class HorarioPrevistoSaidaRota implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHorarioPrevistoSaidaRota;

    /** persistent field */
    private TimeOfDay hrPrevista;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;

    public Long getIdHorarioPrevistoSaidaRota() {
        return this.idHorarioPrevistoSaidaRota;
    }

    public void setIdHorarioPrevistoSaidaRota(Long idHorarioPrevistoSaidaRota) {
        this.idHorarioPrevistoSaidaRota = idHorarioPrevistoSaidaRota;
    }

    public TimeOfDay getHrPrevista() {
        return this.hrPrevista;
    }

    public void setHrPrevista(TimeOfDay hrPrevista) {
        this.hrPrevista = hrPrevista;
    }

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHorarioPrevistoSaidaRota",
				getIdHorarioPrevistoSaidaRota()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HorarioPrevistoSaidaRota))
			return false;
        HorarioPrevistoSaidaRota castOther = (HorarioPrevistoSaidaRota) other;
		return new EqualsBuilder().append(this.getIdHorarioPrevistoSaidaRota(),
				castOther.getIdHorarioPrevistoSaidaRota()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHorarioPrevistoSaidaRota())
            .toHashCode();
    }

}
