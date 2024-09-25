package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.TipoProdutoLogDAO;
import com.mercurio.lms.expedicao.model.TipoProdutoLog;

/**
 * @spring.bean id="lms.expedicao.tipoProdutoLogService"
 */
public class TipoProdutoLogService extends CrudService<TipoProdutoLog, Long> {

	public final void setTipoProdutoLogDAO(TipoProdutoLogDAO dao){

		setDao(dao);
	}

	public final TipoProdutoLogDAO getTipoProdutoLogDAO() {

		return (TipoProdutoLogDAO) getDao();
	}
}