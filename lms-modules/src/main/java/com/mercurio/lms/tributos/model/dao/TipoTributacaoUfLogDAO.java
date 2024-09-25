package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.TipoTributacaoUfLog;

/**
 * @spring.bean 
 */
public class TipoTributacaoUfLogDAO extends BaseCrudDao<TipoTributacaoUfLog, Long> {

	protected final Class getPersistentClass() {

		return TipoTributacaoUfLog.class;
	}	
}
