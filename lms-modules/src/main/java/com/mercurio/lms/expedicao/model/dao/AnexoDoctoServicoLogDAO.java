package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.AnexoDoctoServicoLog;

/**
 * @spring.bean 
 */
public class AnexoDoctoServicoLogDAO extends BaseCrudDao<AnexoDoctoServicoLog, Long> {

	protected final Class<AnexoDoctoServicoLog> getPersistentClass() {
		return AnexoDoctoServicoLog.class;
	}	
}
