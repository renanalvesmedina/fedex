package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.AliquotaContribuicaoServLogDAO;
import com.mercurio.lms.tributos.model.AliquotaContribuicaoServLog;
/**
 * @spring.bean id="lms.tributos.aliquotaContribuicaoServLogService"
 */
public class AliquotaContribuicaoServLogService extends CrudService<AliquotaContribuicaoServLog, Long> {

	public final void setAliquotaContribuicaoServLogDAO(AliquotaContribuicaoServLogDAO dao){

		setDao(dao);
	}

	public final AliquotaContribuicaoServLogDAO getAliquotaContribuicaoServLogDAO() {

		return (AliquotaContribuicaoServLogDAO) getDao();
	}
}