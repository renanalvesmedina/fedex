package com.mercurio.lms.integracao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.integracao.model.DominioNomeIntegracao;

/** 
 * @spring.bean 
 */
public class DominioNomeIntegracaoDAO extends BaseCrudDao<DominioNomeIntegracao, Long> {

	@Override
	protected Class getPersistentClass() {
		return DominioNomeIntegracao.class;
	}

}
