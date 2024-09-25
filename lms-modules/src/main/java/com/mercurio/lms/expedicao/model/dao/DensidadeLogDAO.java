package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.DensidadeLog;

/**
 * @spring.bean 
 */
public class DensidadeLogDAO extends BaseCrudDao<DensidadeLog, Long> {

	protected final Class getPersistentClass() {

		return DensidadeLog.class;
	}	
}
