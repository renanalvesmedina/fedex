package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.FaturaCloud;

public class FaturaCloudDAO extends BaseCrudDao<FaturaCloud, Long> {


	
	@Override
	protected Class getPersistentClass() {
		return FaturaCloudDAO.class;
		
	}

	public FaturaCloud findByFatura(Long idFatura) {
		StringBuilder sb = new StringBuilder()
			.append(" from FaturaCloud as fc ")
			.append("where fc.fatura.id = ?  ")
			.append("order by fc.id desc     ");

		List<FaturaCloud> result = getAdsmHibernateTemplate().find(sb.toString(), new Object[] { idFatura });

		return result.isEmpty() ? null : result.get(0);
		
	}

    public FaturaCloud storeBasic(FaturaCloud faturaCloud){
		getAdsmHibernateTemplate().saveOrUpdate(faturaCloud);
		
		return faturaCloud;
		
    }

	@Override
	public void flush() {
		getAdsmHibernateTemplate().flush();
		
    }
}
