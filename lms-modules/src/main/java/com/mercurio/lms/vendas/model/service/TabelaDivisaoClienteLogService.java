package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.TabelaDivisaoClienteLog;
import com.mercurio.lms.vendas.model.dao.TabelaDivisaoClienteLogDAO;

/**
 * @spring.bean id="lms.vendas.tabelaDivisaoClienteLogService"
 */
public class TabelaDivisaoClienteLogService extends CrudService<TabelaDivisaoClienteLog, Long> {

	public void setTabelaDivisaoClienteLogDAO(TabelaDivisaoClienteLogDAO dao){

		setDao( dao );
	}

	private TabelaDivisaoClienteLogDAO getTabelaDivisaoClienteLogDAO() {

		return (TabelaDivisaoClienteLogDAO) getDao();
	}
}