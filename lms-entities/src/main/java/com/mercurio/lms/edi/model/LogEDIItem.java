package com.mercurio.lms.edi.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "LOG_ARQUIVO_EDI_DETALHE_ITEM")
@Proxy(lazy=false)  
public class LogEDIItem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Long idItem;
	private LogEDIDetalhe logEDIDetalhe;
	private String codItem;
	private BigDecimal pesoRealItem;
	private BigDecimal pesoCubadoItem;
	private BigDecimal alturaItem;
	private BigDecimal larguraItem;
	private BigDecimal comprimentoItem;
	private BigDecimal valorItem;
	
	@Column(name = "NUMERO_ITEM")
	private Integer numeroItem;
	
	@Column(name = "DESCRICAO_ITEM", length = 500)
	private String descricaoItem;
	
	@Column(name = "EAN_ITEM")
	private Long eanItem;

	@Column(name = "NCM_ITEM")
	private Long ncmItem;
	
	@Column(name = "CFOP_ITEM", length = 10)
	private String cfopItem;
	
	@Column(name = "UNIDADE_ITEM", length = 10)
	private String unidadeItem;
	
	@Column(name = "QTDE_ITEM")
	private BigDecimal qtdeItem;
	
	@Column(name = "VALOR_UNID_ITEM")
	private BigDecimal vlUnidadeItem;
	
	@Column(name = "VALOR_TOTAL_ITEM")
	private BigDecimal vlTotalItem;
	
	@Id
	@Column(name = "LOG_ARQUIVO_EDI_DETALHE_ITEM", nullable = false)
	public Long getIdItem() {
		return idItem;
	}

	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOADID_LOG_ARQUIVO_EDI_DETALHE", nullable = false)
	public LogEDIDetalhe getLogEDIDetalhe() {
		return logEDIDetalhe;
	}

	public void setLogEDIDetalhe(LogEDIDetalhe logEDIDetalhe) {
		this.logEDIDetalhe = logEDIDetalhe;
	}

	@Column(name = "COD_ITEM", length = 30)
	public String getCodItem() {
		return this.codItem;
	}

	public void setCodItem(String codItem) {
		this.codItem = codItem;
	}

	@Column(name = "PESO_REAL_ITEM")
	public BigDecimal getPesoRealItem() {
		return this.pesoRealItem;
	}

	public void setPesoRealItem(BigDecimal pesoRealItem) {
		this.pesoRealItem = pesoRealItem;
	}

	@Column(name = "PESO_CUBADO_ITEM")
	public BigDecimal getPesoCubadoItem() {
		return this.pesoCubadoItem;
	}

	public void setPesoCubadoItem(BigDecimal pesoCubadoItem) {
		this.pesoCubadoItem = pesoCubadoItem;
	}

	@Column(name = "ALTURA_ITEM")
	public BigDecimal getAlturaItem() {
		return this.alturaItem;
	}

	public void setAlturaItem(BigDecimal alturaItem) {
		this.alturaItem = alturaItem;
	}

	@Column(name = "LARGURA_ITEM")
	public BigDecimal getLarguraItem() {
		return this.larguraItem;
	}

	public void setLarguraItem(BigDecimal larguraItem) {
		this.larguraItem = larguraItem;
	}

	@Column(name = "COMPRIMENTO_ITEM")
	public BigDecimal getComprimentoItem() {
		return this.comprimentoItem;
	}

	public void setComprimentoItem(BigDecimal comprimentoItem) {
		this.comprimentoItem = comprimentoItem;
	}

	@Column(name = "VALOR_ITEM")
	public BigDecimal getValorItem() {
		return this.valorItem;
	}

	public void setValorItem(BigDecimal valorItem) {
		this.valorItem = valorItem;
	}

	public Integer getNumeroItem() {
		return numeroItem;
	}

	public void setNumeroItem(Integer numeroItem) {
		this.numeroItem = numeroItem;
	}

	public String getDescricaoItem() {
		return descricaoItem;
	}

	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}

	public Long getEanItem() {
		return eanItem;
	}

	public void setEanItem(Long eanItem) {
		this.eanItem = eanItem;
	}

	public Long getNcmItem() {
		return ncmItem;
	}

	public void setNcmItem(Long ncmItem) {
		this.ncmItem = ncmItem;
	}

	public String getCfopItem() {
		return cfopItem;
	}

	public void setCfopItem(String cfopItem) {
		this.cfopItem = cfopItem;
	}
	public String getUnidadeItem() {
		return unidadeItem;
	}

	public void setUnidadeItem(String unidadeItem) {
		this.unidadeItem = unidadeItem;
	}

	public BigDecimal getQtdeItem() {
		return qtdeItem;
	}

	public void setQtdeItem(BigDecimal qtdeItem) {
		this.qtdeItem = qtdeItem;
	}

	public BigDecimal getVlUnidadeItem() {
		return vlUnidadeItem;
	}

	public void setVlUnidadeItem(BigDecimal vlUnidadeItem) {
		this.vlUnidadeItem = vlUnidadeItem;
	}

	public BigDecimal getVlTotalItem() {
		return vlTotalItem;
	}

	public void setVlTotalItem(BigDecimal vlTotalItem) {
		this.vlTotalItem = vlTotalItem;
	}
}