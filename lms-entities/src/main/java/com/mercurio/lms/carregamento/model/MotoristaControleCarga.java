package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotoristaControleCarga implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotoristaControleCarga;

    /** nullable persistent field */
    private DateTime dhTroca;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Motorista motorista;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.LocalTroca localTroca;
    
    /** nullable persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora liberacaoReguladora;
    
    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncAlteraStatus;

    public Long getIdMotoristaControleCarga() {
        return this.idMotoristaControleCarga;
    }

    public void setIdMotoristaControleCarga(Long idMotoristaControleCarga) {
        this.idMotoristaControleCarga = idMotoristaControleCarga;
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

    public com.mercurio.lms.contratacaoveiculos.model.Motorista getMotorista() {
        return this.motorista;
    }

	public void setMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista motorista) {
        this.motorista = motorista;
    }

    public com.mercurio.lms.carregamento.model.LocalTroca getLocalTroca() {
        return this.localTroca;
    }

	public void setLocalTroca(
			com.mercurio.lms.carregamento.model.LocalTroca localTroca) {
        this.localTroca = localTroca;
    }
    
    public com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora getLiberacaoReguladora() {
        return liberacaoReguladora;
    }

	public void setLiberacaoReguladora(
			com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora liberacaoReguladora) {
        this.liberacaoReguladora = liberacaoReguladora;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdFuncAlteraStatus() {
        return this.usuarioByIdFuncAlteraStatus;
    }

	public void setUsuarioByIdFuncAlteraStatus(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncAlteraStatus) {
        this.usuarioByIdFuncAlteraStatus = usuarioByIdFuncAlteraStatus;
    }
    
    public String toString() {
		return new ToStringBuilder(this).append("idMotoristaControleCarga",
				getIdMotoristaControleCarga()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotoristaControleCarga))
			return false;
        MotoristaControleCarga castOther = (MotoristaControleCarga) other;
		return new EqualsBuilder().append(this.getIdMotoristaControleCarga(),
				castOther.getIdMotoristaControleCarga()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotoristaControleCarga())
            .toHashCode();
    }

}
