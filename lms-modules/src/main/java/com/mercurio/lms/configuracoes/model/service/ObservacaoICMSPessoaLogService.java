package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ObservacaoICMSPessoaLog;
import com.mercurio.lms.configuracoes.model.dao.ObservacaoICMSPessoaLogDAO;

/**
 * @spring.bean id="lms.configuracoes.observacaoICMSPessoaLogService"
 */
public class ObservacaoICMSPessoaLogService extends CrudService<ObservacaoICMSPessoaLog, Long> {

	public void setObservacaoICMSPessoaLogDAO(ObservacaoICMSPessoaLogDAO dao){

		setDao( dao );
	}

	private ObservacaoICMSPessoaLogDAO getObservacaoICMSPessoaLogDAO() {

		return (ObservacaoICMSPessoaLogDAO) getDao();
	}
}