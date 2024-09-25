package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.TipoRegistroComplementoLog;

/**
 * @spring.bean 
 */
public class TipoRegistroComplementoLogDAO extends BaseCrudDao<TipoRegistroComplementoLog, Long> {

	protected final Class<TipoRegistroComplementoLog> getPersistentClass() {
		return TipoRegistroComplementoLog.class;
	}	
}
