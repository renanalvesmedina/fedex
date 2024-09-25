package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class FilialRotaCc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idFilialRotaCc;

    /** persistent field */
    private Byte nrOrdem;
    
    /** persistent field */    
    private Boolean blInseridoManualmente;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    public Long getIdFilialRotaCc() {
        return this.idFilialRotaCc;
    }

    public void setIdFilialRotaCc(Long idFilialRotaCc) {
        this.idFilialRotaCc = idFilialRotaCc;
    }

    public Boolean getBlInseridoManualmente() {
		return blInseridoManualmente;
	}

	public void setBlInseridoManualmente(Boolean blInseridoManualmente) {
		this.blInseridoManualmente = blInseridoManualmente;
	}

    public Byte getNrOrdem() {
        return this.nrOrdem;
    }

    public void setNrOrdem(Byte nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idFilialRotaCc",
				getIdFilialRotaCc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FilialRotaCc))
			return false;
        FilialRotaCc castOther = (FilialRotaCc) other;
		return new EqualsBuilder().append(this.getIdFilialRotaCc(),
				castOther.getIdFilialRotaCc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFilialRotaCc()).toHashCode();
    }

}
