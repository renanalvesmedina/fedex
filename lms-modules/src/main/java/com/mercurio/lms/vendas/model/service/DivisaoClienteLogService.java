package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.DivisaoClienteLog;
import com.mercurio.lms.vendas.model.dao.DivisaoClienteLogDAO;

/**
 * @spring.bean id="lms.vendas.divisaoClienteLogService"
 */
public class DivisaoClienteLogService extends CrudService<DivisaoClienteLog, Long> {

	public void setDivisaoClienteLogDAO(DivisaoClienteLogDAO dao){

		setDao( dao );
	}

	private DivisaoClienteLogDAO getDivisaoClienteLogDAO() {

		return (DivisaoClienteLogDAO) getDao();
	}
}