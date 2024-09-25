package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.ContaBancariaLog;

/**
 * @spring.bean 
 */
public class ContaBancariaLogDAO extends BaseCrudDao<ContaBancariaLog, Long> {

	protected final Class getPersistentClass() {

		return ContaBancariaLog.class;
	}	
}
