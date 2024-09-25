package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.DensidadeLogDAO;
import com.mercurio.lms.expedicao.model.DensidadeLog;

/**
 * @spring.bean id="lms.expedicao.densidadeLogService"
 */
public class DensidadeLogService extends CrudService<DensidadeLog, Long> {

	public final void setDensidadeLogDAO(DensidadeLogDAO dao){

		setDao(dao);
	}

	public final DensidadeLogDAO getDensidadeLogDAO() {

		return (DensidadeLogDAO) getDao();
	}
}