package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ExcecaoICMSClienteLog;

/**
 * @spring.bean 
 */
public class ExcecaoICMSClienteLogDAO extends BaseCrudDao<ExcecaoICMSClienteLog, Long> {

	protected final Class getPersistentClass() {

		return ExcecaoICMSClienteLog.class;
	}	
}
