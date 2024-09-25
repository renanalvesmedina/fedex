package com.mercurio.lms.vendas.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.dao.LiberacaoEmbarqueLogDAO;
import com.mercurio.lms.vendas.model.LiberacaoEmbarqueLog;

/**
 * @spring.bean id="lms.vendas.liberacaoEmbarqueLogService"
 */
public class LiberacaoEmbarqueLogService extends CrudService<LiberacaoEmbarqueLog, Long> {

	public final void setLiberacaoEmbarqueLogDAO(LiberacaoEmbarqueLogDAO dao){

		setDao(dao);
	}

	public final LiberacaoEmbarqueLogDAO getLiberacaoEmbarqueLogDAO() {

		return (LiberacaoEmbarqueLogDAO) getDao();
	}
}