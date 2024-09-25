package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class LiberacaoEmbarque implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLiberacaoEmbarque;

    /** nullable persistent field */
    private DomainValue tpModal;
    
    private Boolean blLiberaGrandeCapital;
    private Boolean blEfetivado;
    private com.mercurio.lms.workflow.model.Pendencia pendencia;
    private DomainValue tpSituacaoAprovacao;
    private com.mercurio.lms.configuracoes.model.Usuario usuarioAprovacao;
    private YearMonthDay dtAprovacao;
    private String dsMotivoSolicitacao;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    public Long getIdLiberacaoEmbarque() {
        return this.idLiberacaoEmbarque;
    }

    public void setIdLiberacaoEmbarque(Long idLiberacaoEmbarque) {
        this.idLiberacaoEmbarque = idLiberacaoEmbarque;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public Boolean getBlLiberaGrandeCapital() {
		return blLiberaGrandeCapital;
	}

	public void setBlLiberaGrandeCapital(Boolean blLiberaGrandeCapital) {
		this.blLiberaGrandeCapital = blLiberaGrandeCapital;
	}

	public Boolean getBlEfetivado() {
		return blEfetivado;
	}

	public void setBlEfetivado(Boolean blEfetivado) {
		this.blEfetivado = blEfetivado;
	}
	
	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioAprovacao() {
		return usuarioAprovacao;
	}

	public void setUsuarioAprovacao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioAprovacao) {
		this.usuarioAprovacao = usuarioAprovacao;
	}

	public YearMonthDay getDtAprovacao() {
		return dtAprovacao;
	}

	public void setDtAprovacao(YearMonthDay dtAprovacao) {
		this.dtAprovacao = dtAprovacao;
	}

	public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLiberacaoEmbarque",
				getIdLiberacaoEmbarque()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LiberacaoEmbarque))
			return false;
        LiberacaoEmbarque castOther = (LiberacaoEmbarque) other;
		return new EqualsBuilder().append(this.getIdLiberacaoEmbarque(),
				castOther.getIdLiberacaoEmbarque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLiberacaoEmbarque())
            .toHashCode();
    }

	public String getDsMotivoSolicitacao() {
		return dsMotivoSolicitacao;
	}

	public void setDsMotivoSolicitacao(String dsMotivoSolicitacao) {
		this.dsMotivoSolicitacao = dsMotivoSolicitacao;
	}

}
