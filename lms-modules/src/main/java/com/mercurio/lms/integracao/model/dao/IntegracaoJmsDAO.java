package com.mercurio.lms.integracao.model.dao;


import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.integracao.model.IntegracaoFilaJms;

/** 
 * @spring.bean 
 */
public class IntegracaoJmsDAO extends BaseCrudDao<IntegracaoFilaJms, Long> {

	
	@Override
	protected Class getPersistentClass() {
		return IntegracaoFilaJms.class;
	}
	 
}
