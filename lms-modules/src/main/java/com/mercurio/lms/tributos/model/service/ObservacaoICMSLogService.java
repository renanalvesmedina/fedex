package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.ObservacaoICMSLogDAO;
import com.mercurio.lms.tributos.model.ObservacaoICMSLog;

/**
 * @spring.bean id="lms.tributos.observacaoICMSLogService"
 */
public class ObservacaoICMSLogService extends CrudService<ObservacaoICMSLog, Long> {

	public final void setObservacaoICMSLogDAO(ObservacaoICMSLogDAO dao){

		setDao(dao);
	}

	public final ObservacaoICMSLogDAO getObservacaoICMSLogDAO() {

		return (ObservacaoICMSLogDAO) getDao();
	}
}