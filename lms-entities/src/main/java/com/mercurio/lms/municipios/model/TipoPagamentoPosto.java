package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoPagamentoPosto implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoPagamentoPosto;

    /** persistent field */
    private Integer nrPrioridadeUso;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoPagamPostoPassagem tipoPagamPostoPassagem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PostoPassagem postoPassagem;

    public Long getIdTipoPagamentoPosto() {
        return this.idTipoPagamentoPosto;
    }

    public void setIdTipoPagamentoPosto(Long idTipoPagamentoPosto) {
        this.idTipoPagamentoPosto = idTipoPagamentoPosto;
    }

    public Integer getNrPrioridadeUso() {
        return this.nrPrioridadeUso;
    }

    public void setNrPrioridadeUso(Integer nrPrioridadeUso) {
        this.nrPrioridadeUso = nrPrioridadeUso;
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

    public com.mercurio.lms.municipios.model.TipoPagamPostoPassagem getTipoPagamPostoPassagem() {
        return this.tipoPagamPostoPassagem;
    }

	public void setTipoPagamPostoPassagem(
			com.mercurio.lms.municipios.model.TipoPagamPostoPassagem tipoPagamPostoPassagem) {
        this.tipoPagamPostoPassagem = tipoPagamPostoPassagem;
    }

    public com.mercurio.lms.municipios.model.PostoPassagem getPostoPassagem() {
        return this.postoPassagem;
    }

	public void setPostoPassagem(
			com.mercurio.lms.municipios.model.PostoPassagem postoPassagem) {
        this.postoPassagem = postoPassagem;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoPagamentoPosto",
				getIdTipoPagamentoPosto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoPagamentoPosto))
			return false;
        TipoPagamentoPosto castOther = (TipoPagamentoPosto) other;
		return new EqualsBuilder().append(this.getIdTipoPagamentoPosto(),
				castOther.getIdTipoPagamentoPosto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoPagamentoPosto())
            .toHashCode();
    }

}
