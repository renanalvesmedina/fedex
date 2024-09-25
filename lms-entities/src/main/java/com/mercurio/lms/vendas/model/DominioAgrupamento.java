package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class DominioAgrupamento implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDominioAgrupamento;

    /** persistent field */
    private DomainValue tpCampo;

    /** persistent field */
    private Byte nrOrdemPrioridade;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.InformacaoDocServico informacaoDocServico;

    /** persistent field */
    private com.mercurio.lms.vendas.model.FormaAgrupamento formaAgrupamento;

    /** persistent field */
    private com.mercurio.lms.vendas.model.InformacaoDoctoCliente informacaoDoctoCliente;

    public Long getIdDominioAgrupamento() {
        return this.idDominioAgrupamento;
    }

    public void setIdDominioAgrupamento(Long idDominioAgrupamento) {
        this.idDominioAgrupamento = idDominioAgrupamento;
    }

    public DomainValue getTpCampo() {
        return this.tpCampo;
    }

    public void setTpCampo(DomainValue tpCampo) {
        this.tpCampo = tpCampo;
    }

    public Byte getNrOrdemPrioridade() {
        return this.nrOrdemPrioridade;
    }

    public void setNrOrdemPrioridade(Byte nrOrdemPrioridade) {
        this.nrOrdemPrioridade = nrOrdemPrioridade;
    }

    public com.mercurio.lms.expedicao.model.InformacaoDocServico getInformacaoDocServico() {
        return this.informacaoDocServico;
    }

	public void setInformacaoDocServico(
			com.mercurio.lms.expedicao.model.InformacaoDocServico informacaoDocServico) {
        this.informacaoDocServico = informacaoDocServico;
    }

    public com.mercurio.lms.vendas.model.FormaAgrupamento getFormaAgrupamento() {
        return this.formaAgrupamento;
    }

	public void setFormaAgrupamento(
			com.mercurio.lms.vendas.model.FormaAgrupamento formaAgrupamento) {
        this.formaAgrupamento = formaAgrupamento;
    }

    public com.mercurio.lms.vendas.model.InformacaoDoctoCliente getInformacaoDoctoCliente() {
        return this.informacaoDoctoCliente;
    }

	public void setInformacaoDoctoCliente(
			com.mercurio.lms.vendas.model.InformacaoDoctoCliente informacaoDoctoCliente) {
        this.informacaoDoctoCliente = informacaoDoctoCliente;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDominioAgrupamento",
				getIdDominioAgrupamento()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DominioAgrupamento))
			return false;
        DominioAgrupamento castOther = (DominioAgrupamento) other;
		return new EqualsBuilder().append(this.getIdDominioAgrupamento(),
				castOther.getIdDominioAgrupamento()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDominioAgrupamento())
            .toHashCode();
    }

}
