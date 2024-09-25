package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.ClienteLog;
import com.mercurio.lms.vendas.model.dao.ClienteLogDAO;

/**
 * @spring.bean id="lms.vendas.clienteLogService"
 */
public class ClienteLogService extends CrudService<ClienteLog, Long> {

	public void setClienteLogDAO(ClienteLogDAO dao){

		setDao( dao );
	}

	private ClienteLogDAO getClienteLogDAO() {

		return (ClienteLogDAO) getDao();
	}
}