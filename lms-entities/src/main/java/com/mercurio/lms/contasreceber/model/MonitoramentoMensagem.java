package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class MonitoramentoMensagem implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idMonitoramentoMensagem;
	private DomainValue tpEnvioMensagem;
	private DomainValue tpModeloMensagem;
	private DateTime dhInclusao;
	private DateTime dhProcessamento;
	private String dsDestinatario;
	private String dsParametro;
		
	/**
	 * Mantido como público unica e exclusivamente por ser necessário para o Hibernate
	 * Utilizar static method factories 
	 */
	public MonitoramentoMensagem() {
		super();
	}
	
	public static MonitoramentoMensagem newInstance(Long idMonitoramentoMensagem) {
		MonitoramentoMensagem monitoramentoMensagem = new MonitoramentoMensagem();
		monitoramentoMensagem.setIdMonitoramentoMensagem(idMonitoramentoMensagem);
		return monitoramentoMensagem;
	}
	
	public Long getIdMonitoramentoMensagem() {
		return idMonitoramentoMensagem;
	}
	public void setIdMonitoramentoMensagem(Long idMonitoramentoMensagem) {
		this.idMonitoramentoMensagem = idMonitoramentoMensagem;
	}
	public DomainValue getTpEnvioMensagem() {
		return tpEnvioMensagem;
	}
	public void setTpEnvioMensagem(DomainValue tpEnvioMensagem) {
		this.tpEnvioMensagem = tpEnvioMensagem;
	}
	public DomainValue getTpModeloMensagem() {
		return tpModeloMensagem;
	}
	public void setTpModeloMensagem(DomainValue tpModeloMensagem) {
		this.tpModeloMensagem = tpModeloMensagem;
	}
	public DateTime getDhInclusao() {
		return dhInclusao;
	}
	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}
	public DateTime getDhProcessamento() {
		return dhProcessamento;
	}
	public void setDhProcessamento(DateTime dhProcessamento) {
		this.dhProcessamento = dhProcessamento;
	}
	public String getDsDestinatario() {
		return dsDestinatario;
	}
	public void setDsDestinatario(String dsDestinatario) {
		this.dsDestinatario = dsDestinatario;
	}
	public String getDsParametro() {
		return dsParametro;
	}
	public void setDsParametro(String dsParametro) {
		this.dsParametro = dsParametro;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
