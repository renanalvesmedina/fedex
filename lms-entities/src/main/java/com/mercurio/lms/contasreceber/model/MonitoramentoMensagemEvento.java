package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class MonitoramentoMensagemEvento implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idMonitoramentoMensagemEvento;
	private MonitoramentoMensagem monitoramentoMensagem;
	private DomainValue tpEvento;
	private String dsEvento;
	private DateTime dhEvento;
	
	/**
	 * Mantido como público unica e exclusivamente por ser necessário para o Hibernate
	 * Utilizar static method factories 
	 */
	public MonitoramentoMensagemEvento( ) {
		super();
	}
	
	public static MonitoramentoMensagemEvento newInstance(Long idMonitoramentoMensagem, String tpEvento, String dsEvento) {
		MonitoramentoMensagem monitoramentoMensagem = null;
				
		if(idMonitoramentoMensagem != null) {			
			monitoramentoMensagem = MonitoramentoMensagem.newInstance(idMonitoramentoMensagem);
		}
		
		return MonitoramentoMensagemEvento.newInstance(monitoramentoMensagem, tpEvento, dsEvento);
	}
	
	public static MonitoramentoMensagemEvento newInstance(MonitoramentoMensagem monitoramentoMensagem, String tpEvento, String dsEvento) {
		MonitoramentoMensagemEvento evento = new MonitoramentoMensagemEvento();
		evento.setTpEvento(new DomainValue(tpEvento));
		evento.setDsEvento(dsEvento);
		evento.setDhEvento(new DateTime());
		evento.setMonitoramentoMensagem(monitoramentoMensagem);
		return evento;
	}
	
	public Long getIdMonitoramentoMensagemEvento() {
		return idMonitoramentoMensagemEvento;
	}
	public void setIdMonitoramentoMensagemEvento(Long idMonitoramentoMensagemEvento) {
		this.idMonitoramentoMensagemEvento = idMonitoramentoMensagemEvento;
	}
	public MonitoramentoMensagem getMonitoramentoMensagem() {
		return monitoramentoMensagem;
	}
	public void setMonitoramentoMensagem(MonitoramentoMensagem monitoramentoMensagem) {
		this.monitoramentoMensagem = monitoramentoMensagem;
	}
	public DomainValue getTpEvento() {
		return tpEvento;
	}
	public void setTpEvento(DomainValue tpEvento) {
		this.tpEvento = tpEvento;
	}
	public String getDsEvento() {
		return dsEvento;
	}
	public void setDsEvento(String dsEvento) {
		this.dsEvento = dsEvento;
	}
	public DateTime getDhEvento() {
		return dhEvento;
	}
	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
