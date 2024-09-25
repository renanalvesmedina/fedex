package com.mercurio.lms.tributos.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.TipoTributacaoUfLog;
import com.mercurio.lms.tributos.model.dao.TipoTributacaoUfLogDAO;

/**
 * @spring.bean id="lms.tributos.tipoTributacaoUfLogService"
 */
public class TipoTributacaoUfLogService extends CrudService<TipoTributacaoUfLog, Long> {

	private UnidadeFederativaService unidadeFederativaService;

	public final void setTipoTributacaoUfLogDAO(TipoTributacaoUfLogDAO dao){

		setDao(dao);
	}

	public final TipoTributacaoUfLogDAO getTipoTributacaoUfLogDAO() {

		return (TipoTributacaoUfLogDAO) getDao();
	}

	public List findLookupUF(Map criteria) {

		 return unidadeFederativaService.findLookup(criteria);
	 }
}