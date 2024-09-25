package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemEvento;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemEventoDAO;

public class MonitoramentoMensagemEventoService extends CrudService<MonitoramentoMensagemEvento, Long> {

	public Serializable store(MonitoramentoMensagemEvento entity) {
		return super.store(entity);
	}
	
	public void setMonitoramentoMensagemEventoDAO(MonitoramentoMensagemEventoDAO monitoramentoMensagemEventoDAO) {
		setDao(monitoramentoMensagemEventoDAO);
	}

}