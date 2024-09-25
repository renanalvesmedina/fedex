package com.mercurio.lms.indenizacoes.model;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.workflow.model.Pendencia;

@Entity
@Table(name = "RECIBO_INDENIZACAO_FRQ")
@SequenceGenerator(name = "RECIBO_INDENIZACAO_FRQ_SEQ", sequenceName = "RECIBO_INDENIZACAO_FRQ_SQ", allocationSize = 1)
public class ReciboIndenizacaoFranqueado implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_RECIBO_INDENIZACAO_FRQ", unique = true, nullable = false, precision = 10, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIBO_INDENIZACAO_FRQ_SEQ")
	private Long idReciboIndenizacaoFrq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FRANQUIA", nullable = false)
	private Franquia franquia;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_RECIBO_INDENIZACAO", nullable = false)
	private ReciboIndenizacao reciboIndenizacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PENDENCIA")
	private Pendencia pendencia;
	
	@Column(name = "DT_COMPETENCIA", nullable = false, length = 7)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtCompetencia;
	
	@Column(name = "PC_INDENIZACAO", nullable = false, precision = 3)
	private BigDecimal nrPercentualIndenizacao;
	
	@Column(name="VL_INDENIZADO", nullable = false, precision = 18, scale=2)
	private BigDecimal nrValorIndenizado;
	
	@Column(name="NR_PARCELAS", nullable = false, precision = 2)
	private BigDecimal nrParcelas;
	
	@Column(name = "TP_SITUACAO_PENDENCIA", nullable = false, length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS_WORKFLOW") })
	private DomainValue tpSituacaoPendencia = new DomainValue("E");
	
	@Column(name = "TP_MOTIVO_INDENIZACAO ", nullable = false, length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_MOTIVO_INDENIZACAO_FRQ") })
	private DomainValue tpMotivoIndenizacao  = new DomainValue("AV");

	@Column(name = "DS_INDENIZACAO ", nullable = true, length = 500)
	private String dsIndenizacao;
	
	@Column(name = "DC_ARQUIVO", nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	private byte[] dcArquivo;
	
	@Transient
	private String dtCompetenciaAsMesAno;
	
	@Transient
	private BigDecimal nrValorPorParcela;
	
	@Transient
	private BigDecimal nrValorIndenizacaoReal;
	
	public ReciboIndenizacaoFranqueado() {
	}

	public Franquia getFranquia() {
		return franquia;
	}

	public Long getIdReciboIndenizacaoFrq() {
		return idReciboIndenizacaoFrq;
	}

	public void setIdReciboIndenizacaoFrq(Long idReciboIndenizacaoFrq) {
		this.idReciboIndenizacaoFrq = idReciboIndenizacaoFrq;
	}

	public void setFranquia(Franquia franquia) {
		this.franquia = franquia;
	}

	public ReciboIndenizacao getReciboIndenizacao() {
		return reciboIndenizacao;
	}

	public void setReciboIndenizacao(ReciboIndenizacao reciboIndenizacao) {
		this.reciboIndenizacao = reciboIndenizacao;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public YearMonthDay getDtCompetencia() {
		return dtCompetencia;
	}

	public void setDtCompetencia(YearMonthDay dtCompetencia) {
		this.dtCompetencia = dtCompetencia;
	}

	public BigDecimal getNrPercentualIndenizacao() {
		return nrPercentualIndenizacao;
	}

	public void setNrPercentualIndenizacao(BigDecimal nrPercentualIndenizacao) {
		this.nrPercentualIndenizacao = nrPercentualIndenizacao;
	}

	public BigDecimal getNrValorIndenizado() {
		return nrValorIndenizado;
	}

	public void setNrValorIndenizado(BigDecimal nrValorIndenizado) {
		this.nrValorIndenizado = nrValorIndenizado;
	}

	public DomainValue getTpSituacaoPendencia() {
		return tpSituacaoPendencia;
	}

	public void setTpSituacaoPendencia(DomainValue tpSituacaoPendencia) {
		this.tpSituacaoPendencia = tpSituacaoPendencia;
	}
	
	public DomainValue getTpMotivoIndenizacao() {
		return tpMotivoIndenizacao;
	}

	public void setTpMotivoIndenizacao(DomainValue tpMotivoIndenizacao) {
		this.tpMotivoIndenizacao = tpMotivoIndenizacao;
	}
	

	public BigDecimal getNrParcelas() {
		return nrParcelas;
	}

	public void setNrParcelas(BigDecimal nrParcelas) {
		this.nrParcelas = nrParcelas;
	}

	public String getDtCompetenciaAsMesAno() {
		return dtCompetenciaAsMesAno;
	}

	public void setDtCompetenciaAsMesAno(String dtCompetenciaAsMesAno) {
		this.dtCompetenciaAsMesAno = dtCompetenciaAsMesAno;
	}

	public BigDecimal getNrValorPorParcela() {
		return nrValorPorParcela;
	}

	public void setNrValorPorParcela(BigDecimal nrValorPorParcela) {
		this.nrValorPorParcela = nrValorPorParcela;
	}

	public BigDecimal getNrValorIndenizacaoReal() {
		return nrValorIndenizacaoReal;
	}

	public void setNrValorIndenizacaoReal(BigDecimal nrValorIndenizacaoReal) {
		this.nrValorIndenizacaoReal = nrValorIndenizacaoReal;
	}

	public String getDsIndenizacao() {
		return dsIndenizacao;
	}

	public void setDsIndenizacao(String dsIndenizacao) {
		this.dsIndenizacao = dsIndenizacao;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

}
