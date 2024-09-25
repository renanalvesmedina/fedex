package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.ParametroSubstituicaoTribLogDAO;
import com.mercurio.lms.tributos.model.ParametroSubstituicaoTribLog;

/**
 * @spring.bean id="lms.tributos.parametroSubstituicaoTribLogService"
 */
public class ParametroSubstituicaoTribLogService extends CrudService<ParametroSubstituicaoTribLog, Long> {

	public final void setParametroSubstituicaoTribLogDAO(ParametroSubstituicaoTribLogDAO dao){

		setDao(dao);
	}

	public final ParametroSubstituicaoTribLogDAO getParametroSubstituicaoTribLogDAO() {

		return (ParametroSubstituicaoTribLogDAO) getDao();
	}
}