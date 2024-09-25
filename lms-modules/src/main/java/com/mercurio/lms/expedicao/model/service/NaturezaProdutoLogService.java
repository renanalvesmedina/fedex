package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.NaturezaProdutoLogDAO;
import com.mercurio.lms.expedicao.model.NaturezaProdutoLog;

/**
 * @spring.bean id="lms.expedicao.naturezaProdutoLogService"
 */
public class NaturezaProdutoLogService extends CrudService<NaturezaProdutoLog, Long> {

	public final void setNaturezaProdutoLogDAO(NaturezaProdutoLogDAO dao){

		setDao(dao);
	}

	public final NaturezaProdutoLogDAO getNaturezaProdutoLogDAO() {

		return (NaturezaProdutoLogDAO) getDao();
	}
}