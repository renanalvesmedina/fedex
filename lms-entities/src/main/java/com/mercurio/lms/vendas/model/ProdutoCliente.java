package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.Embalagem;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ProdutoCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idProdutoCliente;

	/** nullable persistent field */
	private BigDecimal psReal;

	/** nullable persistent field */
	private BigDecimal psAforado;

	/** nullable persistent field */
	private Boolean blPesoAforado;

	/** nullable persistent field */
	private BigDecimal vlMedioProdutoKilo;

	/** nullable persistent field */
	private String dsTipoIdentificacaoVolumes;

	/** nullable persistent field */
	private String dsTipoClassificacao;

	/** persistent field */
	private com.mercurio.lms.vendas.model.Cliente cliente;

	/** persistent field */
	private com.mercurio.lms.expedicao.model.Produto produto;

	/** persistent field */
	private com.mercurio.lms.configuracoes.model.Moeda moeda;
	
	/** nullable persistent field */
	private BigDecimal psMedioVolume;
	
	/** nullable persistent field */
	private BigDecimal psMedioDespacho;
	
	/** nullable persistent field */
	private Integer nrMedioVolumesDespacho;
		
	/** persistent field */
	private Embalagem embalagem;	
	
	/** persistent field */
	private DomainValue situacaoAprovacao;
	
	/** persistent field */
	private Pendencia pendencia;
	
	public Long getIdProdutoCliente() {
		return this.idProdutoCliente;
	}

	public void setIdProdutoCliente(Long idProdutoCliente) {
		this.idProdutoCliente = idProdutoCliente;
	}

	public BigDecimal getPsReal() {
		return psReal;
	}

	public void setPsReal(BigDecimal psReal) {
		this.psReal = psReal;
	}

	public BigDecimal getPsAforado() {
		return this.psAforado;
	}

	public void setPsAforado(BigDecimal psAforado) {
		this.psAforado = psAforado;
	}

	public Boolean getBlPesoAforado() {
		return blPesoAforado;
	}

	public void setBlPesoAforado(Boolean blPesoAforado) {
		this.blPesoAforado = blPesoAforado;
	}

	public BigDecimal getVlMedioProdutoKilo() {
		return this.vlMedioProdutoKilo;
	}

	public void setVlMedioProdutoKilo(BigDecimal vlMedioProdutoKilo) {
		this.vlMedioProdutoKilo = vlMedioProdutoKilo;
	}

	public String getDsTipoIdentificacaoVolumes() {
		return this.dsTipoIdentificacaoVolumes;
	}

	public void setDsTipoIdentificacaoVolumes(String dsTipoIdentificacaoVolumes) {
		this.dsTipoIdentificacaoVolumes = dsTipoIdentificacaoVolumes;
	}

	public String getDsTipoClassificacao() {
		return this.dsTipoClassificacao;
	}

	public void setDsTipoClassificacao(String dsTipoClassificacao) {
		this.dsTipoClassificacao = dsTipoClassificacao;
	}

	public com.mercurio.lms.vendas.model.Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
		this.cliente = cliente;
	}

	public com.mercurio.lms.expedicao.model.Produto getProduto() {
		return this.produto;
	}

	public void setProduto(com.mercurio.lms.expedicao.model.Produto produto) {
		this.produto = produto;
	}

	public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
		return this.moeda;
	}

	public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
		this.moeda = moeda;
	}

	public Embalagem getEmbalagem() {
		return embalagem;
	}

	public void setEmbalagem(Embalagem embalagem) {
		this.embalagem = embalagem;
	}

	/**
	 * Retorna o Valor médio formatado com a seguinte máscara : Símbolo da Moeda
	 * ###,###,###,###,##0.00 ex. R$ 1.000,00
	 * 
	 * @return
	 * @deprecated Utilizar FormatUtils
	 */
	public String getValorMedioFormatado(){
		String retorno = null;
		DecimalFormat df = new DecimalFormat("###,###,###,###,##0.00",
				new DecimalFormatSymbols(LocaleContextHolder.getLocale()));

		if(this.getVlMedioProdutoKilo() != null){
			if (this.getMoeda() != null
					&& this.getMoeda().getDsSimbolo() != null) {
				retorno = this.getMoeda().getDsSimbolo() + " "
						+ df.format(this.getVlMedioProdutoKilo());
			} else {
				retorno = this.getVlMedioProdutoKilo() != null ? df.format(this
						.getVlMedioProdutoKilo().toString()) : null;
			}
		}
		
		return retorno;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idProdutoCliente",
				getIdProdutoCliente()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ProdutoCliente))
			return false;
		ProdutoCliente castOther = (ProdutoCliente) other;
		return new EqualsBuilder().append(this.getIdProdutoCliente(),
				castOther.getIdProdutoCliente()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdProdutoCliente()).toHashCode();
	}

	/**
	 * @return the psMedioVolume
	 */
	public BigDecimal getPsMedioVolume() {
		return psMedioVolume;
	}

	/**
	 * @param psMedioVolume
	 *            the psMedioVolume to set
	 */
	public void setPsMedioVolume(BigDecimal psMedioVolume) {
		this.psMedioVolume = psMedioVolume;
	}

	/**
	 * @return the psMedioDespacho
	 */
	public BigDecimal getPsMedioDespacho() {
		return psMedioDespacho;
	}

	/**
	 * @param psMedioDespacho
	 *            the psMedioDespacho to set
	 */
	public void setPsMedioDespacho(BigDecimal psMedioDespacho) {
		this.psMedioDespacho = psMedioDespacho;
	}

	/**
	 * @return the nrMedioVolumesDespacho
	 */
	public Integer getNrMedioVolumesDespacho() {
		return nrMedioVolumesDespacho;
	}

	/**
	 * @param nrMedioVolumesDespacho
	 *            the nrMedioVolumesDespacho to set
	 */
	public void setNrMedioVolumesDespacho(Integer nrMedioVolumesDespacho) {
		this.nrMedioVolumesDespacho = nrMedioVolumesDespacho;
	}
	
	public DomainValue getSituacaoAprovacao() {
		return situacaoAprovacao;
	}
	
	public void setSituacaoAprovacao(DomainValue situacaoAprovacao) {
		this.situacaoAprovacao = situacaoAprovacao;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}
}