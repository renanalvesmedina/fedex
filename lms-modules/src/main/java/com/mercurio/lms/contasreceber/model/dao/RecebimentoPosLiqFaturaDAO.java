package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.RecebimentoPosLiqFatura;

public class RecebimentoPosLiqFaturaDAO extends BaseCrudDao<RecebimentoPosLiqFatura, Long> {

	@Override
	protected Class getPersistentClass() {
		return RecebimentoPosLiqFatura.class;
	}
	
	public RecebimentoPosLiqFatura findByIdFatura(Long idFatura){
		List recebimentos = getAdsmHibernateTemplate().find("from "+RecebimentoPosLiqFatura.class.getName()+" where fatura.idFatura="+idFatura);
		if ( recebimentos == null || recebimentos.isEmpty() ){
			return null;
		}
		return (RecebimentoPosLiqFatura) recebimentos.get(0);
	}

}
