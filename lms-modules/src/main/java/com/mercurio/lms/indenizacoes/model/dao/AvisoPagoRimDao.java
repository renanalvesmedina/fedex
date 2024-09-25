package com.mercurio.lms.indenizacoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.indenizacoes.model.AvisoPagoRimInd;


public class AvisoPagoRimDao extends BaseCrudDao<AvisoPagoRimInd, Long> {
	
	@Override
	protected Class getPersistentClass() {		
		return AvisoPagoRimInd.class;
	}

}
