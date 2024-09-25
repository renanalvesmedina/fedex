package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.EnderecoPessoaLog;

/**
 * @spring.bean 
 */
public class EnderecoPessoaLogDAO extends BaseCrudDao<EnderecoPessoaLog, Long> {

	protected final Class getPersistentClass() {

		return EnderecoPessoaLog.class;
	}	
}
