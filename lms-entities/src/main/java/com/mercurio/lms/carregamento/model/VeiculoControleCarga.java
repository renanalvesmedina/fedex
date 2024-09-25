package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class VeiculoControleCarga implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idVeiculoControleCarga;

    /** nullable persistent field */
    private DateTime dhTroca;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** nullable persistent field */
    private com.mercurio.lms.carregamento.model.LocalTroca localTroca;

    /** nullable persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao;
    
    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncAlteraStatus;

    /** persistent field */
    private List pagtoProprietarioCcs;

    public VeiculoControleCarga() {
    }

    public VeiculoControleCarga(Long idVeiculoControleCarga, DateTime dhTroca, ControleCarga controleCarga, MeioTransporte meioTransporte, LocalTroca localTroca, SolicitacaoContratacao solicitacaoContratacao, Usuario usuarioByIdFuncAlteraStatus, List pagtoProprietarioCcs) {
        this.idVeiculoControleCarga = idVeiculoControleCarga;
        this.dhTroca = dhTroca;
        this.controleCarga = controleCarga;
        this.meioTransporte = meioTransporte;
        this.localTroca = localTroca;
        this.solicitacaoContratacao = solicitacaoContratacao;
        this.usuarioByIdFuncAlteraStatus = usuarioByIdFuncAlteraStatus;
        this.pagtoProprietarioCcs = pagtoProprietarioCcs;
    }

    public Long getIdVeiculoControleCarga() {
        return this.idVeiculoControleCarga;
    }

    public void setIdVeiculoControleCarga(Long idVeiculoControleCarga) {
        this.idVeiculoControleCarga = idVeiculoControleCarga;
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

    public com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao getSolicitacaoContratacao() {
		return solicitacaoContratacao;
	}

	public void setSolicitacaoContratacao(
			com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao) {
		this.solicitacaoContratacao = solicitacaoContratacao;
	}

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdFuncAlteraStatus() {
        return this.usuarioByIdFuncAlteraStatus;
    }

	public void setUsuarioByIdFuncAlteraStatus(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncAlteraStatus) {
        this.usuarioByIdFuncAlteraStatus = usuarioByIdFuncAlteraStatus;
    }
	
    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PagtoProprietarioCc.class)     
    public List getPagtoProprietarioCcs() {
        return this.pagtoProprietarioCcs;
    }

    public void setPagtoProprietarioCcs(List pagtoProprietarioCcs) {
        this.pagtoProprietarioCcs = pagtoProprietarioCcs;
    }

	public String toString() {
		return new ToStringBuilder(this).append("idVeiculoControleCarga",
				getIdVeiculoControleCarga()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VeiculoControleCarga))
			return false;
        VeiculoControleCarga castOther = (VeiculoControleCarga) other;
		return new EqualsBuilder().append(this.getIdVeiculoControleCarga(),
				castOther.getIdVeiculoControleCarga()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdVeiculoControleCarga())
            .toHashCode();
    }

}
