package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.InscricaoEstadualLog;
import com.mercurio.lms.configuracoes.model.dao.InscricaoEstadualLogDAO;

/**
 * @spring.bean id="lms.configuracoes.inscricaoEstadualLogService"
 */
public class InscricaoEstadualLogService extends CrudService<InscricaoEstadualLog, Long> {

	public void setInscricaoEstadualLogDAO(InscricaoEstadualLogDAO dao){

		setDao( dao );
	}

	private InscricaoEstadualLogDAO getInscricaoEstadualLogDAO() {

		return (InscricaoEstadualLogDAO) getDao();
	}
}