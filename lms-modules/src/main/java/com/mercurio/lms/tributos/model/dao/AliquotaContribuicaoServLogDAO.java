package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.AliquotaContribuicaoServLog;

/**
 * @spring.bean 
 */
public class AliquotaContribuicaoServLogDAO extends BaseCrudDao<AliquotaContribuicaoServLog, Long> {

	protected final Class getPersistentClass() {

		return AliquotaContribuicaoServLog.class;
	}	
}
