package com.mercurio.lms.edi.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.AdsmDao;


/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */

public class ValidaConexaoDAO extends AdsmDao {

	public Boolean validateConexao() {
		List result = (List) getAdsmHibernateTemplate().find("select sysdate from DoctoServico where rownum = 1");

		return result != null && result.size() > 0;
	}
}

