package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.LogCargaAwb;
import com.mercurio.lms.expedicao.model.dao.LogCargaAwbDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class LogCargaAwbService extends CrudService<LogCargaAwb, Long> {

	private LogCargaAwb populateLogCargaAwb(String dsSerie, Long nrAwb, String dsMensagem, String nrChave) {
		LogCargaAwb logCargaAwb = new LogCargaAwb();
		logCargaAwb.setDsSerie(dsSerie);
		logCargaAwb.setNrAwb(nrAwb);
		logCargaAwb.setDsMensagem(dsMensagem);
		logCargaAwb.setNrChave(nrChave);
		logCargaAwb.setDhInclusao(JTDateTimeUtils.getDataHoraAtual().toDate());
	
		return logCargaAwb;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void generateLogCargaAwb(String dsSerie, Long nrAwb, String dsMensagem, String nrChave) {
		LogCargaAwb logCargaAwb = populateLogCargaAwb(dsSerie, nrAwb, dsMensagem, nrChave);			
		this.store(logCargaAwb);
	}
	
	public boolean isNewLogCargaAwb(String dsMensagem, String nrChave){
		List<LogCargaAwb> logs = this.findByNrChaveAndDsMessage(nrChave, dsMensagem);		
		return CollectionUtils.isEmpty(logs);
	}
	
	public List<LogCargaAwb> findByNrChave(String nrChave) {
		return getLogCargaAwbDAO().findByNrChave(nrChave);
	}
	
	public List<LogCargaAwb> findByNrChaveAndDsMessage(String nrChave, String dsMensagem) {
		return getLogCargaAwbDAO().findByNrChaveAndDsMessage(nrChave, dsMensagem);
	}
	
	public Serializable store(LogCargaAwb bean) {
		return super.store(bean);
	}
	
	public LogCargaAwbDAO getLogCargaAwbDAO() {
		return (LogCargaAwbDAO) getDao();
	}
	
	public void setLogCargaAwbDAO(LogCargaAwbDAO logCargaAwbDAO) {
		setDao(logCargaAwbDAO);
	}
}
