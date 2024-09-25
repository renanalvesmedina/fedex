package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class SemiReboqueCc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSemiReboqueCc;

    /** nullable persistent field */
    private DateTime dhTroca;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** nullable persistent field */
    private com.mercurio.lms.carregamento.model.LocalTroca localTroca;
    
    public Long getIdSemiReboqueCc() {
        return this.idSemiReboqueCc;
    }

    public void setIdSemiReboqueCc(Long idSemiReboqueCc) {
        this.idSemiReboqueCc = idSemiReboqueCc;
    }

    public DateTime getDhTroca() {
        return this.dhTroca;
    }

    public void setDhTroca(DateTime dhTroca) {
        this.dhTroca = dhTroca;
    }

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.carregamento.model.LocalTroca getLocalTroca() {
        return this.localTroca;
    }

	public void setLocalTroca(
			com.mercurio.lms.carregamento.model.LocalTroca localTroca) {
        this.localTroca = localTroca;
    }

	public String toString() {
		return new ToStringBuilder(this).append("idSemiReboqueCc",
				getIdSemiReboqueCc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SemiReboqueCc))
			return false;
        SemiReboqueCc castOther = (SemiReboqueCc) other;
		return new EqualsBuilder().append(this.getIdSemiReboqueCc(),
				castOther.getIdSemiReboqueCc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSemiReboqueCc()).toHashCode();
    }

}
