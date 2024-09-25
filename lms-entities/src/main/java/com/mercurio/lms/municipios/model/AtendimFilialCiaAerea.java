package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class AtendimFilialCiaAerea implements Serializable,Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAtendimFilialCiaAerea;

    /** persistent field */
    private Boolean blDomingo;

    /** persistent field */
    private Boolean blSegunda;

    /** persistent field */
    private Boolean blTerca;

    /** persistent field */
    private Boolean blQuarta;

    /** persistent field */
    private Boolean blQuinta;

    /** persistent field */
    private Boolean blSexta;

    /** persistent field */
    private Boolean blSabado;

    /** persistent field */
    private TimeOfDay hrAtendimentoInicial;

    /** persistent field */
    private TimeOfDay hrAtendimentoFinal;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.FilialCiaAerea filialCiaAerea;

    public Long getIdAtendimFilialCiaAerea() {
        return this.idAtendimFilialCiaAerea;
    }

    public void setIdAtendimFilialCiaAerea(Long idAtendimFilialCiaAerea) {
        this.idAtendimFilialCiaAerea = idAtendimFilialCiaAerea;
    }

    public Boolean getBlDomingo() {
        return this.blDomingo;
    }

    public void setBlDomingo(Boolean blDomingo) {
        this.blDomingo = blDomingo;
    }

    public Boolean getBlSegunda() {
        return this.blSegunda;
    }

    public void setBlSegunda(Boolean blSegunda) {
        this.blSegunda = blSegunda;
    }

    public Boolean getBlTerca() {
        return this.blTerca;
    }

    public void setBlTerca(Boolean blTerca) {
        this.blTerca = blTerca;
    }

    public Boolean getBlQuarta() {
        return this.blQuarta;
    }

    public void setBlQuarta(Boolean blQuarta) {
        this.blQuarta = blQuarta;
    }

    public Boolean getBlQuinta() {
        return this.blQuinta;
    }

    public void setBlQuinta(Boolean blQuinta) {
        this.blQuinta = blQuinta;
    }

    public Boolean getBlSexta() {
        return this.blSexta;
    }

    public void setBlSexta(Boolean blSexta) {
        this.blSexta = blSexta;
    }

    public Boolean getBlSabado() {
        return this.blSabado;
    }

    public void setBlSabado(Boolean blSabado) {
        this.blSabado = blSabado;
    }

    public TimeOfDay getHrAtendimentoInicial() {
        return this.hrAtendimentoInicial;
    }

    public void setHrAtendimentoInicial(TimeOfDay hrAtendimentoInicial) {
        this.hrAtendimentoInicial = hrAtendimentoInicial;
    }

    public TimeOfDay getHrAtendimentoFinal() {
        return this.hrAtendimentoFinal;
    }

    public void setHrAtendimentoFinal(TimeOfDay hrAtendimentoFinal) {
        this.hrAtendimentoFinal = hrAtendimentoFinal;
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

    public com.mercurio.lms.municipios.model.FilialCiaAerea getFilialCiaAerea() {
        return this.filialCiaAerea;
    }

	public void setFilialCiaAerea(
			com.mercurio.lms.municipios.model.FilialCiaAerea filialCiaAerea) {
        this.filialCiaAerea = filialCiaAerea;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAtendimFilialCiaAerea",
				getIdAtendimFilialCiaAerea()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AtendimFilialCiaAerea))
			return false;
        AtendimFilialCiaAerea castOther = (AtendimFilialCiaAerea) other;
		return new EqualsBuilder().append(this.getIdAtendimFilialCiaAerea(),
				castOther.getIdAtendimFilialCiaAerea()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAtendimFilialCiaAerea())
            .toHashCode();
    }

}
