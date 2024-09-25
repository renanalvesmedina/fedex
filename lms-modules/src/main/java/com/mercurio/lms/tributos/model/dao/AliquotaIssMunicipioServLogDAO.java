package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.AliquotaIssMunicipioServLog;

/**
 * @spring.bean 
 */
public class AliquotaIssMunicipioServLogDAO extends BaseCrudDao<AliquotaIssMunicipioServLog, Long> {

	protected final Class getPersistentClass() {

		return AliquotaIssMunicipioServLog.class;
	}	
}
