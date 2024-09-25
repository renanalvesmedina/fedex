package com.mercurio.lms.configuracoes.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.TelefoneEnderecoLog;
import com.mercurio.lms.configuracoes.model.dao.TelefoneEnderecoLogDAO;

/**
 * @spring.bean id="lms.configuracoes.telefoneEnderecoLogService"
 */
public class TelefoneEnderecoLogService extends CrudService<TelefoneEnderecoLog, Long> {

	public void setTelefoneEnderecoLogDAO(TelefoneEnderecoLogDAO dao){

		setDao( dao );
	}

	private TelefoneEnderecoLogDAO getTelefoneEnderecoLogDAO() {

		return (TelefoneEnderecoLogDAO) getDao();
	}
}