package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.MotivoCancelamentoLogDAO;
import com.mercurio.lms.expedicao.model.MotivoCancelamentoLog;

/**
 * @spring.bean id="lms.expedicao.motivoCancelamentoLogService"
 */
public class MotivoCancelamentoLogService extends CrudService<MotivoCancelamentoLog, Long> {

	public final void setMotivoCancelamentoLogDAO(MotivoCancelamentoLogDAO dao){

		setDao(dao);
	}

	public final MotivoCancelamentoLogDAO getMotivoCancelamentoLogDAO() {

		return (MotivoCancelamentoLogDAO) getDao();
	}
}