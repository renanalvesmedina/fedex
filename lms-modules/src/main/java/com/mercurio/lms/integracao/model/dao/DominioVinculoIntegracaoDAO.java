package com.mercurio.lms.integracao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.integracao.model.DominioVinculoIntegracao;


/** 
 * @spring.bean 
 */
public class DominioVinculoIntegracaoDAO extends BaseCrudDao<DominioVinculoIntegracao, Long> {
	
	@Override
	protected Class getPersistentClass() {
		return DominioVinculoIntegracao.class;
	}

}
