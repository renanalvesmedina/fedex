package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.joda.time.DateTime;

public class MonitoramentoMensagemDetalhe implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idMonitoramentoMensagemDetalhe;
	private MonitoramentoMensagem monitoramentoMensagem;
	private DateTime dhEnvio;
	private DateTime dhRecebimento;
	private DateTime dhDevolucao;
	private DateTime dhErro;
	private String dcMensagem;
	
	public Long getIdMonitoramentoMensagemDetalhe() {
		return idMonitoramentoMensagemDetalhe;
	}
	public void setIdMonitoramentoMensagemDetalhe(
			Long idMonitoramentoMensagemDetalhe) {
		this.idMonitoramentoMensagemDetalhe = idMonitoramentoMensagemDetalhe;
	}
	public MonitoramentoMensagem getMonitoramentoMensagem() {
		return monitoramentoMensagem;
	}
	public void setMonitoramentoMensagem(MonitoramentoMensagem monitoramentoMensagem) {
		this.monitoramentoMensagem = monitoramentoMensagem;
	}
	public DateTime getDhEnvio() {
		return dhEnvio;
	}
	public void setDhEnvio(DateTime dhEnvio) {
		this.dhEnvio = dhEnvio;
	}
	public DateTime getDhRecebimento() {
		return dhRecebimento;
	}
	public void setDhRecebimento(DateTime dhRecebimento) {
		this.dhRecebimento = dhRecebimento;
	}
	public DateTime getDhDevolucao() {
		return dhDevolucao;
	}
	public void setDhDevolucao(DateTime dhDevolucao) {
		this.dhDevolucao = dhDevolucao;
	}
	public DateTime getDhErro() {
		return dhErro;
	}
	public void setDhErro(DateTime dhErro) {
		this.dhErro = dhErro;
	}
	public String getDcMensagem() {
		return dcMensagem;
	}
	public void setDcMensagem(String dcMensagem) {
		this.dcMensagem = dcMensagem;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
