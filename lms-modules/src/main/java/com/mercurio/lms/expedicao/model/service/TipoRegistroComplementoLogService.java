package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.TipoRegistroComplementoLogDAO;
import com.mercurio.lms.expedicao.model.TipoRegistroComplementoLog;

/**
 * @spring.bean id="lms.expedicao.tipoRegistroComplementoLogService"
 */
public class TipoRegistroComplementoLogService extends CrudService<TipoRegistroComplementoLog, Long> {

	public final void setTipoRegistroComplementoLogDAO(TipoRegistroComplementoLogDAO dao){

		setDao(dao);
	}

	public final TipoRegistroComplementoLogDAO getTipoRegistroComplementoLogDAO() {

		return (TipoRegistroComplementoLogDAO) getDao();
	}
}