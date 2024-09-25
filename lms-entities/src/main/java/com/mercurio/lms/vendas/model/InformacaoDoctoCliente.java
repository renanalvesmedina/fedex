package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author Hibernate CodeGenerator */
public class InformacaoDoctoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idInformacaoDoctoCliente;

	/** nullable persistent field */
	private DomainValue tpModal;

	/** nullable persistent field */
	private DomainValue tpAbrangencia;

	/** persistent field */
	private String dsCampo;

	/** persistent field */
	private DomainValue tpCampo;

	/** nullable persistent field */
	private String dsFormatacao;

	/** nullable persistent field */
	private Integer nrTamanho;

	/** persistent field */
	private Boolean blOpcional;

	/** persistent field */
	private Boolean blImprimeConhecimento;

	/** persistent field */
	private Boolean blRemetente;

	/** persistent field */
	private Boolean blDestinatario;

	/** persistent field */
	private Boolean blDevedor;
	
	/** persistent field */
	private Boolean blIndicadorNotaFiscal;
	
	/** persistent field */
	private DomainValue tpSituacao;

	/** persistent field */
	private com.mercurio.lms.vendas.model.Cliente cliente;

	/** persistent field */
	private Set dominioAgrupamentos;

	/** nullable persistent field */
	private String dsValorPadrao;

	/** persistent field */
	private Boolean blValorFixo;

	public Long getIdInformacaoDoctoCliente() {
		return this.idInformacaoDoctoCliente;
	}

	public void setIdInformacaoDoctoCliente(Long idInformacaoDoctoCliente) {
		this.idInformacaoDoctoCliente = idInformacaoDoctoCliente;
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

	public String getDsCampo() {
		return this.dsCampo;
	}

	public void setDsCampo(String dsCampo) {
		this.dsCampo = dsCampo;
	}

	public DomainValue getTpCampo() {
		return this.tpCampo;
	}

	public void setTpCampo(DomainValue tpCampo) {
		this.tpCampo = tpCampo;
	}

	public String getDsFormatacao() {
		return this.dsFormatacao;
	}

	public void setDsFormatacao(String dsFormatacao) {
		this.dsFormatacao = dsFormatacao;
	}

	public Integer getNrTamanho() {
		return this.nrTamanho;
	}

	public void setNrTamanho(Integer nrTamanho) {
		this.nrTamanho = nrTamanho;
	}

	public Boolean getBlOpcional() {
		return this.blOpcional;
	}

	public void setBlOpcional(Boolean blOpcional) {
		this.blOpcional = blOpcional;
	}

	public Boolean getBlImprimeConhecimento() {
		return this.blImprimeConhecimento;
	}

	public void setBlImprimeConhecimento(Boolean blImprimeConhecimento) {
		this.blImprimeConhecimento = blImprimeConhecimento;
	}

	public Boolean getBlIndicadorNotaFiscal() {
		return this.blIndicadorNotaFiscal;
	}

	public void setBlIndicadorNotaFiscal(Boolean blIndicadorNotaFiscal) {
		this.blIndicadorNotaFiscal = blIndicadorNotaFiscal;
	}

	public Boolean getBlRemetente() {
		return this.blRemetente;
	}

	public void setBlRemetente(Boolean blRemetente) {
		this.blRemetente = blRemetente;
	}

	public Boolean getBlDestinatario() {
		return this.blDestinatario;
	}

	public void setBlDestinatario(Boolean blDestinatario) {
		this.blDestinatario = blDestinatario;
	}

	public Boolean getBlDevedor() {
		return this.blDevedor;
	}

	public void setBlDevedor(Boolean blDevedor) {
		this.blDevedor = blDevedor;
	}
	
	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DominioAgrupamento.class)	 
	public Set getDominioAgrupamentos() {
		return this.dominioAgrupamentos;
	}

	public void setDominioAgrupamentos(Set dominioAgrupamentos) {
		this.dominioAgrupamentos = dominioAgrupamentos;
	}

	/**
	 * @return Returns the tpSituacao.
	 */
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	/**
	 * @param tpSituacao
	 *            The tpSituacao to set.
	 */
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Boolean getBlValorFixo() {
		return blValorFixo;
	}

	public void setBlValorFixo(Boolean blValorFixo) {
		this.blValorFixo = blValorFixo;
	}

	public String getDsValorPadrao() {
		return dsValorPadrao;
	}

	public void setDsValorPadrao(String dsValorPadrao) {
		this.dsValorPadrao = dsValorPadrao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idInformacaoDctoCliente",
				getIdInformacaoDoctoCliente()).toString();
	}
}
