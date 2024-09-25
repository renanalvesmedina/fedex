package com.mercurio.lms.rest.contasareceber.excecoesclientes.dto;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contasreceber.model.ExcecoesClienteFinanceiro;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.util.session.SessionUtils;
 
public class ExcecoesClientesDTO extends BaseDTO { 

	private static final long serialVersionUID = 1L; 
	private ClienteSuggestDTO cliente;
	private DomainValue tpClienteFinanceiro;
	private DomainValue tpEnvioFaturamento;
	private DomainValue tpEnvioCartaCobranca;
	private DomainValue tpEnvioSerasa;
	private DomainValue tpEnvioCobrancaTerceira;
	private DomainValue tpEnvioCobrancaTerceiraProAtiva;
	private DateTime dataAlteracao;
	private String dataAlteracaoFormatada;
	private UsuarioDTO usuario;
	private String observacao;
	
	public DomainValue getTpClienteFinanceiro() {
		return tpClienteFinanceiro;
	}
	public void setTpClienteFinanceiro(DomainValue tpClienteFinanceiro) {
		this.tpClienteFinanceiro = tpClienteFinanceiro;
	}
	public DomainValue getTpEnvioFaturamento() {
		return tpEnvioFaturamento;
	}
	public void setTpEnvioFaturamento(DomainValue tpEnvioFaturamento) {
		this.tpEnvioFaturamento = tpEnvioFaturamento;
	}
	public DomainValue getTpEnvioCartaCobranca() {
		return tpEnvioCartaCobranca;
	}
	public void setTpEnvioCartaCobranca(DomainValue tpEnvioCartaCobranca) {
		this.tpEnvioCartaCobranca = tpEnvioCartaCobranca;
	}
	public DomainValue getTpEnvioSerasa() {
		return tpEnvioSerasa;
	}
	public void setTpEnvioSerasa(DomainValue tpEnvioSerasa) {
		this.tpEnvioSerasa = tpEnvioSerasa;
	}
	public DomainValue getTpEnvioCobrancaTerceira() {
		return tpEnvioCobrancaTerceira;
	}
	public void setTpEnvioCobrancaTerceira(DomainValue tpEnvioCobrancaTerceira) {
		this.tpEnvioCobrancaTerceira = tpEnvioCobrancaTerceira;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public DateTime getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(DateTime dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public ClienteSuggestDTO getCliente() {
		return cliente;
	}
	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}
	public String getDataAlteracaoFormatada() {
		return dataAlteracaoFormatada;
	}
	public void setDataAlteracaoFormatada(String dataAlteracaoFormatada) {
		this.dataAlteracaoFormatada = dataAlteracaoFormatada;
	}
	
	public DomainValue getTpEnvioCobrancaTerceiraProAtiva() {
		return tpEnvioCobrancaTerceiraProAtiva;
	}
	public void setTpEnvioCobrancaTerceiraProAtiva(
			DomainValue tpEnvioCobrancaTerceiraProAtiva) {
		this.tpEnvioCobrancaTerceiraProAtiva = tpEnvioCobrancaTerceiraProAtiva;
	}
	
	public ExcecoesClienteFinanceiro build(ExcecoesClienteFinanceiro current){
		ExcecoesClienteFinanceiro excecoesClienteFinanceiro = current;
		excecoesClienteFinanceiro.setUsuario(SessionUtils.getUsuarioLogado());
		Pessoa p = new Pessoa();
		p.setNrIdentificacao(getCliente().getNrIdentificacao());
		p.setIdPessoa(getCliente().getIdCliente());
		p.setNmPessoa(getCliente().getNmPessoa());
		excecoesClienteFinanceiro.setPessoa(p);
		excecoesClienteFinanceiro.setTpCliente(getTpClienteFinanceiro());
		excecoesClienteFinanceiro.setTpEnvioCartaCobranca(getTpEnvioCartaCobranca());
		excecoesClienteFinanceiro.setTpEnvioCobrancaTerceira(getTpEnvioCobrancaTerceira());
		excecoesClienteFinanceiro.setTpEnvioCobrancaTerceiraProAtiva(getTpEnvioCobrancaTerceiraProAtiva());
		excecoesClienteFinanceiro.setTpEnvioFaturamento(getTpEnvioFaturamento());
		excecoesClienteFinanceiro.setTpEnvioSerasa(getTpEnvioSerasa());
		excecoesClienteFinanceiro.setObExcecaoClienteFinanceiro(getObservacao() != null ? getObservacao() : "");
		excecoesClienteFinanceiro.setDhAlteracao(new DateTime());
		
		return excecoesClienteFinanceiro;
	}
	
} 
