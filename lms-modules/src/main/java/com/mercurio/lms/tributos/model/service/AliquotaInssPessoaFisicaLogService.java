package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.dao.AliquotaInssPessoaFisicaLogDAO;
import com.mercurio.lms.tributos.model.AliquotaInssPessoaFisicaLog;

/**
 * @spring.bean id="lms.tributos.aliquotaInssPessoaFisicaLogService"
 */
public class AliquotaInssPessoaFisicaLogService extends CrudService<AliquotaInssPessoaFisicaLog, Long> {

	public final void setAliquotaInssPessoaFisicaLogDAO(AliquotaInssPessoaFisicaLogDAO dao){

		setDao(dao);
	}

	public final AliquotaInssPessoaFisicaLogDAO getAliquotaInssPessoaFisicaLogDAO() {

		return (AliquotaInssPessoaFisicaLogDAO) getDao();
	}
}