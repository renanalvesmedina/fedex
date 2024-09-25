package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.AliquotaIssMunicipioServLogDAO;
import com.mercurio.lms.tributos.model.AliquotaIssMunicipioServLog;

/**
 * @spring.bean id="lms.tributos.aliquotaIssMunicipioServLogService"
 */
public class AliquotaIssMunicipioServLogService extends CrudService<AliquotaIssMunicipioServLog, Long> {

	public final void setAliquotaIssMunicipioServLogDAO(AliquotaIssMunicipioServLogDAO dao){

		setDao(dao);
	}

	public final AliquotaIssMunicipioServLogDAO getAliquotaIssMunicipioServLogDAO() {

		return (AliquotaIssMunicipioServLogDAO) getDao();
	}
}