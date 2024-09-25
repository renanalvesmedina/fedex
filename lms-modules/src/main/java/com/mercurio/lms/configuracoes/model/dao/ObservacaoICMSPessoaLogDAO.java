package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.ObservacaoICMSPessoaLog;

/**
 * @spring.bean 
 */
public class ObservacaoICMSPessoaLogDAO extends BaseCrudDao<ObservacaoICMSPessoaLog, Long> {

	protected final Class getPersistentClass() {

		return ObservacaoICMSPessoaLog.class;
	}	
}
