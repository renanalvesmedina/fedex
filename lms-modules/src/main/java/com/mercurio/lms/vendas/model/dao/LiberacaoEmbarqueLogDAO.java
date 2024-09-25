package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.LiberacaoEmbarqueLog;

/**
 * @spring.bean 
 */
public class LiberacaoEmbarqueLogDAO extends BaseCrudDao<LiberacaoEmbarqueLog, Long> {

	protected final Class getPersistentClass() {

		return LiberacaoEmbarqueLog.class;
	}	
}
