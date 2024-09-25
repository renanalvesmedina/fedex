package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Turno implements Serializable,  Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTurno;

    /** persistent field */
    private String  dsTurno;

    /** persistent field */
    private TimeOfDay hrTurnoInicial;

    /** persistent field */
    private TimeOfDay hrTurnoFinal;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List agendamentoEntregas;

    public Long getIdTurno() {
        return this.idTurno;
    }

    public void setIdTurno(Long idTurno) {
        this.idTurno = idTurno;
    }

    public String  getDsTurno() {
        return this.dsTurno;
    }

    public void setDsTurno(String  dsTurno) {
        this.dsTurno = dsTurno;
    }

    public TimeOfDay getHrTurnoInicial() {
        return this.hrTurnoInicial;
    }

    public void setHrTurnoInicial(TimeOfDay hrTurnoInicial) {
        this.hrTurnoInicial = hrTurnoInicial;
    }

    public TimeOfDay getHrTurnoFinal() {
        return this.hrTurnoFinal;
    }

    public void setHrTurnoFinal(TimeOfDay hrTurnoFinal) {
        this.hrTurnoFinal = hrTurnoFinal;
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

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.AgendamentoEntrega.class)     
    public List getAgendamentoEntregas() {
        return this.agendamentoEntregas;
    }

    public void setAgendamentoEntregas(List agendamentoEntregas) {
        this.agendamentoEntregas = agendamentoEntregas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTurno", getIdTurno())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Turno))
			return false;
        Turno castOther = (Turno) other;
		return new EqualsBuilder().append(this.getIdTurno(),
				castOther.getIdTurno()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTurno()).toHashCode();
    }

}
