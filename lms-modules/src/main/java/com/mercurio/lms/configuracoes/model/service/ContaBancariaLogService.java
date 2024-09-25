package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ContaBancariaLog;
import com.mercurio.lms.configuracoes.model.dao.ContaBancariaLogDAO;

/**
 * @spring.bean id="lms.configuracoes.contaBancariaLogService"
 */
public class ContaBancariaLogService extends CrudService<ContaBancariaLog, Long> {

	public final void setContaBancariaLogDAO(ContaBancariaLogDAO dao){

		setDao(dao);
	}

	public final ContaBancariaLogDAO getContaBancariaLogDAO() {

		return (ContaBancariaLogDAO) getDao();
	}
}