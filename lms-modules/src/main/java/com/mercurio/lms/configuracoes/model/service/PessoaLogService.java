package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.PessoaLog;
import com.mercurio.lms.configuracoes.model.dao.PessoaLogDAO;

/**
 * @spring.bean id="lms.configuracoes.pessoaLogService"
 */
public class PessoaLogService extends CrudService<PessoaLog, Long> {

	public void setPessoaLogDAO(PessoaLogDAO dao){

		setDao( dao );
	}

	private PessoaLogDAO getPessoaLogDAO() {

		return (PessoaLogDAO) getDao();
	}
}