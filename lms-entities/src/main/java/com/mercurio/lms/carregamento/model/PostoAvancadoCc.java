package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class PostoAvancadoCc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPostoAvancadoCc;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleTrecho controleTrecho;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PostoAvancado postoAvancado;

    public Long getIdPostoAvancadoCc() {
        return this.idPostoAvancadoCc;
    }

    public void setIdPostoAvancadoCc(Long idPostoAvancadoCc) {
        this.idPostoAvancadoCc = idPostoAvancadoCc;
    }

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.carregamento.model.ControleTrecho getControleTrecho() {
		return controleTrecho;
	}

	public void setControleTrecho(
			com.mercurio.lms.carregamento.model.ControleTrecho controleTrecho) {
		this.controleTrecho = controleTrecho;
	}

	public com.mercurio.lms.municipios.model.PostoAvancado getPostoAvancado() {
        return this.postoAvancado;
    }

	public void setPostoAvancado(
			com.mercurio.lms.municipios.model.PostoAvancado postoAvancado) {
        this.postoAvancado = postoAvancado;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPostoAvancadoCc",
				getIdPostoAvancadoCc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PostoAvancadoCc))
			return false;
        PostoAvancadoCc castOther = (PostoAvancadoCc) other;
		return new EqualsBuilder().append(this.getIdPostoAvancadoCc(),
				castOther.getIdPostoAvancadoCc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPostoAvancadoCc())
            .toHashCode();
    }

}
