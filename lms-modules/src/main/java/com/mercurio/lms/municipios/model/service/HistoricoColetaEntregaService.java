package com.mercurio.lms.municipios.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.HistoricoColetaEntrega;
import com.mercurio.lms.municipios.model.dao.HistoricoColetaEntregaDAO;

public class HistoricoColetaEntregaService extends
		CrudService<HistoricoColetaEntrega, Long> {

	public void setHistoricoColetaEntregaDAO(HistoricoColetaEntregaDAO dao) {
		setDao(dao);
	}

	private HistoricoColetaEntregaDAO getHistoricoColetaEntregaDAO() {
		return (HistoricoColetaEntregaDAO) getDao();
	}

	public List<Map<String, Object>> findHistoricoColetaEntrega(Map<String, Object> parameters) {
		return getHistoricoColetaEntregaDAO().findHistoricoColetaEntrega(parameters);
	}
	
	public Serializable store(HistoricoColetaEntrega bean) {
		return super.store(bean);
	}
}