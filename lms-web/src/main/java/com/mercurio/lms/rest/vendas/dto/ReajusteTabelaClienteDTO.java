package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class ReajusteTabelaClienteDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;

	private String numeroReajusteFilial;
	private String nrIdentificacao;
	private String sgFilial;
	private String nome;
	private String divisaoCliente;
	private String tabelaAtual;
	private String tabelaNova;
	private BigDecimal percSugerido;
	private BigDecimal percAcordado;
	private YearMonthDay dataReajuste;
	private Boolean efetivado;
	private YearMonthDay dataEfetivacao;
	private String situacaoWorkflow;
	private String justificativa;
	private Long idDivisaoCliente;
	private Long idTabDivisaoCliente;
	private Long nrReajuste;
	private Long idFilial;
	private Long idUsuario;
	private Long idCliente;
	private String nomeUsuario;
	private Long idTabelaNova;
	private String situacaoWorkflowDesc;
	private Long idPendeciaAprovacao;
	
	
	public YearMonthDay getDataReajuste() {
		return dataReajuste;
	}
	public void setDataReajuste(YearMonthDay dataReajuste) {
		this.dataReajuste = dataReajuste;
	}
	public String getNumeroReajuste() {
		return numeroReajusteFilial;
	}
	public void setNumeroReajuste(String numeroReajuste) {
		this.numeroReajusteFilial = numeroReajuste;
	}
	public String getNrIdentificacao() {
		return nrIdentificacao;
	}
	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDivisaoCliente() {
		return divisaoCliente;
	}
	public void setDivisaoCliente(String divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}
	public String getTabelaAtual() {
		return tabelaAtual;
	}
	public void setTabelaAtual(String tabelaAtual) {
		this.tabelaAtual = tabelaAtual;
	}
	public String getTabelaNova() {
		return tabelaNova;
	}
	public void setTabelaNova(String tabelaNova) {
		this.tabelaNova = tabelaNova;
	}
	public BigDecimal getPercSugerido() {
		return percSugerido;
	}
	public void setPercSugerido(BigDecimal percSugerido) {
		this.percSugerido = percSugerido;
	}
	public BigDecimal getPercAcordado() {
		return percAcordado;
	}
	public void setPercAcordado(BigDecimal percAcordado) {
		this.percAcordado = percAcordado;
	}	
	public Boolean getEfetivado() {
		return efetivado;
	}
	public void setEfetivado(Boolean efetivado) {
		this.efetivado = efetivado;
	}
	public YearMonthDay getDataEfetivacao() {
		return dataEfetivacao;
	}
	public void setDataEfetivacao(YearMonthDay dataEfetivacao) {
		this.dataEfetivacao = dataEfetivacao;
	}
	public String getSituacaoWorkflow() {
		return situacaoWorkflow;
	}
	public void setSituacaoWorkflow(String situacaoWorkflow) {
		this.situacaoWorkflow = situacaoWorkflow;
	}
	public String getJustificativa() {
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	public Long getIdDivisaoCliente() {
		return idDivisaoCliente;
	}
	public void setIdDivisaoCliente(Long idDivisaoCliente) {
		this.idDivisaoCliente = idDivisaoCliente;
	}
	public String getNumeroReajusteFilial() {
		return numeroReajusteFilial;
	}
	public void setNumeroReajusteFilial(String numeroReajusteFilial) {
		this.numeroReajusteFilial = numeroReajusteFilial;
	}
	public Long getIdTabDivisaoCliente() {
		return idTabDivisaoCliente;
	}
	public void setIdTabDivisaoCliente(Long idTabDivisaoCliente) {
		this.idTabDivisaoCliente = idTabDivisaoCliente;
	}
	public Long getNrReajuste() {
		return nrReajuste;
	}
	public void setNrReajuste(Long nrReajuste) {
		this.nrReajuste = nrReajuste;
	}
	public Long getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
	public Long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Long getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public Long getIdTabelaNova() {
		return idTabelaNova;
	}
	public void setIdTabelaNova(Long idTabelaNova) {
		this.idTabelaNova = idTabelaNova;
	}
	public String getSituacaoWorkflowDesc() {
		return situacaoWorkflowDesc;
	}
	public void setSituacaoWorkflowDesc(String situacaoWorkflowDesc) {
		this.situacaoWorkflowDesc = situacaoWorkflowDesc;
	}
	public Long getIdPendeciaAprovacao() {
		return idPendeciaAprovacao;
	}
	public void setIdPendeciaAprovacao(Long idPendeciaAprovacao) {
		this.idPendeciaAprovacao = idPendeciaAprovacao;
	}
}
