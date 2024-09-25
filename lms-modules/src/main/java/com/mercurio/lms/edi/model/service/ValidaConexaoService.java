package com.mercurio.lms.edi.model.service;

import com.mercurio.lms.edi.model.dao.ValidaConexaoDAO;


/**
 * Classe de servi�o para
 *
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.edi.validaConexaoService"
 */

public class ValidaConexaoService {
	private ValidaConexaoDAO dao;

    public Boolean validateConexao() {
    	return getDao().validateConexao();
    }

	public ValidaConexaoDAO getDao() {
		return dao;
	}

	public void setDao(ValidaConexaoDAO dao) {
		this.dao = dao;
	}
}
