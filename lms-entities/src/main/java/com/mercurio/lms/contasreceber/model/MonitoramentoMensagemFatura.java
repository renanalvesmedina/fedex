package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

public class MonitoramentoMensagemFatura implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idMonitMensFatura;
	private MonitoramentoMensagem monitoramentoMensagem;
	private Fatura fatura;
	
	public Long getIdMonitMensFatura() {
		return idMonitMensFatura;
	}
	public void setIdMonitMensFatura(Long idMonitMensFatura) {
		this.idMonitMensFatura = idMonitMensFatura;
	}
	
	public MonitoramentoMensagem getMonitoramentoMensagem() {
		return monitoramentoMensagem;
	}
	
	public void setMonitoramentoMensagem(MonitoramentoMensagem monitoramentoMensagem) {
		this.monitoramentoMensagem = monitoramentoMensagem;
	}
	public Fatura getFatura() {
		return fatura;
	}
	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
