package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.AnexoDoctoServicoLogDAO;
import com.mercurio.lms.expedicao.model.AnexoDoctoServicoLog;

/**
 * @spring.bean id="lms.expedicao.anexoDoctoServicoLogService"
 */
public class AnexoDoctoServicoLogService extends CrudService<AnexoDoctoServicoLog, Long> {

	public final void setAnexoDoctoServicoLogDAO(AnexoDoctoServicoLogDAO dao){

		setDao(dao);
	}

	public final AnexoDoctoServicoLogDAO getAnexoDoctoServicoLogDAO() {

		return (AnexoDoctoServicoLogDAO) getDao();
	}
}