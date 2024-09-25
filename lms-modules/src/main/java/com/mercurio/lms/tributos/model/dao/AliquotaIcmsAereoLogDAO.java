package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.AliquotaIcmsAereoLog;

/**
 * @spring.bean 
 */
public class AliquotaIcmsAereoLogDAO extends BaseCrudDao<AliquotaIcmsAereoLog, Long> {

	protected final Class getPersistentClass() {

		return AliquotaIcmsAereoLog.class;
	}	
}
