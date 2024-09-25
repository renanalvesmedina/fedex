package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.ContatoLog;

/**
 * @spring.bean 
 */
public class ContatoLogDAO extends BaseCrudDao<ContatoLog, Long> {

	protected final Class getPersistentClass() {

		return ContatoLog.class;
	}	
}
