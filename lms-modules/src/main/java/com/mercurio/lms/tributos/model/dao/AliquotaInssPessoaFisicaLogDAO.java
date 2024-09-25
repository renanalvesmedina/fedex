package com.mercurio.lms.tributos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.AliquotaInssPessoaFisicaLog;

/**
 * @spring.bean 
 */
public class AliquotaInssPessoaFisicaLogDAO extends BaseCrudDao<AliquotaInssPessoaFisicaLog, Long> {

	protected final Class getPersistentClass() {

		return AliquotaInssPessoaFisicaLog.class;
	}	
}
