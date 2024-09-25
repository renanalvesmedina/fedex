package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.RemetenteExcecaoICMSCliLogDAO;
import com.mercurio.lms.tributos.model.RemetenteExcecaoICMSCliLog;

/**
 * @spring.bean id="lms.tributos.remetenteExcecaoICMSCliLogService"
 */
public class RemetenteExcecaoICMSCliLogService extends CrudService<RemetenteExcecaoICMSCliLog, Long> {

	public final void setRemetenteExcecaoICMSCliLogDAO(RemetenteExcecaoICMSCliLogDAO dao){

		setDao(dao);
	}

	public final RemetenteExcecaoICMSCliLogDAO getRemetenteExcecaoICMSCliLogDAO() {

		return (RemetenteExcecaoICMSCliLogDAO) getDao();
	}
}