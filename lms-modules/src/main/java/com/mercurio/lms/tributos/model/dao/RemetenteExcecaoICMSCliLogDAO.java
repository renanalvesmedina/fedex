package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.RemetenteExcecaoICMSCliLog;

/**
 * @spring.bean 
 */
public class RemetenteExcecaoICMSCliLogDAO extends BaseCrudDao<RemetenteExcecaoICMSCliLog, Long> {

	protected final Class getPersistentClass() {

		return RemetenteExcecaoICMSCliLog.class;
	}	
}
