package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.EmbalagemLog;

/**
 * @spring.bean 
 */
public class EmbalagemLogDAO extends BaseCrudDao<EmbalagemLog, Long> {

	protected final Class<EmbalagemLog> getPersistentClass() {
		return EmbalagemLog.class;
	}	
}
