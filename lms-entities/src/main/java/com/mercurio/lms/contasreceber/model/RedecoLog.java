package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.EmpresaCobranca;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.workflow.model.Pendencia;

public class RedecoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idRedecoLog;
	private Redeco redeco;
	private Filial filial;
	private Long nrRedeco;
	private BigDecimal vlDiferencaCambialCotacao;
	private YearMonthDay dtEmissao;
	private DomainValue tpSituacaoRedeco;
	private DomainValue tpFinalidade;
	private String nmResponsavelCobranca;
	private DomainValue tpRecebimento;
	private DomainValue tpSituacaoWorkflow;
	private EmpresaCobranca empresaCobranca;
	private YearMonthDay dtLiquidacao;
	private YearMonthDay dtRecebimento;
	private DateTime dhTransmissao;
	private String obRedeco;
	private DomainValue tpAbrangencia;
	private Long nrVersao;
	private Pendencia pendenciaDesconto;
	private Pendencia pendenciaLucrosPerdas;
	private Pendencia pendenciaRecebimento;
	private Long idMoeda;
	private String dhTransmissaoTzr;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdRedecoLog() {
   
		return idRedecoLog;
	}
   
	public void setIdRedecoLog(long idRedecoLog) {
   
		this.idRedecoLog = idRedecoLog;
	}
	
	public Redeco getRedeco() {
   
		return redeco;
	}
   
	public void setRedeco(Redeco redeco) {
   
		this.redeco = redeco;
	}
	
	public Filial getFilial() {
   
		return filial;
	}
   
	public void setFilial(Filial filial) {
   
		this.filial = filial;
	}
	
	public Long getNrRedeco() {
   
		return nrRedeco;
	}
   
	public void setNrRedeco(Long nrRedeco) {
   
		this.nrRedeco = nrRedeco;
	}
	
	public BigDecimal getVlDiferencaCambialCotacao() {
   
		return vlDiferencaCambialCotacao;
	}
   
	public void setVlDiferencaCambialCotacao(
			BigDecimal vlDiferencaCambialCotacao) {
   
		this.vlDiferencaCambialCotacao = vlDiferencaCambialCotacao;
	}
	
	public YearMonthDay getDtEmissao() {
   
		return dtEmissao;
	}
   
	public void setDtEmissao(YearMonthDay dtEmissao) {
   
		this.dtEmissao = dtEmissao;
	}
	
	public DomainValue getTpSituacaoRedeco() {
   
		return tpSituacaoRedeco;
	}
   
	public void setTpSituacaoRedeco(DomainValue tpSituacaoRedeco) {
   
		this.tpSituacaoRedeco = tpSituacaoRedeco;
	}
	
	public DomainValue getTpFinalidade() {
   
		return tpFinalidade;
	}
   
	public void setTpFinalidade(DomainValue tpFinalidade) {
   
		this.tpFinalidade = tpFinalidade;
	}
	
	public String getNmResponsavelCobranca() {
   
		return nmResponsavelCobranca;
	}
   
	public void setNmResponsavelCobranca(String nmResponsavelCobranca) {
   
		this.nmResponsavelCobranca = nmResponsavelCobranca;
	}
	
	public DomainValue getTpRecebimento() {
   
		return tpRecebimento;
	}
   
	public void setTpRecebimento(DomainValue tpRecebimento) {
   
		this.tpRecebimento = tpRecebimento;
	}
	
	public DomainValue getTpSituacaoWorkflow() {
   
		return tpSituacaoWorkflow;
	}
   
	public void setTpSituacaoWorkflow(DomainValue tpSituacaoWorkflow) {
   
		this.tpSituacaoWorkflow = tpSituacaoWorkflow;
	}
	
	public EmpresaCobranca getEmpresaCobranca() {
   
		return empresaCobranca;
	}
   
	public void setEmpresaCobranca(EmpresaCobranca empresaCobranca) {
   
		this.empresaCobranca = empresaCobranca;
	}
	
	public YearMonthDay getDtLiquidacao() {
   
		return dtLiquidacao;
	}
   
	public void setDtLiquidacao(YearMonthDay dtLiquidacao) {
   
		this.dtLiquidacao = dtLiquidacao;
	}
	
	public YearMonthDay getDtRecebimento() {
   
		return dtRecebimento;
	}
   
	public void setDtRecebimento(YearMonthDay dtRecebimento) {
   
		this.dtRecebimento = dtRecebimento;
	}
	
	public DateTime getDhTransmissao() {
   
		return dhTransmissao;
	}
   
	public void setDhTransmissao(DateTime dhTransmissao) {
   
		this.dhTransmissao = dhTransmissao;
	}
	
	public String getObRedeco() {
   
		return obRedeco;
	}
   
	public void setObRedeco(String obRedeco) {
   
		this.obRedeco = obRedeco;
	}
	
	public DomainValue getTpAbrangencia() {
   
		return tpAbrangencia;
	}
   
	public void setTpAbrangencia(DomainValue tpAbrangencia) {
   
		this.tpAbrangencia = tpAbrangencia;
	}
	
	public Long getNrVersao() {
   
		return nrVersao;
	}
   
	public void setNrVersao(Long nrVersao) {
   
		this.nrVersao = nrVersao;
	}
	
	public Pendencia getPendenciaDesconto() {
   
		return pendenciaDesconto;
	}
   
	public void setPendenciaDesconto(Pendencia pendenciaDesconto) {
   
		this.pendenciaDesconto = pendenciaDesconto;
	}
	
	public Pendencia getPendenciaLucrosPerdas() {
   
		return pendenciaLucrosPerdas;
	}
   
	public void setPendenciaLucrosPerdas(Pendencia pendenciaLucrosPerdas) {
   
		this.pendenciaLucrosPerdas = pendenciaLucrosPerdas;
	}
	
	public Pendencia getPendenciaRecebimento() {
   
		return pendenciaRecebimento;
	}
   
	public void setPendenciaRecebimento(Pendencia pendenciaRecebimento) {
   
		this.pendenciaRecebimento = pendenciaRecebimento;
	}
	
	public Long getIdMoeda() {
   
		return idMoeda;
	}
   
	public void setIdMoeda(Long idMoeda) {
   
		this.idMoeda = idMoeda;
	}
	
	public String getDhTransmissaoTzr() {
   
		return dhTransmissaoTzr;
	}
   
	public void setDhTransmissaoTzr(String dhTransmissaoTzr) {
   
		this.dhTransmissaoTzr = dhTransmissaoTzr;
	}
	
	public DomainValue getTpOrigemLog() {
   
		return tpOrigemLog;
	}
   
	public void setTpOrigemLog(DomainValue tpOrigemLog) {
   
		this.tpOrigemLog = tpOrigemLog;
	}
	
	public String getLoginLog() {
   
		return loginLog;
	}
   
	public void setLoginLog(String loginLog) {
   
		this.loginLog = loginLog;
	}
	
	public DateTime getDhLog() {
   
		return dhLog;
	}
   
	public void setDhLog(DateTime dhLog) {
   
		this.dhLog = dhLog;
	}
	
	public DomainValue getOpLog() {
   
		return opLog;
	}
   
	public void setOpLog(DomainValue opLog) {
   
		this.opLog = opLog;
	}
	
   	public String toString() {
		return new ToStringBuilder(this)
				.append("idRedecoLog", getIdRedecoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RedecoLog))
			return false;
		RedecoLog castOther = (RedecoLog) other;
		return new EqualsBuilder().append(this.getIdRedecoLog(),
				castOther.getIdRedecoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdRedecoLog()).toHashCode();
	}
} 