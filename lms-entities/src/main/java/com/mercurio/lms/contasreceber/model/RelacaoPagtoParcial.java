package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
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

import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.municipios.model.Filial;


@Entity
@Table(name="RELACAO_PAGTO_PARCIAL")
@SequenceGenerator(name = "SQ_RELACAO_PAGTO_PARCIAL", sequenceName = "RELACAO_PAGTO_PARCIAL_SQ")
public class RelacaoPagtoParcial implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_RELACAO_PAGTO_PARCIAL")
	@Column(name = "ID_RELACAO_PAGTO_PARCIAL", nullable = false)
	private Long idRelacaoPagtoParcial;
	
	@Column(name="NR_RELACAO", nullable = false)
	private String nrRelacao;
	
	@Column(name="NR_LOTE_CONTABIL_JDE")
	private String nrLoteContabilJDE;
	
	@Column(name="VL_PAGAMENTO", nullable = false)
	private BigDecimal vlPagamento;
	
	@Column(name="VL_JUROS")
	private BigDecimal vlJuros;
	
	@Column(name="VL_DESCONTO")
	private BigDecimal vlDesconto;
	
	@Column(name = "DT_PAGAMENTO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtPagamento;
	
	@Column(name="DT_ENVIO_CONTABILIDADE")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtEnvioContabilidade;
	
	@Column(name="OB_RELACAO")
	private String obRelacao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	private Filial filial;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FATURA", nullable = false)
	private Fatura fatura;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_BANCO", nullable = false)
	private Banco banco;
	

	public Long getIdRelacaoPagtoParcial() {
		return idRelacaoPagtoParcial;
	}

	public void setIdRelacaoPagtoParcial(Long idRelacaoPagtoParcial) {
		this.idRelacaoPagtoParcial = idRelacaoPagtoParcial;
	}

	public String getNrRelacao() {
		return nrRelacao;
	}

	public void setNrRelacao(String nrRelacao) {
		this.nrRelacao = nrRelacao;
	}

	public String getNrLoteContabilJDE() {
		return nrLoteContabilJDE;
	}

	public void setNrLoteContabilJDE(String nrLoteContabilJDE) {
		this.nrLoteContabilJDE = nrLoteContabilJDE;
	}

	public BigDecimal getVlPagamento() {
		return vlPagamento;
	}

	public void setVlPagamento(BigDecimal vlPagamento) {
		this.vlPagamento = vlPagamento;
	}

	public BigDecimal getVlJuros() {
		return vlJuros;
	}

	public void setVlJuros(BigDecimal vlJuros) {
		this.vlJuros = vlJuros;
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public YearMonthDay getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(YearMonthDay dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public YearMonthDay getDtEnvioContabilidade() {
		return dtEnvioContabilidade;
	}

	public void setDtEnvioContabilidade(YearMonthDay dtEnvioContabilidade) {
		this.dtEnvioContabilidade = dtEnvioContabilidade;
	}

	public String getObRelacao() {
		return obRelacao;
	}

	public void setObRelacao(String obRelacao) {
		this.obRelacao = obRelacao;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
	
	
}
