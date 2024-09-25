package com.mercurio.lms.tributos.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.TipoTributacaoIcmsLog;
import com.mercurio.lms.tributos.model.dao.TipoTributacaoIcmsLogDAO;

/**
 * @spring.bean id="lms.tributos.tipoTributacaoIcmsLogService"
 */
public class TipoTributacaoIcmsLogService extends CrudService<TipoTributacaoIcmsLog, Long> {

	public void setTipoTributacaoIcmsLogDAO(TipoTributacaoIcmsLogDAO dao){

		setDao( dao );
	}

	private TipoTributacaoIcmsLogDAO getTipoTributacaoIcmsLogDAO() {

		return (TipoTributacaoIcmsLogDAO) getDao();
	}
}