package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.ImpressoraLogDAO;
import com.mercurio.lms.expedicao.model.ImpressoraLog;

/**
 * @spring.bean id="lms.expedicao.impressoraLogService"
 */
public class ImpressoraLogService extends CrudService<ImpressoraLog, Long> {

	public final void setImpressoraLogDAO(ImpressoraLogDAO dao){

		setDao(dao);
	}

	public final ImpressoraLogDAO getImpressoraLogDAO() {

		return (ImpressoraLogDAO) getDao();
	}
}