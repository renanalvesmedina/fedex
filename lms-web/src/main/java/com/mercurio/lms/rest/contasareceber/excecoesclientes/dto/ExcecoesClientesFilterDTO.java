package com.mercurio.lms.rest.contasareceber.excecoesclientes.dto;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
 
public class ExcecoesClientesFilterDTO extends BaseFilterDTO { 
	private static final long serialVersionUID = 1L; 
 
	private ClienteSuggestDTO cliente;
	private DomainValue tpClienteFinanceiro;
	private DomainValue tpEnvioSerasa;
	private DomainValue tpEnvioFaturamento;
	private DomainValue tpEnvioCobrancaTerceira;
	private DomainValue tpEnvioCartaCobranca;
	private DomainValue tpEnvioCobrancaTerceiraProAtiva;
	private YearMonthDay periodoEmissaoInicial;
	private YearMonthDay periodoEmissaoFinal;
	
	public ClienteSuggestDTO getCliente() {
		return cliente;
	}
	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}
	public DomainValue getTpClienteFinanceiro() {
		return tpClienteFinanceiro;
	}
	public void setTpClienteFinanceiro(DomainValue tpClienteFinanceiro) {
		this.tpClienteFinanceiro = tpClienteFinanceiro;
	}
	public DomainValue getTpEnvioSerasa() {
		return tpEnvioSerasa;
	}
	public void setTpEnvioSerasa(DomainValue tpEnvioSerasa) {
		this.tpEnvioSerasa = tpEnvioSerasa;
	}
	public DomainValue getTpEnvioFaturamento() {
		return tpEnvioFaturamento;
	}
	public void setTpEnvioFaturamento(DomainValue tpEnvioFaturamento) {
		this.tpEnvioFaturamento = tpEnvioFaturamento;
	}
	public DomainValue getTpEnvioCobrancaTerceira() {
		return tpEnvioCobrancaTerceira;
	}
	public void setTpEnvioCobrancaTerceira(DomainValue tpEnvioCobrancaTerceira) {
		this.tpEnvioCobrancaTerceira = tpEnvioCobrancaTerceira;
	}
	public DomainValue getTpEnvioCartaCobranca() {
		return tpEnvioCartaCobranca;
	}
	public void setTpEnvioCartaCobranca(DomainValue tpEnvioCartaCobranca) {
		this.tpEnvioCartaCobranca = tpEnvioCartaCobranca;
	}
	public YearMonthDay getPeriodoEmissaoInicial() {
		return periodoEmissaoInicial;
	}
	public void setPeriodoEmissaoInicial(YearMonthDay periodoEmissaoInicial) {
		this.periodoEmissaoInicial = periodoEmissaoInicial;
	}
	public YearMonthDay getPeriodoEmissaoFinal() {
		return periodoEmissaoFinal;
	}
	public void setPeriodoEmissaoFinal(YearMonthDay periodoEmissaoFinal) {
		this.periodoEmissaoFinal = periodoEmissaoFinal;
	}
	public void setTpEnvioCobrancaTerceiraProAtiva(DomainValue tpEnvioCobrancaTerceiraProAtiva) {
		this.tpEnvioCobrancaTerceiraProAtiva = tpEnvioCobrancaTerceiraProAtiva;
	}
	
	public DomainValue getTpEnvioCobrancaTerceiraProAtiva() {
		return tpEnvioCobrancaTerceiraProAtiva;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
} 
