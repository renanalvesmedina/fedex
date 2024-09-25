package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.NaturezaProdutoLog;

/**
 * @spring.bean 
 */
public class NaturezaProdutoLogDAO extends BaseCrudDao<NaturezaProdutoLog, Long> {

	protected final Class<NaturezaProdutoLog> getPersistentClass() {
		return NaturezaProdutoLog.class;
	}	
}
