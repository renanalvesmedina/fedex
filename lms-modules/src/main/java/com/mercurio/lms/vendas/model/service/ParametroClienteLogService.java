package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.ParametroClienteLog;
import com.mercurio.lms.vendas.model.dao.ParametroClienteLogDAO;

/**
 * @spring.bean id="lms.vendas.parametroClienteLogService"
 */
public class ParametroClienteLogService extends CrudService<ParametroClienteLog, Long> {
	private ParametroClienteService parametroClienteService;
	
	/**
	 * Utiliza o findById da parametro e não do log 
	 */
	public  Serializable findById(Long id) {
		return parametroClienteService.findById(id);
	}

	public ParametroClienteService getParametroClienteService() {
		return parametroClienteService;
	}

	public void setParametroClienteService(
			ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}
	
	public void setParametroClienteLogDAO(ParametroClienteLogDAO dao){

		setDao( dao );
	}

	public ParametroClienteLogDAO getParametroClienteLogDAO() {
		return (ParametroClienteLogDAO) getDao();
	}

}