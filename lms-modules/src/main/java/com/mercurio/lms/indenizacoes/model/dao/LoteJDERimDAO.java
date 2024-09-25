package com.mercurio.lms.indenizacoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LoteJDERimDAO extends BaseCrudDao<LoteJDERimDAO, Long> {

	@Override
	protected Class getPersistentClass() {
		return LoteJDERimDAO.class;
	}

	
}