package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.LogCargaAwb;

public class LogCargaAwbDAO  extends BaseCrudDao<LogCargaAwb, Long> {

	@Override
	protected Class getPersistentClass() {
		return LogCargaAwb.class;
	}

	
	public List<LogCargaAwb> findByNrChave(String nrChave) {
		StringBuilder sql = new StringBuilder();
		
		sql	.append("SELECT lca ")
			.append(" FROM " + getPersistentClass().getSimpleName() + " lca")
			.append(" WHERE lca.nrChave = ?");
		
		return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{nrChave});
	}


	public List<LogCargaAwb> findByNrChaveAndDsMessage(String nrChave, String dsMensagem) {
		StringBuilder sql = new StringBuilder();
		
		sql	.append("SELECT lca ")
			.append(" FROM " + getPersistentClass().getSimpleName() + " lca")
			.append(" WHERE lca.nrChave = ? AND lca.dsMensagem = ?");
		
		return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{nrChave, dsMensagem});
	}
}
