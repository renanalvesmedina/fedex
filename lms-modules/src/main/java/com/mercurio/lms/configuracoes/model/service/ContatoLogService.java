package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ContatoLog;
import com.mercurio.lms.configuracoes.model.dao.ContatoLogDAO;

/**
 * @spring.bean id="lms.configuracoes.contatoLogService"
 */
public class ContatoLogService extends CrudService<ContatoLog, Long> {

	public void setContatoLogDAO(ContatoLogDAO dao){

		setDao( dao );
	}

	private ContatoLogDAO getContatoLogDAO() {

		return (ContatoLogDAO) getDao();
	}
}