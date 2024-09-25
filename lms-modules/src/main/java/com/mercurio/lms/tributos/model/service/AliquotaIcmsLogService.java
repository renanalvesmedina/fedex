package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.AliquotaIcmsLogDAO;
import com.mercurio.lms.tributos.model.AliquotaIcmsLog;

/**
 * @spring.bean id="lms.tributos.aliquotaIcmsLogService"
 */
public class AliquotaIcmsLogService extends CrudService<AliquotaIcmsLog, Long> {

	public final void setAliquotaIcmsLogDAO(AliquotaIcmsLogDAO dao){

		setDao(dao);
	}

	public final AliquotaIcmsLogDAO getAliquotaIcmsLogDAO() {

		return (AliquotaIcmsLogDAO) getDao();
	}
}