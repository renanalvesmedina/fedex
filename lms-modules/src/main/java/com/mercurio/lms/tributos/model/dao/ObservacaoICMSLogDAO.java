package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ObservacaoICMSLog;

/**
 * @spring.bean 
 */
public class ObservacaoICMSLogDAO extends BaseCrudDao<ObservacaoICMSLog, Long> {

	protected final Class getPersistentClass() {

		return ObservacaoICMSLog.class;
	}	
}
