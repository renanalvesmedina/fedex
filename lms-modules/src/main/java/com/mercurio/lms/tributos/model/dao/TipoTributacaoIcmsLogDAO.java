package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.TipoTributacaoIcmsLog;

/**
 * @spring.bean 
 */
public class TipoTributacaoIcmsLogDAO extends BaseCrudDao<TipoTributacaoIcmsLog, Long> {

	protected final Class getPersistentClass() {

		return TipoTributacaoIcmsLog.class;
	}	
}
