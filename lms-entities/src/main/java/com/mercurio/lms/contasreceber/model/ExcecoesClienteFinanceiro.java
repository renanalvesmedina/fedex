package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;

public class ExcecoesClienteFinanceiro implements Serializable {
		private static final long serialVersionUID = 1L;

		private Long idExcecaoClienteFinanceiro;
		private Pessoa pessoa;
		private Usuario usuario;
		private DomainValue tpCliente;
		private DomainValue tpEnvioFaturamento;
		private DomainValue tpEnvioCartaCobranca;
		private DomainValue tpEnvioSerasa;
		private DomainValue tpEnvioCobrancaTerceira;
		private DomainValue tpEnvioCobrancaTerceiraProAtiva;
		
		private String obExcecaoClienteFinanceiro;
		private DateTime dhAlteracao;
		
		public Long getIdExcecaoClienteFinanceiro() {
			return idExcecaoClienteFinanceiro;
		}
		public void setIdExcecaoClienteFinanceiro(Long idExcecaoClienteFinanceiro) {
			this.idExcecaoClienteFinanceiro = idExcecaoClienteFinanceiro;
		}
		public Pessoa getPessoa() {
			return pessoa;
		}
		public void setPessoa(Pessoa pessoa) {
			this.pessoa = pessoa;
		}
		public Usuario getUsuario() {
			return usuario;
		}
		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}
		public DomainValue getTpCliente() {
			return tpCliente;
		}
		public void setTpCliente(DomainValue tpCliente) {
			this.tpCliente = tpCliente;
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
		public String getObExcecaoClienteFinanceiro() {
			return obExcecaoClienteFinanceiro;
		}
		public void setObExcecaoClienteFinanceiro(String obExcecaoClienteFinanceiro) {
			this.obExcecaoClienteFinanceiro = obExcecaoClienteFinanceiro;
		}
		public DateTime getDhAlteracao() {
			return dhAlteracao;
		}
		public void setDhAlteracao(DateTime dhAlteracao) {
			this.dhAlteracao = dhAlteracao;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public DomainValue getTpEnvioCobrancaTerceiraProAtiva() {
			return tpEnvioCobrancaTerceiraProAtiva;
		}
		public void setTpEnvioCobrancaTerceiraProAtiva(
				DomainValue tpEnvioCobrancaTerceiraProAtiva) {
			this.tpEnvioCobrancaTerceiraProAtiva = tpEnvioCobrancaTerceiraProAtiva;
		}
}
