package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

@Entity
@Table(name = "RETORNO_BANCO")
@SequenceGenerator(name = "RETORNO_BANCO_SEQ", sequenceName = "RETORNO_BANCO_SQ", allocationSize=1)
public class RetornoBanco implements Serializable {

	private static final long serialVersionUID = 4191874761404556219L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RETORNO_BANCO_SEQ")
	@Column(name = "ID_RETORNO_BANCO", nullable = false)
	private Long idRetornoBanco;

	@ManyToOne
	@JoinColumn(name = "ID_BOLETO", nullable = true)
	private Boleto boleto;
	
	@Column(name = "NR_BOLETO", nullable = true)
	private String nrBoleto;

	@Column(name = "NR_BANCO", nullable = true)
	private Long nrBanco;
	
	@Column(name = "DT_MOVIMENTO", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtMovimento;
	
	@Column(name = "VL_TOTAL", nullable = false)
	private BigDecimal vlTotal;
	
	@Column(name = "VL_ABATIMENTO", nullable = false)
	private BigDecimal vlAbatimento;
	
	@Column(name = "VL_DESCONTO", nullable = false)
	private BigDecimal vlDesconto;
	
	@Column(name = "VL_JUROS", nullable = false)
	private BigDecimal vlJuros;

	@Column(name = "NR_OCORRENCIA", nullable = true)
	private String nrOcorrencia;

	@Column(name = "NR_MOTIVO_REJEICAO", nullable = true)
	private String nrMotivoRejeicao;
	
	@Column(name = "DS_RETORNO_BANCO", nullable = true)
	private String dsRetornoBanco;

	@Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = true), @Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = true), @Column(name = "DH_ALTERACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;
	
	@Column(name = "DS_LINHA", nullable = true)
	private String dsLinha;
	
	@Transient
	private String descricaoOcorrencia;
	
	@Transient
	private String descricaoMotivo;

	public Long getIdRetornoBanco() {
		return idRetornoBanco;
	}

	public void setIdRetornoBanco(Long idRetornoBanco) {
		this.idRetornoBanco = idRetornoBanco;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public String getNrBoleto() {
		return nrBoleto;
	}

	public void setNrBoleto(String nrBoleto) {
		this.nrBoleto = nrBoleto;
	}

	public Long getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(Long nrBanco) {
		this.nrBanco = nrBanco;
	}

	public YearMonthDay getDtMovimento() {
		return dtMovimento;
	}

	public void setDtMovimento(YearMonthDay dtMovimento) {
		this.dtMovimento = dtMovimento;
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}

	public BigDecimal getVlAbatimento() {
		return vlAbatimento;
	}

	public void setVlAbatimento(BigDecimal vlAbatimento) {
		this.vlAbatimento = vlAbatimento;
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public BigDecimal getVlJuros() {
		return vlJuros;
	}

	public void setVlJuros(BigDecimal vlJuros) {
		this.vlJuros = vlJuros;
	}

	public String getNrOcorrencia() {
		return nrOcorrencia;
	}

	public void setNrOcorrencia(String nrOcorrencia) {
		this.nrOcorrencia = nrOcorrencia;
	}

	public String getNrMotivoRejeicao() {
		return nrMotivoRejeicao;
	}

	public void setNrMotivoRejeicao(String nrMotivoRejeicao) {
		this.nrMotivoRejeicao = nrMotivoRejeicao;
	}

	public String getDsRetornoBanco() {
		return dsRetornoBanco;
	}

	public void setDsRetornoBanco(String dsRetornoBanco) {
		this.dsRetornoBanco = dsRetornoBanco;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}
	
	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idRetornoBanco == null) ? 0 : idRetornoBanco.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RetornoBanco other = (RetornoBanco) obj;
		if (idRetornoBanco == null) {
			if (other.idRetornoBanco != null)
				return false;
		} else if (!idRetornoBanco.equals(other.idRetornoBanco))
			return false;
		return true;
	}
	
	public String getDescricaoOcorrencia() {
		return descricaoOcorrencia;
	}
	public void setDescricaoOcorrencia(String descricaoOcorrencia) {
		this.descricaoOcorrencia = descricaoOcorrencia;
	}

	public void setDescricaoMotivo(String descricaoMotivo) {
		this.descricaoMotivo = descricaoMotivo;
	}
	public String getDescricaoMotivo() {
		return descricaoMotivo;
	}

	public String getDsLinha() {
		return dsLinha;
	}

	public void setDsLinha(String dsLinha) {
		this.dsLinha = dsLinha;
	}
}
