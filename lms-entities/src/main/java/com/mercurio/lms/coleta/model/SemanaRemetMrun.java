package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class SemanaRemetMrun implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSemanaRemetMrun;

    /** persistent field */
    private DomainValue tpSemanaDoMes;

    /** nullable persistent field */
    private Short nrDomingo;

    /** nullable persistent field */
    private Short nrSegundaFeira;

    /** nullable persistent field */
    private Short nrTercaFeira;

    /** nullable persistent field */
    private Short nrQuartaFeira;

    /** nullable persistent field */
    private Short nrQuintaFeira;

    /** nullable persistent field */
    private Short nrSextaFeira;

    /** nullable persistent field */
    private Short nrSabado;

    /** nullable persistent field */
    private TimeOfDay hrInicialDomingo;

    /** nullable persistent field */
    private TimeOfDay hrInicialSegundaFeira;

    /** nullable persistent field */
    private TimeOfDay hrInicialTercaFeira;

    /** nullable persistent field */
    private TimeOfDay hrInicialQuartaFeira;

    /** nullable persistent field */
    private TimeOfDay hrInicialQuintaFeira;

    /** nullable persistent field */
    private TimeOfDay hrInicialSextaFeira;

    /** nullable persistent field */
    private TimeOfDay hrInicialSabado;

    /** nullable persistent field */
    private TimeOfDay hrFinalDomingo;

    /** nullable persistent field */
    private TimeOfDay hrFinalSegundaFeira;

    /** nullable persistent field */
    private TimeOfDay hrFinalTercaFeira;

    /** nullable persistent field */
    private TimeOfDay hrFinalQuartaFeira;

    /** nullable persistent field */
    private TimeOfDay hrFinalQuintaFeira;

    /** nullable persistent field */
    private TimeOfDay hrFinalSextaFeira;

    /** nullable persistent field */
    private TimeOfDay hrFinalSabado;

    /** persistent field */
    private com.mercurio.lms.coleta.model.MilkRemetente milkRemetente;

    public Long getIdSemanaRemetMrun() {
        return this.idSemanaRemetMrun;
    }

    public void setIdSemanaRemetMrun(Long idSemanaRemetMrun) {
        this.idSemanaRemetMrun = idSemanaRemetMrun;
    }

    public DomainValue getTpSemanaDoMes() {
        return this.tpSemanaDoMes;
    }

    public void setTpSemanaDoMes(DomainValue tpSemanaDoMes) {
        this.tpSemanaDoMes = tpSemanaDoMes;
    }

    public Short getNrDomingo() {
        return this.nrDomingo;
    }

    public void setNrDomingo(Short nrDomingo) {
        this.nrDomingo = nrDomingo;
    }

    public Short getNrSegundaFeira() {
        return this.nrSegundaFeira;
    }

    public void setNrSegundaFeira(Short nrSegundaFeira) {
        this.nrSegundaFeira = nrSegundaFeira;
    }

    public Short getNrTercaFeira() {
        return this.nrTercaFeira;
    }

    public void setNrTercaFeira(Short nrTercaFeira) {
        this.nrTercaFeira = nrTercaFeira;
    }

    public Short getNrQuartaFeira() {
        return this.nrQuartaFeira;
    }

    public void setNrQuartaFeira(Short nrQuartaFeira) {
        this.nrQuartaFeira = nrQuartaFeira;
    }

    public Short getNrQuintaFeira() {
        return this.nrQuintaFeira;
    }

    public void setNrQuintaFeira(Short nrQuintaFeira) {
        this.nrQuintaFeira = nrQuintaFeira;
    }

    public Short getNrSextaFeira() {
        return this.nrSextaFeira;
    }

    public void setNrSextaFeira(Short nrSextaFeira) {
        this.nrSextaFeira = nrSextaFeira;
    }

    public Short getNrSabado() {
        return this.nrSabado;
    }

    public void setNrSabado(Short nrSabado) {
        this.nrSabado = nrSabado;
    }

    public com.mercurio.lms.coleta.model.MilkRemetente getMilkRemetente() {
        return this.milkRemetente;
    }

	public void setMilkRemetente(
			com.mercurio.lms.coleta.model.MilkRemetente milkRemetente) {
        this.milkRemetente = milkRemetente;
    }
    
    public TimeOfDay getHrFinalDomingo() {
        return hrFinalDomingo;
    }

    public void setHrFinalDomingo(TimeOfDay hrFinalDomingo) {
        this.hrFinalDomingo = hrFinalDomingo;
    }
    
    public TimeOfDay getHrFinalQuartaFeira() {
        return hrFinalQuartaFeira;
    }
    
    public void setHrFinalQuartaFeira(TimeOfDay hrFinalQuartaFeira) {
        this.hrFinalQuartaFeira = hrFinalQuartaFeira;
    }
    
    public TimeOfDay getHrFinalQuintaFeira() {
        return hrFinalQuintaFeira;
    }
    
    public void setHrFinalQuintaFeira(TimeOfDay hrFinalQuintaFeira) {
        this.hrFinalQuintaFeira = hrFinalQuintaFeira;
    }
    
    public TimeOfDay getHrFinalSabado() {
        return hrFinalSabado;
    }
    
    public void setHrFinalSabado(TimeOfDay hrFinalSabado) {
        this.hrFinalSabado = hrFinalSabado;
    }
    
    public TimeOfDay getHrFinalSegundaFeira() {
        return hrFinalSegundaFeira;
    }
    
    public void setHrFinalSegundaFeira(TimeOfDay hrFinalSegundaFeira) {
        this.hrFinalSegundaFeira = hrFinalSegundaFeira;
    }
    
    public TimeOfDay getHrFinalSextaFeira() {
        return hrFinalSextaFeira;
    }
    
    public void setHrFinalSextaFeira(TimeOfDay hrFinalSextaFeira) {
        this.hrFinalSextaFeira = hrFinalSextaFeira;
    }
    
    public TimeOfDay getHrFinalTercaFeira() {
        return hrFinalTercaFeira;
    }
    
    public void setHrFinalTercaFeira(TimeOfDay hrFinalTercaFeira) {
        this.hrFinalTercaFeira = hrFinalTercaFeira;
    }
    
    public TimeOfDay getHrInicialDomingo() {
        return hrInicialDomingo;
    }
    
    public void setHrInicialDomingo(TimeOfDay hrInicialDomingo) {
        this.hrInicialDomingo = hrInicialDomingo;
    }
    
    public TimeOfDay getHrInicialQuartaFeira() {
        return hrInicialQuartaFeira;
    }
    
    public void setHrInicialQuartaFeira(TimeOfDay hrInicialQuartaFeira) {
        this.hrInicialQuartaFeira = hrInicialQuartaFeira;
    }
    
    public TimeOfDay getHrInicialQuintaFeira() {
        return hrInicialQuintaFeira;
    }
    
    public void setHrInicialQuintaFeira(TimeOfDay hrInicialQuintaFeira) {
        this.hrInicialQuintaFeira = hrInicialQuintaFeira;
    }
    
    public TimeOfDay getHrInicialSabado() {
        return hrInicialSabado;
    }
    
    public void setHrInicialSabado(TimeOfDay hrInicialSabado) {
        this.hrInicialSabado = hrInicialSabado;
    }
    
    public TimeOfDay getHrInicialSegundaFeira() {
        return hrInicialSegundaFeira;
    }
    
    public void setHrInicialSegundaFeira(TimeOfDay hrInicialSegundaFeira) {
        this.hrInicialSegundaFeira = hrInicialSegundaFeira;
    }
    
    public TimeOfDay getHrInicialSextaFeira() {
        return hrInicialSextaFeira;
    }
    
    public void setHrInicialSextaFeira(TimeOfDay hrInicialSextaFeira) {
        this.hrInicialSextaFeira = hrInicialSextaFeira;
    }
    
    public TimeOfDay getHrInicialTercaFeira() {
        return hrInicialTercaFeira;
    }
    
    public void setHrInicialTercaFeira(TimeOfDay hrInicialTercaFeira) {
        this.hrInicialTercaFeira = hrInicialTercaFeira;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idSemanaRemetMrun",
				getIdSemanaRemetMrun()).toString();
    }

	public boolean equals(Object other) {
        if ((this == other))
            return true;
        if (!(other instanceof SemanaRemetMrun))
            return false;
        SemanaRemetMrun castOther = (SemanaRemetMrun) other;
		return new EqualsBuilder().append(this.getIdSemanaRemetMrun(),
				castOther.getIdSemanaRemetMrun()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSemanaRemetMrun())
				.toHashCode();
    }

}
