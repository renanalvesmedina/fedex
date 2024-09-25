package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Servico;

/** @author LMS Custom Hibernate CodeGenerator */
public class DiaFaturamento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDiaFaturamento;

    /** persistent field */
    private DomainValue tpFrete;

    /** nullable persistent field */
    private Byte nrDiaFaturamento;

    /** nullable persistent field */
    private Byte nrDiaFaturamentoSolicitado;

    /** nullable persistent field */
    private DomainValue tpModal;

    /** nullable persistent field */
    private DomainValue tpAbrangencia;

    /** nullable persistent field */
    private DomainValue tpPeriodicidade;

    /** nullable persistent field */
    private DomainValue tpPeriodicidadeSolicitado;

    /** nullable persistent field */
    private DomainValue tpPeriodicidadeAprovado;

    /** nullable persistent field */
    private DomainValue tpDiaSemana;

    /** nullable persistent field */
    private DomainValue tpDiaSemanaSolicitado;

    /** persistent field */
    private com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente;
    
    private Servico servico;

    public Long getIdDiaFaturamento() {
        return this.idDiaFaturamento;
    }

    public void setIdDiaFaturamento(Long idDiaFaturamento) {
        this.idDiaFaturamento = idDiaFaturamento;
    }

    public DomainValue getTpFrete() {
        return this.tpFrete;
    }

    public void setTpFrete(DomainValue tpFrete) {
        this.tpFrete = tpFrete;
    }

    public Byte getNrDiaFaturamento() {
        return this.nrDiaFaturamento;
    }

    public void setNrDiaFaturamento(Byte nrDiaFaturamento) {
        this.nrDiaFaturamento = nrDiaFaturamento;
    }

    public Byte getNrDiaFaturamentoSolicitado() {
		return nrDiaFaturamentoSolicitado;
	}

	public void setNrDiaFaturamentoSolicitado(Byte nrDiaFaturamentoSolicitado) {
		this.nrDiaFaturamentoSolicitado = nrDiaFaturamentoSolicitado;
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

    public DomainValue getTpPeriodicidade() {
        return this.tpPeriodicidade;
    }

    public void setTpPeriodicidade(DomainValue tpPeriodicidade) {
        this.tpPeriodicidade = tpPeriodicidade;
    }

    public DomainValue getTpPeriodicidadeSolicitado() {
		return tpPeriodicidadeSolicitado;
	}

	public void setTpPeriodicidadeSolicitado(
			DomainValue tpPeriodicidadeSolicitado) {
		this.tpPeriodicidadeSolicitado = tpPeriodicidadeSolicitado;
	}

	public DomainValue getTpPeriodicidadeAprovado() {
		return tpPeriodicidadeAprovado;
	}

	public void setTpPeriodicidadeAprovado(DomainValue tpPeriodicidadeAprovado) {
		this.tpPeriodicidadeAprovado = tpPeriodicidadeAprovado;
	}

	public DomainValue getTpDiaSemana() {
        return this.tpDiaSemana;
    }

    public void setTpDiaSemana(DomainValue tpDiaSemana) {
        this.tpDiaSemana = tpDiaSemana;
    }

    public DomainValue getTpDiaSemanaSolicitado() {
		return tpDiaSemanaSolicitado;
	}

	public void setTpDiaSemanaSolicitado(DomainValue tpDiaSemanaSolicitado) {
		this.tpDiaSemanaSolicitado = tpDiaSemanaSolicitado;
	}

	public com.mercurio.lms.vendas.model.DivisaoCliente getDivisaoCliente() {
        return this.divisaoCliente;
    }

	public void setDivisaoCliente(
			com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente) {
        this.divisaoCliente = divisaoCliente;
    }

    public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idDiaFaturamento",
				getIdDiaFaturamento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DiaFaturamento))
			return false;
        DiaFaturamento castOther = (DiaFaturamento) other;
		return new EqualsBuilder().append(this.getIdDiaFaturamento(),
				castOther.getIdDiaFaturamento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDiaFaturamento()).toHashCode();
    }

    public String getDiaRefFaturamento(){
		if (tpPeriodicidade != null && "S".equals(tpPeriodicidade.getValue())
				&& tpDiaSemana != null) {
    		return tpDiaSemana.getDescription().getValue();
    	} else if (nrDiaFaturamento != null) {
    		return nrDiaFaturamento.toString(); 
    	}
    	return null;
    }
}
