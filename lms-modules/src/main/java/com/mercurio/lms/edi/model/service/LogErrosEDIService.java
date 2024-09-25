package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.dto.LogErrosEdiDTO;
import com.mercurio.lms.edi.model.LogErrosEDI;
import com.mercurio.lms.edi.model.dao.LogErrosEDIDAO;

public class LogErrosEDIService extends CrudService<LogErrosEDI, Long> {

	public void setDAO(LogErrosEDIDAO dao) {
		setDao(dao);
	}

	private LogErrosEDIDAO getDAO() {
		return (LogErrosEDIDAO) getDao();
	}

	public Serializable store(LogErrosEDI bean) {
		return super.store(bean);
	}

	public LogErrosEDI findById(Long id) {
		return getDAO().findById(id);
	}

	public List findNotasEDIParaAjuste(TypedFlatMap criteria){
		return getDAO().findNotasEDIParaAjuste(criteria);
	}

	public List<LogErrosEdiDTO> findNotasEDIParaAjuste(String naoTrazerLogsDoTipo, Integer nrProcessamento){
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("naoTrazerLogsDoTipo", naoTrazerLogsDoTipo);
		criteria.put("nrProcessamento", nrProcessamento);
		return getDAO().findNotasEDIParaAjuste(criteria);
	}
}
