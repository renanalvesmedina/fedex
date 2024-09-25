package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.EnderecoPessoaLog;
import com.mercurio.lms.configuracoes.model.dao.EnderecoPessoaLogDAO;

/**
 * @spring.bean id="lms.configuracoes.enderecoPessoaLogService"
 */
public class EnderecoPessoaLogService extends CrudService<EnderecoPessoaLog, Long> {

	public void setEnderecoPessoaLogDAO(EnderecoPessoaLogDAO dao){

		setDao( dao );
	}

	private EnderecoPessoaLogDAO getEnderecoPessoaLogDAO() {

		return (EnderecoPessoaLogDAO) getDao();
	}
}