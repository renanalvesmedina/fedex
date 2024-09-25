package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.TelefoneEnderecoLog;

/**
 * @spring.bean 
 */
public class TelefoneEnderecoLogDAO extends BaseCrudDao<TelefoneEnderecoLog, Long> {

	protected final Class getPersistentClass() {

		return TelefoneEnderecoLog.class;
	}	
}
