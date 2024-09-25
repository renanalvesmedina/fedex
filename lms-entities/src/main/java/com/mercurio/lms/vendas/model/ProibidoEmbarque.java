package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class ProibidoEmbarque implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idProibidoEmbarque;

    /** persistent field */
    private YearMonthDay dtBloqueio;

    /** nullable persistent field */
    private YearMonthDay dtDesbloqueio;

    /** nullable persistent field */
    private String dsDesbloqueio;

    /** nullable persistent field */
    private String dsBloqueio;
    
    /** transient field */
    private YearMonthDay dtBloqueioAnterior;

    /** persistent field */
    private Cliente cliente;

    /** persistent field */
    private Usuario usuarioByIdUsuarioDesbloqueio;

    /** persistent field */
    private Usuario usuarioByIdUsuarioBloqueio;

    /** persistent field */
    private MotivoProibidoEmbarque motivoProibidoEmbarque;

    public Long getIdProibidoEmbarque() {
        return this.idProibidoEmbarque;
    }

    public void setIdProibidoEmbarque(Long idProibidoEmbarque) {
        this.idProibidoEmbarque = idProibidoEmbarque;
    }

    public YearMonthDay getDtBloqueio() {
        return this.dtBloqueio;
    }

    public void setDtBloqueio(YearMonthDay dtBloqueio) {
        this.dtBloqueio = dtBloqueio;
    }

    public YearMonthDay getDtDesbloqueio() {
        return this.dtDesbloqueio;
    }

    public void setDtDesbloqueio(YearMonthDay dtDesbloqueio) {
        this.dtDesbloqueio = dtDesbloqueio;
    }

    public String getDsDesbloqueio() {
        return this.dsDesbloqueio;
    }

    public void setDsDesbloqueio(String dsDesbloqueio) {
        this.dsDesbloqueio = dsDesbloqueio;
    }

    public String getDsBloqueio() {
        return this.dsBloqueio;
    }

    public void setDsBloqueio(String dsBloqueio) {
        this.dsBloqueio = dsBloqueio;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuarioByIdUsuarioDesbloqueio() {
        return this.usuarioByIdUsuarioDesbloqueio;
    }

	public void setUsuarioByIdUsuarioDesbloqueio(
			Usuario usuarioByIdUsuarioDesbloqueio) {
        this.usuarioByIdUsuarioDesbloqueio = usuarioByIdUsuarioDesbloqueio;
    }

    public Usuario getUsuarioByIdUsuarioBloqueio() {
        return this.usuarioByIdUsuarioBloqueio;
    }

    public void setUsuarioByIdUsuarioBloqueio(Usuario usuarioByIdUsuarioBloqueio) {
        this.usuarioByIdUsuarioBloqueio = usuarioByIdUsuarioBloqueio;
    }

    public com.mercurio.lms.vendas.model.MotivoProibidoEmbarque getMotivoProibidoEmbarque() {
        return this.motivoProibidoEmbarque;
    }

	public void setMotivoProibidoEmbarque(
			com.mercurio.lms.vendas.model.MotivoProibidoEmbarque motivoProibidoEmbarque) {
        this.motivoProibidoEmbarque = motivoProibidoEmbarque;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idProibidoEmbarque",
				getIdProibidoEmbarque()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ProibidoEmbarque))
			return false;
        ProibidoEmbarque castOther = (ProibidoEmbarque) other;
		return new EqualsBuilder().append(this.getIdProibidoEmbarque(),
				castOther.getIdProibidoEmbarque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdProibidoEmbarque())
            .toHashCode();
    }

	public YearMonthDay getDtBloqueioAnterior() {
		return dtBloqueioAnterior;
	}

	public void setDtBloqueioAnterior(YearMonthDay dtBloqueioAnterior) {
		this.dtBloqueioAnterior = dtBloqueioAnterior;
	}

}
