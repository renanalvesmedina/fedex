package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.TipoTributacaoIELog;

/**
 * @spring.bean 
 */
public class TipoTributacaoIELogDAO extends BaseCrudDao<TipoTributacaoIELog, Long> {

	protected final Class getPersistentClass() {

		return TipoTributacaoIELog.class;
	}	
}
