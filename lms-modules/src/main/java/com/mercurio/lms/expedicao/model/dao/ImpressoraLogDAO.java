package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ImpressoraLog;

/**
 * @spring.bean 
 */
public class ImpressoraLogDAO extends BaseCrudDao<ImpressoraLog, Long> {

	protected final Class<ImpressoraLog> getPersistentClass() {
		return ImpressoraLog.class;
	}	
}
