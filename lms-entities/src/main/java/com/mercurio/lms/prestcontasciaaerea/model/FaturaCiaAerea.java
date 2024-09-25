package com.mercurio.lms.prestcontasciaaerea.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.municipios.model.Empresa;

@Entity
@Table(name = "FATURA_CIA_AEREA")
@SequenceGenerator(name = "FATURA_CIA_AEREA_SEQ", sequenceName = "FATURA_CIA_AEREA_SQ", allocationSize = 1)
public class FaturaCiaAerea implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FATURA_CIA_AEREA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FATURA_CIA_AEREA_SEQ")
	private Long idFaturaCiaAerea;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CIA_AEREA", nullable = false)
	private Empresa ciaAerea;
	
	@Column(name="NR_FATURA_CIA_AEREA", nullable = false)
	private Long nrFaturaCiaAerea;	
	
	@Column(name = "DT_EMISSAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtEmissao;
	
	@Column(name = "DT_VENCIMENTO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVencimento;
	
	@Column(name = "DT_PERIODO_INICIAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtPeriodoInicial;
	
	@Column(name = "DT_PERIODO_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtPeriodoFinal;
	
	@Column(name = "VL_DESCONTO", nullable = false)
	private BigDecimal vlDesconto;
	
	@Column(name = "VL_ACRESCIMO", nullable = false)
	private BigDecimal vlAcrescimo;
	
	@Column(name = "OB_FATURA", length = 500)
	private String obFatura;
	
	@Column(name = "DT_ENVIO_JDE")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtEnvioJDE;
	
	@Column(name = "DT_PAGAMENTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtPagamento;
	
	@Column(name = "DT_INCLUSAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtInclusao;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "faturaCiaAerea")
    private List<ItemFaturaCiaAerea> itensFaturaCiaAerea;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "faturaCiaAerea")
    private List<FaturaCiaAereaAnexo> faturaCiaAereaAnexos;
	
	@Transient
	private Boolean alteracaoCompleta;
	
	@Transient
	private BigDecimal vlSomaAwbs;
	
	@Transient
	private BigDecimal vlSomaDescontosAwbs;
	
	@Transient
	private BigDecimal vlSomaAcrescimosAwbs;
	
	@Transient
	private BigDecimal vlTotalFatura;
	
	public String toString() {
		return new ToStringBuilder(this).append("idFaturaCiaAerea", getIdFaturaCiaAerea()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaturaCiaAerea))
			return false;
		FaturaCiaAerea castOther = (FaturaCiaAerea) other;
		return new EqualsBuilder().append(this.getIdFaturaCiaAerea(), castOther.getIdFaturaCiaAerea()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdFaturaCiaAerea()).toHashCode();
	}

	public Long getIdFaturaCiaAerea() {
		return idFaturaCiaAerea;
	}

	public void setIdFaturaCiaAerea(Long idFaturaCiaAerea) {
		this.idFaturaCiaAerea = idFaturaCiaAerea;
	}

	public Empresa getCiaAerea() {
		return ciaAerea;
	}

	public void setCiaAerea(Empresa ciaAerea) {
		this.ciaAerea = ciaAerea;
	}

	public Long getNrFaturaCiaAerea() {
		return nrFaturaCiaAerea;
	}

	public void setNrFaturaCiaAerea(Long nrFaturaCiaAerea) {
		this.nrFaturaCiaAerea = nrFaturaCiaAerea;
	}

	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public YearMonthDay getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public YearMonthDay getDtPeriodoInicial() {
		return dtPeriodoInicial;
	}

	public void setDtPeriodoInicial(YearMonthDay dtPeriodoInicial) {
		this.dtPeriodoInicial = dtPeriodoInicial;
	}

	public YearMonthDay getDtPeriodoFinal() {
		return dtPeriodoFinal;
	}

	public void setDtPeriodoFinal(YearMonthDay dtPeriodoFinal) {
		this.dtPeriodoFinal = dtPeriodoFinal;
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

	public String getObFatura() {
		return obFatura;
	}

	public void setObFatura(String obFatura) {
		this.obFatura = obFatura;
	}

	public YearMonthDay getDtEnvioJDE() {
		return dtEnvioJDE;
	}

	public void setDtEnvioJDE(YearMonthDay dtEnvioJDE) {
		this.dtEnvioJDE = dtEnvioJDE;
	}

	public YearMonthDay getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(YearMonthDay dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public YearMonthDay getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(YearMonthDay dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	public List<ItemFaturaCiaAerea> getItensFaturaCiaAerea() {
		return itensFaturaCiaAerea;
	}

	public void setItensFaturaCiaAerea(List<ItemFaturaCiaAerea> itensFaturaCiaAerea) {
		this.itensFaturaCiaAerea = itensFaturaCiaAerea;
	}

	public List<FaturaCiaAereaAnexo> getFaturaCiaAereaAnexos() {
		return faturaCiaAereaAnexos;
	}

	public void setFaturaCiaAereaAnexos(List<FaturaCiaAereaAnexo> faturaCiaAereaAnexos) {
		this.faturaCiaAereaAnexos = faturaCiaAereaAnexos;
	}

	public BigDecimal getVlSomaAwbs() {
		return vlSomaAwbs;
	}

	public void setVlSomaAwbs(BigDecimal vlSomaAwbs) {
		this.vlSomaAwbs = vlSomaAwbs;
	}

	public BigDecimal getVlSomaDescontosAwbs() {
		return vlSomaDescontosAwbs;
	}

	public void setVlSomaDescontosAwbs(BigDecimal vlSomaDescontosAwbs) {
		this.vlSomaDescontosAwbs = vlSomaDescontosAwbs;
	}

	public BigDecimal getVlSomaAcrescimosAwbs() {
		return vlSomaAcrescimosAwbs;
	}

	public void setVlSomaAcrescimosAwbs(BigDecimal vlSomaAcrescimosAwbs) {
		this.vlSomaAcrescimosAwbs = vlSomaAcrescimosAwbs;
	}

	public BigDecimal getVlTotalFatura() {
		return vlTotalFatura;
	}

	public void setVlTotalFatura(BigDecimal vlTotalFatura) {
		this.vlTotalFatura = vlTotalFatura;
	}

	public Boolean getAlteracaoCompleta() {
		return alteracaoCompleta;
	}

	public void setAlteracaoCompleta(Boolean alteracaoCompleta) {
		this.alteracaoCompleta = alteracaoCompleta;
	}
}
