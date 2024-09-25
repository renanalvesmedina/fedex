package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class HorarioTransito implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHorarioTransito;

    /** persistent field */
    private TimeOfDay hrTransitoInicial;

    /** persistent field */
    private TimeOfDay hrTransitoFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaIntervaloCep rotaIntervaloCep;

    public Long getIdHorarioTransito() {
        return this.idHorarioTransito;
    }

    public void setIdHorarioTransito(Long idHorarioTransito) {
        this.idHorarioTransito = idHorarioTransito;
    }

    public TimeOfDay getHrTransitoInicial() {
        return this.hrTransitoInicial;
    }

    public void setHrTransitoInicial(TimeOfDay hrTransitoInicial) {
        this.hrTransitoInicial = hrTransitoInicial;
    }

    public TimeOfDay getHrTransitoFinal() {
        return this.hrTransitoFinal;
    }

    public void setHrTransitoFinal(TimeOfDay hrTransitoFinal) {
        this.hrTransitoFinal = hrTransitoFinal;
    }

    public com.mercurio.lms.municipios.model.RotaIntervaloCep getRotaIntervaloCep() {
        return this.rotaIntervaloCep;
    }

	public void setRotaIntervaloCep(
			com.mercurio.lms.municipios.model.RotaIntervaloCep rotaIntervaloCep) {
        this.rotaIntervaloCep = rotaIntervaloCep;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHorarioTransito",
				getIdHorarioTransito()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HorarioTransito))
			return false;
        HorarioTransito castOther = (HorarioTransito) other;
		return new EqualsBuilder().append(this.getIdHorarioTransito(),
				castOther.getIdHorarioTransito()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHorarioTransito())
            .toHashCode();
    }

}
