package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.TaxaClienteLog;
import com.mercurio.lms.vendas.model.dao.TaxaClienteLogDAO;

/**
 * @spring.bean id="lms.vendas.taxaClienteLogService"
 */
public class TaxaClienteLogService extends CrudService<TaxaClienteLog, Long> {

	public void setTaxaClienteLogDAO(TaxaClienteLogDAO dao){

		setDao( dao );
	}

	private TaxaClienteLogDAO getTaxaClienteLogDAO() {

		return (TaxaClienteLogDAO) getDao();
	}
}