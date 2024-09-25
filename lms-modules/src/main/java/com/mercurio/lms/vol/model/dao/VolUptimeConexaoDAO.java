package com.mercurio.lms.vol.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vol.model.VolUptimeConexao;

public class VolUptimeConexaoDAO extends BaseCrudDao<VolUptimeConexao, Long>{

	@Override
	protected Class getPersistentClass() {
		return VolUptimeConexao.class;
	}
	
	

}
