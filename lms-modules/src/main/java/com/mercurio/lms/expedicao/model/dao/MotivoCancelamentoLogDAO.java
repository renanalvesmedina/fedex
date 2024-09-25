package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.MotivoCancelamentoLog;

/**
 * @spring.bean 
 */
public class MotivoCancelamentoLogDAO extends BaseCrudDao<MotivoCancelamentoLog, Long> {

	protected final Class<MotivoCancelamentoLog> getPersistentClass() {
		return MotivoCancelamentoLog.class;
	}	
}
