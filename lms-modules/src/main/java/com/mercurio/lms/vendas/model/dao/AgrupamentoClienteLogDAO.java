package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.AgrupamentoClienteLog;

/**
 * @spring.bean 
 */
public class AgrupamentoClienteLogDAO extends BaseCrudDao<AgrupamentoClienteLog, Long> {

	protected final Class getPersistentClass() {

		return AgrupamentoClienteLog.class;
	}	
}
