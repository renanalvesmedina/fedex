package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ParametroSubstituicaoTribLog;

/**
 * @spring.bean 
 */
public class ParametroSubstituicaoTribLogDAO extends BaseCrudDao<ParametroSubstituicaoTribLog, Long> {

	protected final Class getPersistentClass() {

		return ParametroSubstituicaoTribLog.class;
	}	
}
