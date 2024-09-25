package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.GeneralidadeClienteLog;
import com.mercurio.lms.vendas.model.dao.GeneralidadeClienteLogDAO;

/**
 * @spring.bean id="lms.vendas.generalidadeClienteLogService"
 */
public class GeneralidadeClienteLogService extends CrudService<GeneralidadeClienteLog, Long> {

	public void setGeneralidadeClienteLogDAO(GeneralidadeClienteLogDAO dao){

		setDao( dao );
	}

	private GeneralidadeClienteLogDAO getGeneralidadeClienteLogDAO() {

		return (GeneralidadeClienteLogDAO) getDao();
	}
}