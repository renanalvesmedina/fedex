package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.InscricaoEstadualLog;

/**
 * @spring.bean 
 */
public class InscricaoEstadualLogDAO extends BaseCrudDao<InscricaoEstadualLog, Long> {

	protected final Class getPersistentClass() {

		return InscricaoEstadualLog.class;
	}	
}
