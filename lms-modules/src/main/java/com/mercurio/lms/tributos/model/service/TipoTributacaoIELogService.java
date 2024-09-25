package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.TipoTributacaoIELog;
import com.mercurio.lms.tributos.model.dao.TipoTributacaoIELogDAO;

/**
 * @spring.bean id="lms.tributos.tipoTributacaoIELogService"
 */
public class TipoTributacaoIELogService extends CrudService<TipoTributacaoIELog, Long> {

	public void setTipoTributacaoIELogDAO(TipoTributacaoIELogDAO dao){

		setDao( dao );
	}

	private TipoTributacaoIELogDAO getTipoTributacaoIELogDAO() {

		return (TipoTributacaoIELogDAO) getDao();
	}
}