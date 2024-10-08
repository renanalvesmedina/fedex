package com.mercurio.lms.franqueados.model;

//Generated 17/04/2014 14:27:46 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/**
 * ContaContabilFrq generated by hbm2java
 */
@Entity
@Table(name = "CONTA_CONTABIL_FRQ")
@SequenceGenerator(name = "CONTA_CONTABIL_FRQ_SEQ", sequenceName = "CONTA_CONTABIL_FRQ_SQ", allocationSize = 1)
public class ContaContabilFranqueado implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID_CONTA_CONTABIL_FRQ", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTA_CONTABIL_FRQ_SEQ")
	private long idContaContabilFrq;
	
	@Column(name = "DS_CONTA_CONTABIL", nullable = false, length = 200)
	private String dsContaContabil;
	
	@Column(name = "CD_CONTA_CONTABIL", nullable = false, length = 30)
	private String cdContaContabil;
	
	@Column(name = "BL_PERMITE_ALTERACAO", nullable = false, length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blPermiteAlteracao;
	
	@Column(name = "TP_LANCAMENTO", nullable = false, length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_LANCAMENTO_CC") })
	private DomainValue tpLancamento;
	
	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false, length = 7)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;
	
	@Column(name = "DT_VIGENCIA_FINAL", nullable = false, length = 7)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "contaContabilFranqueado")
	private Set<LancamentoFranqueado> lancamentoFranqueados = new HashSet<LancamentoFranqueado>();
	
	@Column(name = "TP_CONTA_CONTABIL", nullable = false, length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_CONTA_CONTABIL_FRQ") })
	private DomainValue tpContaContabil;

	public ContaContabilFranqueado() {
	}

	public long getIdContaContabilFrq() {
		return this.idContaContabilFrq;
	}

	public void setIdContaContabilFrq(long idContaContabilFrq) {
		this.idContaContabilFrq = idContaContabilFrq;
	}

	public String getDsContaContabil() {
		return this.dsContaContabil;
	}

	public void setDsContaContabil(String dsContaContabil) {
		this.dsContaContabil = dsContaContabil;
	}

	public String getCdContaContabil() {
		return this.cdContaContabil;
	}

	public void setCdContaContabil(String cdContaContabil) {
		this.cdContaContabil = cdContaContabil;
	}

	public Boolean getBlPermiteAlteracao() {
		return this.blPermiteAlteracao;
	}

	public void setBlPermiteAlteracao(Boolean blPermiteAlteracao) {
		this.blPermiteAlteracao = blPermiteAlteracao;
	}

	public DomainValue getTpLancamento() {
		return this.tpLancamento;
	}

	public void setTpLancamento(DomainValue tpLancamento) {
		this.tpLancamento = tpLancamento;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return this.dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return this.dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public Set<LancamentoFranqueado> getLancamentoFranqueados() {
		return lancamentoFranqueados;
	}

	public void setLancamentoFranqueados(
			Set<LancamentoFranqueado> lancamentoFranqueados) {
		this.lancamentoFranqueados = lancamentoFranqueados;
	}

	public DomainValue getTpContaContabil() {
		return tpContaContabil;
	}

	public void setTpContaContabil(DomainValue tpContaContabil) {
		this.tpContaContabil = tpContaContabil;
	}

}
