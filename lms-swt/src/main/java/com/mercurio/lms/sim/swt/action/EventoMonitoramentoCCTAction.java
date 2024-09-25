package com.mercurio.lms.sim.swt.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.sim.model.service.EventoMonitoramentoCCTService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;

public class EventoMonitoramentoCCTAction extends CrudAction {

	private EventoMonitoramentoCCTService eventoMonitoramentoCCTService;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;

	public List findComboTpSituacao(){
		return monitoramentoNotasFiscaisCCTService.findComboTpSituacao(new String[]{"XM", "XD", "CD"}, true);
	}
	
	public List findComboTpSituacaoListagem(){
		return monitoramentoNotasFiscaisCCTService.findComboTpSituacao(new String[]{"XD", "CD"}, true);
	}
	
	public void incluirEventoPopUp(Map parameters){
		eventoMonitoramentoCCTService.storePopUpEventos(parameters);
	}

	public void setService(EventoMonitoramentoCCTService eventoMonitoramentoCCTService) {
		this.eventoMonitoramentoCCTService = eventoMonitoramentoCCTService;
	}

	public EventoMonitoramentoCCTService getService() {
		return eventoMonitoramentoCCTService;
	}

	public MonitoramentoNotasFiscaisCCTService getMonitoramentoNotasFiscaisCCTService() {
		return monitoramentoNotasFiscaisCCTService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}
	
	

}
