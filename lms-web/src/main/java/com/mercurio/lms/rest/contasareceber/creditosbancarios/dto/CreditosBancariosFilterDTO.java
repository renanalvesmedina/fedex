package com.mercurio.lms.rest.contasareceber.creditosbancarios.dto;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.configuracoes.dto.BancoSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class CreditosBancariosFilterDTO extends BaseFilterDTO {
	
	private static final long serialVersionUID = 1L;
	
	private FilialSuggestDTO idFilialCredito;
	private YearMonthDay dataCreditoInicial;
	private YearMonthDay dataCreditoFinal;
	private DomainValue tpModalidade;
	private DomainValue tpOrigem;
	private BigDecimal vlCreditoInicial;
	private BigDecimal vlCreditoFinal;
	private String dsCpfCnpj;
	private String dsNomeRazaoSocial;
	private BancoSuggestDTO banco;
	private YearMonthDay dataAlteracaoInicial;
	private YearMonthDay dataAlteracaoFinal;
	private BigDecimal vlSaldoInicial;
	private BigDecimal vlSaldoFinal;
	private String dsBoleto;
	private String obCreditoBancario;
	private DomainValue tpSituacao;
	private DomainValue tpClassificacao;

	public YearMonthDay getDataCreditoInicial() {
		return dataCreditoInicial;
	}
	public void setDataCreditoInicial(YearMonthDay dataCreditoInicial) {
		this.dataCreditoInicial = dataCreditoInicial;
	}
	public YearMonthDay getDataCreditoFinal() {
		return dataCreditoFinal;
	}
	public void setDataCreditoFinal(YearMonthDay dataCreditoFinal) {
		this.dataCreditoFinal = dataCreditoFinal;
	}
	public DomainValue getTpModalidade() {
		return tpModalidade;
	}
	public void setTpModalidade(DomainValue tpModalidade) {
		this.tpModalidade = tpModalidade;
	}
	public DomainValue getTpOrigem() {
		return tpOrigem;
	}
	public void setTpOrigem(DomainValue tpOrigem) {
		this.tpOrigem = tpOrigem;
	}
	public BigDecimal getVlCreditoInicial() {
		return vlCreditoInicial;
	}
	public void setVlCreditoInicial(BigDecimal vlCreditoInicial) {
		this.vlCreditoInicial = vlCreditoInicial;
	}
	public BigDecimal getVlCreditoFinal() {
		return vlCreditoFinal;
	}
	public void setVlCreditoFinal(BigDecimal vlCreditoFinal) {
		this.vlCreditoFinal = vlCreditoFinal;
	}
	public String getDsCpfCnpj() {
		return dsCpfCnpj;
	}
	public void setDsCpfCnpj(String dsCpfCnpj) {
		this.dsCpfCnpj = dsCpfCnpj;
	}
	public String getDsNomeRazaoSocial() {
		return dsNomeRazaoSocial;
	}
	public void setDsNomeRazaoSocial(String dsNomeRazaoSocial) {
		this.dsNomeRazaoSocial = dsNomeRazaoSocial;
	}
	public BancoSuggestDTO getBanco() {
		return banco;
	}
	public void setBanco(BancoSuggestDTO banco) {
		this.banco = banco;
	}
	public YearMonthDay getDataAlteracaoInicial() {
		return dataAlteracaoInicial;
	}
	public void setDataAlteracaoInicial(YearMonthDay dataAlteracaoInicial) {
		this.dataAlteracaoInicial = dataAlteracaoInicial;
	}
	public YearMonthDay getDataAlteracaoFinal() {
		return dataAlteracaoFinal;
	}
	public void setDataAlteracaoFinal(YearMonthDay dataAlteracaoFinal) {
		this.dataAlteracaoFinal = dataAlteracaoFinal;
	}
	public BigDecimal getVlSaldoInicial() {
		return vlSaldoInicial;
	}
	public void setVlSaldoInicial(BigDecimal vlSaldoInicial) {
		this.vlSaldoInicial = vlSaldoInicial;
	}
	public BigDecimal getVlSaldoFinal() {
		return vlSaldoFinal;
	}
	public void setVlSaldoFinal(BigDecimal vlSaldoFinal) {
		this.vlSaldoFinal = vlSaldoFinal;
	}
	public String getDsBoleto() {
		return dsBoleto;
	}
	public void setDsBoleto(String dsBoleto) {
		this.dsBoleto = dsBoleto;
	}
	public String getObCreditoBancario() {
		return obCreditoBancario;
	}
	public void setObCreditoBancario(String obCreditoBancario) {
		this.obCreditoBancario = obCreditoBancario;
	}
	public FilialSuggestDTO getIdFilialCredito() {
		return idFilialCredito;
	}
	public void setIdFilialCredito(FilialSuggestDTO idFilialCredito) {
		this.idFilialCredito = idFilialCredito;
	}
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	public DomainValue getTpClassificacao() {
		return tpClassificacao;
	}
	public void setTpClassificacao(DomainValue tpClassificacao) {
		this.tpClassificacao = tpClassificacao;
	}
	
}
