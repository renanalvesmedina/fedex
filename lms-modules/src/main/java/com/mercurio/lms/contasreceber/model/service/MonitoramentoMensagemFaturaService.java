package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemEvento;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemFatura;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemFaturaDAO;

public class MonitoramentoMensagemFaturaService extends CrudService<MonitoramentoMensagemFatura, Long> {

	private MonitoramentoMensagemFaturaDAO monitoramentoMensagemFaturaDAO;

	public List<Long> findFaturas(MonitoramentoMensagem monitoramentoMsg) {
		return monitoramentoMensagemFaturaDAO.findFaturas(monitoramentoMsg.getIdMonitoramentoMensagem());
	}
	
	public List<Long> getBoletos(MonitoramentoMensagem monitoramentoMsg) {
		return monitoramentoMensagemFaturaDAO.getBoletos(monitoramentoMsg.getIdMonitoramentoMensagem());
	}
	
	public void setMonitoramentoMensagemFaturaDAO(MonitoramentoMensagemFaturaDAO monitoramentoMensagemFaturaDAO) {
		this.monitoramentoMensagemFaturaDAO = monitoramentoMensagemFaturaDAO;
		setDao(monitoramentoMensagemFaturaDAO);
	}
	
	public Serializable store(MonitoramentoMensagemFatura entity) {
		return super.store(entity);
	}
	

}