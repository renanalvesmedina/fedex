package com.mercurio.lms.prestcontasciaaerea.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.Awb;

@Entity
@Table(name = "ITEM_FATURA_CIA_AEREA")
@SequenceGenerator(name = "ITEM_FATURA_CIA_AEREA_SEQ", sequenceName = "ITEM_FATURA_CIA_AEREA_SQ", allocationSize = 1)
public class ItemFaturaCiaAerea implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_ITEM_FATURA_CIA_AEREA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_FATURA_CIA_AEREA_SEQ")
	private Long idItemFaturaCiaAerea;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FATURA_CIA_AEREA", nullable = false)
	private FaturaCiaAerea faturaCiaAerea;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AWB", nullable = false)
	private Awb awb;
	
	@Column(name = "PS_COBRADO", nullable = false)
	private BigDecimal psCobrado;
	
	@Column(name = "VL_COBRADO", nullable = false)
	private BigDecimal vlCobrado;
	
	@Column(name = "VL_DESCONTO", nullable = false)
	private BigDecimal vlDesconto;
	
	@Column(name = "VL_ACRESCIMO", nullable = false)
	private BigDecimal vlAcrescimo;
	
	@Transient
	private String diferencaFrete;
	
	@Transient
	private String dsSerie;
	
	@Transient
	private Long nrAwb;
	
	@Transient
	private Integer dvAwb;
	
	@Transient
	private String nrAwbFormatado;
	
	@Transient
	private DateTime dhEmissao;
	
	@Transient
	private DomainValue tpStatusAwb;
	
	@Transient
	private BigDecimal vlFrete;
	
	@Transient
	private BigDecimal vlDiferencaFrete;
	
	@Transient
	private BigDecimal vlFinal;
	
	@Transient
	private Integer qtVolumes;
	
	@Transient
	private BigDecimal psTotal;
	
	@Transient
	private BigDecimal psCubado;
	
	@Transient
	private String sgAeroportoOrigem;
	
	@Transient
	private String sgAeroportoDestino;
	
	public String toString() {
		return new ToStringBuilder(this).append("idItemFaturaCiaAerea", getIdItemFaturaCiaAerea()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemFaturaCiaAerea))
			return false;
		ItemFaturaCiaAerea castOther = (ItemFaturaCiaAerea) other;
		return new EqualsBuilder().append(this.getIdItemFaturaCiaAerea(), castOther.getIdItemFaturaCiaAerea()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdItemFaturaCiaAerea()).toHashCode();
	}

	public Long getIdItemFaturaCiaAerea() {
		return idItemFaturaCiaAerea;
	}

	public void setIdItemFaturaCiaAerea(Long idItemFaturaCiaAerea) {
		this.idItemFaturaCiaAerea = idItemFaturaCiaAerea;
	}

	public FaturaCiaAerea getFaturaCiaAerea() {
		return faturaCiaAerea;
	}

	public void setFaturaCiaAerea(FaturaCiaAerea faturaCiaAerea) {
		this.faturaCiaAerea = faturaCiaAerea;
	}

	public Awb getAwb() {
		return awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}

	public BigDecimal getPsCobrado() {
		return psCobrado;
	}

	public void setPsCobrado(BigDecimal psCobrado) {
		this.psCobrado = psCobrado;
	}

	public BigDecimal getVlCobrado() {
		return vlCobrado;
	}

	public void setVlCobrado(BigDecimal vlCobrado) {
		this.vlCobrado = vlCobrado;
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public BigDecimal getVlAcrescimo() {
		return vlAcrescimo;
	}

	public void setVlAcrescimo(BigDecimal vlAcrescimo) {
		this.vlAcrescimo = vlAcrescimo;
	}

	public String getDsSerie() {
		return dsSerie;
	}

	public void setDsSerie(String dsSerie) {
		this.dsSerie = dsSerie;
	}

	public Long getNrAwb() {
		return nrAwb;
	}

	public void setNrAwb(Long nrAwb) {
		this.nrAwb = nrAwb;
	}

	public Integer getDvAwb() {
		return dvAwb;
	}

	public void setDvAwb(Integer dvAwb) {
		this.dvAwb = dvAwb;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public DomainValue getTpStatusAwb() {
		return tpStatusAwb;
	}

	public void setTpStatusAwb(DomainValue tpStatusAwb) {
		this.tpStatusAwb = tpStatusAwb;
	}

	public BigDecimal getVlFrete() {
		return vlFrete;
	}

	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}

	public BigDecimal getVlDiferencaFrete() {
		return vlDiferencaFrete;
	}

	public void setVlDiferencaFrete(BigDecimal vlDiferencaFrete) {
		this.vlDiferencaFrete = vlDiferencaFrete;
	}

	public Integer getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public BigDecimal getPsTotal() {
		return psTotal;
	}

	public void setPsTotal(BigDecimal psTotal) {
		this.psTotal = psTotal;
	}

	public BigDecimal getPsCubado() {
		return psCubado;
	}

	public void setPsCubado(BigDecimal psCubado) {
		this.psCubado = psCubado;
	}

	public String getSgAeroportoOrigem() {
		return sgAeroportoOrigem;
	}

	public void setSgAeroportoOrigem(String sgAeroportoOrigem) {
		this.sgAeroportoOrigem = sgAeroportoOrigem;
	}

	public String getSgAeroportoDestino() {
		return sgAeroportoDestino;
	}

	public void setSgAeroportoDestino(String sgAeroportoDestino) {
		this.sgAeroportoDestino = sgAeroportoDestino;
	}

	public BigDecimal getVlFinal() {
		return vlFinal;
	}

	public void setVlFinal(BigDecimal vlFinal) {
		this.vlFinal = vlFinal;
	}

	public String getNrAwbFormatado() {
		return nrAwbFormatado;
	}

	public void setNrAwbFormatado(String nrAwbFormatado) {
		this.nrAwbFormatado = nrAwbFormatado;
	}

	public String getDiferencaFrete() {
		return diferencaFrete;
	}

	public void setDiferencaFrete(String diferencaFrete) {
		this.diferencaFrete = diferencaFrete;
	}
}
