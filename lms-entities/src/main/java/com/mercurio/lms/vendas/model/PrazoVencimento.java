package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class PrazoVencimento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPrazoVencimento;

    /** persistent field */
    private DomainValue tpFrete;
    
    /** persistent field */
    private DomainValue tpDiaSemana;

    /** nullable persistent field */
    private Short nrPrazoPagamento;

    /** nullable persistent field */
    private Short nrPrazoPagamentoSolicitado;

    /** nullable persistent field */
    private Short nrPrazoPagamentoAprovado;

    /** nullable persistent field */
    private DomainValue tpModal;

    /** nullable persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    private List diasVencimento;

    public Long getIdPrazoVencimento() {
        return this.idPrazoVencimento;
    }

    public void setIdPrazoVencimento(Long idPrazoVencimento) {
        this.idPrazoVencimento = idPrazoVencimento;
    }

    public DomainValue getTpFrete() {
        return this.tpFrete;
    }

    public void setTpFrete(DomainValue tpFrete) {
        this.tpFrete = tpFrete;
    }
    
    public DomainValue getTpDiaSemana() {
		return tpDiaSemana;
	}

	public void setTpDiaSemana(DomainValue tpDiaSemana) {
		this.tpDiaSemana = tpDiaSemana;
	}

	public Short getNrPrazoPagamento() {
        return this.nrPrazoPagamento;
    }

    public void setNrPrazoPagamento(Short nrPrazoPagamento) {
        this.nrPrazoPagamento = nrPrazoPagamento;
    }

    public Short getNrPrazoPagamentoSolicitado() {
		return nrPrazoPagamentoSolicitado;
	}

	public void setNrPrazoPagamentoSolicitado(Short nrPrazoPagamentoSolicitado) {
		this.nrPrazoPagamentoSolicitado = nrPrazoPagamentoSolicitado;
	}

	public Short getNrPrazoPagamentoAprovado() {
		return nrPrazoPagamentoAprovado;
	}

	public void setNrPrazoPagamentoAprovado(Short nrPrazoPagamentoAprovado) {
		this.nrPrazoPagamentoAprovado = nrPrazoPagamentoAprovado;
	}

	public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public com.mercurio.lms.vendas.model.DivisaoCliente getDivisaoCliente() {
        return this.divisaoCliente;
    }

	public void setDivisaoCliente(
			com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente) {
        this.divisaoCliente = divisaoCliente;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DiaVencimento.class)     
    public List getDiasVencimento() {
		return diasVencimento;
	}

	public void setDiasVencimento(List diasVencimento) {
		this.diasVencimento = diasVencimento;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idPrazoVencimento",
				getIdPrazoVencimento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PrazoVencimento))
			return false;
        PrazoVencimento castOther = (PrazoVencimento) other;
		return new EqualsBuilder().append(this.getIdPrazoVencimento(),
				castOther.getIdPrazoVencimento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPrazoVencimento())
            .toHashCode();
    }

}
