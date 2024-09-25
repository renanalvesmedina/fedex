package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.ExcecaoICMSClienteLogDAO;
import com.mercurio.lms.tributos.model.ExcecaoICMSClienteLog;

/**
 * @spring.bean id="lms.tributos.excecaoICMSClienteLogService"
 */
public class ExcecaoICMSClienteLogService extends CrudService<ExcecaoICMSClienteLog, Long> {

	public final void setExcecaoICMSClienteLogDAO(ExcecaoICMSClienteLogDAO dao){

		setDao(dao);
	}

	public final ExcecaoICMSClienteLogDAO getExcecaoICMSClienteLogDAO() {

		return (ExcecaoICMSClienteLogDAO) getDao();
	}
}