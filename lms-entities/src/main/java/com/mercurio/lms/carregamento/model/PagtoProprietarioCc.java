package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class PagtoProprietarioCc implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPagtoProprietarioCc;

    /** nullable persistent field */
    private BigDecimal vlPagamento;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.VeiculoControleCarga veiculoControleCarga;
    
    public Long getIdPagtoProprietarioCc() {
        return this.idPagtoProprietarioCc;
    }

    public void setIdPagtoProprietarioCc(Long idPagtoProprietarioCc) {
        this.idPagtoProprietarioCc = idPagtoProprietarioCc;
    }

    public BigDecimal getVlPagamento() {
        return this.vlPagamento;
    }

    public void setVlPagamento(BigDecimal vlPagamento) {
        this.vlPagamento = vlPagamento;
    }

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Proprietario getProprietario() {
        return this.proprietario;
    }

	public void setProprietario(
			com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public com.mercurio.lms.carregamento.model.VeiculoControleCarga getVeiculoControleCarga() {
		return veiculoControleCarga;
	}

	public void setVeiculoControleCarga(
			com.mercurio.lms.carregamento.model.VeiculoControleCarga veiculoControleCarga) {
		this.veiculoControleCarga = veiculoControleCarga;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idPagtoProprietarioCc",
				getIdPagtoProprietarioCc()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PagtoProprietarioCc))
			return false;
        PagtoProprietarioCc castOther = (PagtoProprietarioCc) other;
		return new EqualsBuilder().append(this.getIdPagtoProprietarioCc(),
				castOther.getIdPagtoProprietarioCc()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPagtoProprietarioCc())
            .toHashCode();
    }

}
