package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.dao.RedecoLogDAO;
import com.mercurio.lms.contasreceber.model.RedecoLog;

/**
 * @spring.bean id="lms.contasreceber.redecoLogService"
 */
public class RedecoLogService extends CrudService<RedecoLog, Long> {

	public final void setRedecoLogDAO(RedecoLogDAO dao){

		setDao(dao);
	}

	public final RedecoLogDAO getRedecoLogDAO() {

		return (RedecoLogDAO) getDao();
	}
}