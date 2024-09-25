package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.EmbalagemLogDAO;
import com.mercurio.lms.expedicao.model.EmbalagemLog;

/**
 * @spring.bean id="lms.expedicao.embalagemLogService"
 */
public class EmbalagemLogService extends CrudService<EmbalagemLog, Long> {

	public final void setEmbalagemLogDAO(EmbalagemLogDAO dao){

		setDao(dao);
	}

	public final EmbalagemLogDAO getEmbalagemLogDAO() {

		return (EmbalagemLogDAO) getDao();
	}
}