package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.TipoCustoLogDAO;
import com.mercurio.lms.expedicao.model.TipoCustoLog;

/**
 * @spring.bean id="lms.expedicao.tipoCustoLogService"
 */
public class TipoCustoLogService extends CrudService<TipoCustoLog, Long> {

	public final void setTipoCustoLogDAO(TipoCustoLogDAO dao){

		setDao(dao);
	}

	public final TipoCustoLogDAO getTipoCustoLogDAO() {

		return (TipoCustoLogDAO) getDao();
	}
}