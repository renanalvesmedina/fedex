package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RegiaoFilialRotaColEnt implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegiaoFilialRotaColEnt;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil regiaoColetaEntregaFil;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;

    public Long getIdRegiaoFilialRotaColEnt() {
        return this.idRegiaoFilialRotaColEnt;
    }

    public void setIdRegiaoFilialRotaColEnt(Long idRegiaoFilialRotaColEnt) {
        this.idRegiaoFilialRotaColEnt = idRegiaoFilialRotaColEnt;
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

    public com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil getRegiaoColetaEntregaFil() {
        return this.regiaoColetaEntregaFil;
    }

	public void setRegiaoColetaEntregaFil(
			com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil regiaoColetaEntregaFil) {
        this.regiaoColetaEntregaFil = regiaoColetaEntregaFil;
    }

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRegiaoFilialRotaColEnt",
				getIdRegiaoFilialRotaColEnt()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegiaoFilialRotaColEnt))
			return false;
        RegiaoFilialRotaColEnt castOther = (RegiaoFilialRotaColEnt) other;
		return new EqualsBuilder().append(this.getIdRegiaoFilialRotaColEnt(),
				castOther.getIdRegiaoFilialRotaColEnt()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegiaoFilialRotaColEnt())
            .toHashCode();
    }

}
