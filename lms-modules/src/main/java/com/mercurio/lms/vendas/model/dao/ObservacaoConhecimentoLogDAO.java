package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ObservacaoConhecimentoLog;

/**
 * @spring.bean 
 */
public class ObservacaoConhecimentoLogDAO extends BaseCrudDao<ObservacaoConhecimentoLog, Long> {

	protected final Class getPersistentClass() {

		return ObservacaoConhecimentoLog.class;
	}	
}
