package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.AliquotaIcmsAereoLogDAO;
import com.mercurio.lms.tributos.model.AliquotaIcmsAereoLog;

/**
 * @spring.bean id="lms.tributos.aliquotaIcmsAereoLogService"
 */
public class AliquotaIcmsAereoLogService extends CrudService<AliquotaIcmsAereoLog, Long> {

	public final void setAliquotaIcmsAereoLogDAO(AliquotaIcmsAereoLogDAO dao){

		setDao(dao);
	}

	public final AliquotaIcmsAereoLogDAO getAliquotaIcmsAereoLogDAO() {

		return (AliquotaIcmsAereoLogDAO) getDao();
	}
}