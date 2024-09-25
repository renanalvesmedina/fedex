package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.AliquotaIcmsLog;

/**
 * @spring.bean 
 */
public class AliquotaIcmsLogDAO extends BaseCrudDao<AliquotaIcmsLog, Long> {

	protected final Class getPersistentClass() {

		return AliquotaIcmsLog.class;
	}
}
