package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.AgrupamentoClienteLog;
import com.mercurio.lms.vendas.model.dao.AgrupamentoClienteLogDAO;

/**
 * @spring.bean id="lms.vendas.agrupamentoClienteLogService"
 */
public class AgrupamentoClienteLogService extends CrudService<AgrupamentoClienteLog, Long> {

	public void setAgrupamentoClienteLogDAO(AgrupamentoClienteLogDAO dao){

		setDao( dao );
	}

	private AgrupamentoClienteLogDAO getAgrupamentoClienteLogDAO() {

		return (AgrupamentoClienteLogDAO) getDao();
	}
}