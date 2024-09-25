package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.TipoProdutoLog;

/**
 * @spring.bean 
 */
public class TipoProdutoLogDAO extends BaseCrudDao<TipoProdutoLog, Long> {

	protected final Class<TipoProdutoLog> getPersistentClass() {
		return TipoProdutoLog.class;
	}	
}
