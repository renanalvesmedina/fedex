package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.expedicao.model.DivisaoClienteNaturezaProduto;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;

/** @author LMS Custom Hibernate CodeGenerator */
public class DivisaoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idDivisaoCliente;

	/** persistent field */
	private Long cdDivisaoCliente;

	/** persistent field */
	private String dsDivisaoCliente;

	/** nullable persistent field */
	private Integer nrQtdeDocsRomaneio;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private DomainValue tpSituacaoSolicitada;

	/** transient field **/
	private String dsMotivoSolicitacao;

	/** transient field **/
	private Boolean temPendenciaSituacao;
	
	/** persistent field */
	private List simulacoes;

	/** persistent field */
	private List diaFaturamentos;

	/** persistent field */
	private List doctoServicos;

	/** persistent field */
	private List divisaoProdutos;

	/** persistent field */
	private List tabelaDivisaoClientes;

	/** persistent field */
	private List agrupamentoClientes;
	
	/** persistent field */
	private List prazoVencimentos;
	
	/** persistent field */
	private List faturas;
	
	/** persistent field */
	private List devedorDocServFats;	

	/** persistent field */
	private NaturezaProduto naturezaProduto;

	/** persistent field */
	private List<DivisaoClienteNaturezaProduto> divisaoClienteNaturezaProdutos;

	public Long getIdDivisaoCliente() {
		return this.idDivisaoCliente;
	}

	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}

	public Long getCdDivisaoCliente() {
		return this.cdDivisaoCliente;
	}

	public void setCdDivisaoCliente(Long cdDivisaoCliente) {
		this.cdDivisaoCliente = cdDivisaoCliente;
	}

	public String getDsDivisaoCliente() {
		return this.dsDivisaoCliente;
	}

	public void setDsDivisaoCliente(String dsDivisaoCliente) {
		this.dsDivisaoCliente = dsDivisaoCliente;
	}

	public Integer getNrQtdeDocsRomaneio() {
		return this.nrQtdeDocsRomaneio;
	}

	public void setNrQtdeDocsRomaneio(Integer nrQtdeDocsRomaneio) {
		this.nrQtdeDocsRomaneio = nrQtdeDocsRomaneio;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpSituacaoSolicitada() {
		return tpSituacaoSolicitada;
	}

	public void setTpSituacaoSolicitada(DomainValue tpSituacaoSolicitada) {
		this.tpSituacaoSolicitada = tpSituacaoSolicitada;
	}

	@ParametrizedAttribute(type = Simulacao.class) 
	public List getSimulacoes() {
		return this.simulacoes;
	}

	public void setSimulacoes(List simulacoes) {
		this.simulacoes = simulacoes;
	}

	@ParametrizedAttribute(type = DiaFaturamento.class) 
	public List getDiaFaturamentos() {
		return this.diaFaturamentos;
	}

	public void setDiaFaturamentos(List diaFaturamentos) {
		this.diaFaturamentos = diaFaturamentos;
	}

	@ParametrizedAttribute(type = DoctoServico.class) 
	public List getDoctoServicos() {
		return this.doctoServicos;
	}

	public void setDoctoServicos(List doctoServicos) {
		this.doctoServicos = doctoServicos;
	}

	@ParametrizedAttribute(type = DivisaoProduto.class) 
	public List getDivisaoProdutos() {
		return this.divisaoProdutos;
	}

	public void setDivisaoProdutos(List divisaoProdutos) {
		this.divisaoProdutos = divisaoProdutos;
	}

	@ParametrizedAttribute(type = TabelaDivisaoCliente.class) 
	public List getTabelaDivisaoClientes() {
		return this.tabelaDivisaoClientes;
	}

	public void setTabelaDivisaoClientes(List tabelaDivisaoClientes) {
		this.tabelaDivisaoClientes = tabelaDivisaoClientes;
	}

	@ParametrizedAttribute(type = AgrupamentoCliente.class) 
	public List getAgrupamentoClientes() {
		return this.agrupamentoClientes;
	}

	public void setAgrupamentoClientes(List agrupamentoClientes) {
		this.agrupamentoClientes = agrupamentoClientes;
	}

	@ParametrizedAttribute(type = PrazoVencimento.class) 
	public List getPrazoVencimentos() {
		return prazoVencimentos;
	}

	public void setPrazoVencimentos(List prazoVencimentos) {
		this.prazoVencimentos = prazoVencimentos;
	}

	@ParametrizedAttribute(type = Fatura.class) 
	public List getFaturas() {
		return this.faturas;
	}

	public void setFaturas(List faturas) {
		this.faturas = faturas;
	}

	public List<DivisaoClienteNaturezaProduto> getDivisaoClienteNaturezaProdutos() {
		return divisaoClienteNaturezaProdutos;
	}

	public void setDivisaoClienteNaturezaProdutos(
			List<DivisaoClienteNaturezaProduto> divisaoClienteNaturezaProdutos) {
		this.divisaoClienteNaturezaProdutos = divisaoClienteNaturezaProdutos;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idDivisaoCliente",
				getIdDivisaoCliente()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DivisaoCliente))
			return false;
		DivisaoCliente castOther = (DivisaoCliente) other;
		return new EqualsBuilder().append(this.getIdDivisaoCliente(),
				castOther.getIdDivisaoCliente()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdDivisaoCliente()).toHashCode();
	}

	public List getDevedorDocServFats() {
		return devedorDocServFats;
	}

	public void setDevedorDocServFats(List devedorDocServFats) {
		this.devedorDocServFats = devedorDocServFats;
	}

	public NaturezaProduto getNaturezaProduto() {
		return naturezaProduto;
	}

	public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}

	public String getDsMotivoSolicitacao() {
		return dsMotivoSolicitacao;
	}

	public void setDsMotivoSolicitacao(String dsMotivoSolicitacao) {
		this.dsMotivoSolicitacao = dsMotivoSolicitacao;
	}

	public Boolean getTemPendenciaSituacao() {
		return temPendenciaSituacao;
	}

	public void setTemPendenciaSituacao(Boolean temPendenciaSituacao) {
		this.temPendenciaSituacao = temPendenciaSituacao;
	}
}
