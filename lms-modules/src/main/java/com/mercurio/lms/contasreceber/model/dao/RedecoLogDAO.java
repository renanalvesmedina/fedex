package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.RedecoLog;

/**
 * @spring.bean 
 */
public class RedecoLogDAO extends BaseCrudDao<RedecoLog, Long> {

	protected final Class getPersistentClass() {

		return RedecoLog.class;
	}	
}
