package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistoricoCheque implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHistoricoCheque;

    /** persistent field */
    private DateTime dhHistoricoCheque;

    /** persistent field */
    private DomainValue tpHistoricoCheque;

    /** nullable persistent field */
    private String obHistoricoCheque;

    /** persistent field */
    private DomainValue tpOperacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EmpresaCobranca empresaCobranca;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Cheque cheque;

    public Long getIdHistoricoCheque() {
        return this.idHistoricoCheque;
    }

    public void setIdHistoricoCheque(Long idHistoricoCheque) {
        this.idHistoricoCheque = idHistoricoCheque;
    }

    public DateTime getDhHistoricoCheque() {
        return this.dhHistoricoCheque;
    }

    public void setDhHistoricoCheque(DateTime dhHistoricoCheque) {
        this.dhHistoricoCheque = dhHistoricoCheque;
    }

    public DomainValue getTpHistoricoCheque() {
        return this.tpHistoricoCheque;
    }

    public void setTpHistoricoCheque(DomainValue tpHistoricoCheque) {
        this.tpHistoricoCheque = tpHistoricoCheque;
    }

    public String getObHistoricoCheque() {
        return this.obHistoricoCheque;
    }

    public void setObHistoricoCheque(String obHistoricoCheque) {
        this.obHistoricoCheque = obHistoricoCheque;
    }

    public com.mercurio.lms.configuracoes.model.EmpresaCobranca getEmpresaCobranca() {
        return this.empresaCobranca;
    }

	public void setEmpresaCobranca(
			com.mercurio.lms.configuracoes.model.EmpresaCobranca empresaCobranca) {
        this.empresaCobranca = empresaCobranca;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.contasreceber.model.Cheque getCheque() {
        return this.cheque;
    }

    public void setCheque(com.mercurio.lms.contasreceber.model.Cheque cheque) {
        this.cheque = cheque;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHistoricoCheque",
				getIdHistoricoCheque()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HistoricoCheque))
			return false;
        HistoricoCheque castOther = (HistoricoCheque) other;
		return new EqualsBuilder().append(this.getIdHistoricoCheque(),
				castOther.getIdHistoricoCheque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHistoricoCheque())
            .toHashCode();
    }

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

}
