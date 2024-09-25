package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.InformacaoDocServicoLog;

/**
 * @spring.bean 
 */
public class InformacaoDocServicoLogDAO extends BaseCrudDao<InformacaoDocServicoLog, Long> {

	protected final Class<InformacaoDocServicoLog> getPersistentClass() {
		return InformacaoDocServicoLog.class;
	}	
}
