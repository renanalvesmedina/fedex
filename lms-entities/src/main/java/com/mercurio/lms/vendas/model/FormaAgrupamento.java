package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class FormaAgrupamento implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idFormaAgrupamento;

	/** persistent field */
	private VarcharI18n dsFormaAgrupamento;

	/** persistent field */
	private Boolean blAutomatico;

	/** persistent field */
	private DomainValue tpSituacao;

	/** identifier field */
	private Long nrOrdemPrioridade;

	private Long sqCorporativo;
	
	/** persistent field */
	private com.mercurio.lms.vendas.model.Cliente cliente;

	/** persistent field */
	private List dominioAgrupamentos;

	/** persistent field */
	private List agrupamentoClientes;

	public Long getIdFormaAgrupamento() {
		return this.idFormaAgrupamento;
	}

	public void setIdFormaAgrupamento(Long idFormaAgrupamento) {
		this.idFormaAgrupamento = idFormaAgrupamento;
	}

	public VarcharI18n getDsFormaAgrupamento() {
		return dsFormaAgrupamento;
	}

	public void setDsFormaAgrupamento(VarcharI18n dsFormaAgrupamento) {
		this.dsFormaAgrupamento = dsFormaAgrupamento;
	}

	public Boolean getBlAutomatico() {
		return this.blAutomatico;
	}

	public void setBlAutomatico(Boolean blAutomatico) {
		this.blAutomatico = blAutomatico;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Long getNrOrdemPrioridade() {
		return nrOrdemPrioridade;
	}

	public void setNrOrdemPrioridade(Long nrOrdemPrioridade) {
		this.nrOrdemPrioridade = nrOrdemPrioridade;
	}

	public Long getSqCorporativo() {
		return sqCorporativo;
	}

	public void setSqCorporativo(Long sqCorporativo) {
		this.sqCorporativo = sqCorporativo;
	}
	
	public com.mercurio.lms.vendas.model.Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
		this.cliente = cliente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DominioAgrupamento.class) 
	public List getDominioAgrupamentos() {
		return this.dominioAgrupamentos;
	}

	public void setDominioAgrupamentos(List dominioAgrupamentos) {
		this.dominioAgrupamentos = dominioAgrupamentos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.AgrupamentoCliente.class) 
	public List getAgrupamentoClientes() {
		return this.agrupamentoClientes;
	}

	public void setAgrupamentoClientes(List agrupamentoClientes) {
		this.agrupamentoClientes = agrupamentoClientes;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idFormaAgrupamento",
				getIdFormaAgrupamento()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FormaAgrupamento))
			return false;
		FormaAgrupamento castOther = (FormaAgrupamento) other;
		return new EqualsBuilder().append(this.getIdFormaAgrupamento(),
				castOther.getIdFormaAgrupamento()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdFormaAgrupamento())
			.toHashCode();
	}

}
