package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.TipoCustoLog;

/**
 * @spring.bean 
 */
public class TipoCustoLogDAO extends BaseCrudDao<TipoCustoLog, Long> {

	protected final Class<TipoCustoLog> getPersistentClass() {
		return TipoCustoLog.class;
	}	
}
