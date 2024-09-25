package com.mercurio.lms.contasreceber.model;

import java.util.Date;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.DoctoServico;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaDebitoNacional extends DoctoServico {

	private static final long serialVersionUID = 1L;

    /** persistent field */
    private Long nrNotaDebitoNac;
    
    /** persistent field */
    private DomainValue tpSituacaoNotaDebitoNac;

    /** persistent field */
    private DomainValue tpSituacaoCancelamento;

    /** nullable persistent field */
    private Date dhTransmissao;

    /** nullable persistent field */
    private String obNotaDebitoNac;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    private com.mercurio.lms.workflow.model.Pendencia pendencia;
    
    /** persistent field */
    private DomainValue tpSituacaoDesconto;

    /** persistent field */
    private List itemNotaDebitoNacionais;

    public Long getNrNotaDebitoNac() {
        return this.nrNotaDebitoNac;
    }

    public void setNrNotaDebitoNac(Long nrNotaDebitoNac) {
        this.nrNotaDebitoNac = nrNotaDebitoNac;
    }

    public DomainValue getTpSituacaoNotaDebitoNac() {
        return this.tpSituacaoNotaDebitoNac;
    }

    public void setTpSituacaoNotaDebitoNac(DomainValue tpSituacaoNotaDebitoNac) {
        this.tpSituacaoNotaDebitoNac = tpSituacaoNotaDebitoNac;
    }

    public DomainValue getTpSituacaoCancelamento() {
        return this.tpSituacaoCancelamento;
    }

    public void setTpSituacaoCancelamento(DomainValue tpSituacaoCancelamento) {
        this.tpSituacaoCancelamento = tpSituacaoCancelamento;
    }

    public Date getDhTransmissao() {
        return this.dhTransmissao;
    }

    public void setDhTransmissao(Date dhTransmissao) {
        this.dhTransmissao = dhTransmissao;
    }

    public String getObNotaDebitoNac() {
        return this.obNotaDebitoNac;
    }

    public void setObNotaDebitoNac(String obNotaDebitoNac) {
        this.obNotaDebitoNac = obNotaDebitoNac;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemNotaDebitoNacional.class)     
    public List getItemNotaDebitoNacionais() {
        return this.itemNotaDebitoNacionais;
    }

    public void setItemNotaDebitoNacionais(List itemNotaDebitoNacionais) {
        this.itemNotaDebitoNacionais = itemNotaDebitoNacionais;
    }

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public DomainValue getTpSituacaoDesconto() {
		return tpSituacaoDesconto;
	}

	public void setTpSituacaoDesconto(DomainValue tpSituacaoDesconto) {
		this.tpSituacaoDesconto = tpSituacaoDesconto;
	}
    
}