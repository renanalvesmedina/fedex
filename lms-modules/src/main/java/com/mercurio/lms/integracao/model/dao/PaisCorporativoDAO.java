package com.mercurio.lms.integracao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.integracao.model.PaisCorporativo;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PaisCorporativoDAO extends BaseCrudDao<PaisCorporativo, Long>{

	@Override
	protected Class getPersistentClass() {
		return PaisCorporativo.class;
	}

}
