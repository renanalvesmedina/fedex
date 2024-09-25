package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ClienteProcessar;

public class ClienteProcessarDAO extends BaseCrudDao<ClienteProcessar, Long>{

	@Override
	protected Class getPersistentClass() {
		return ClienteProcessar.class;
	}

	public void store(ClienteProcessar clienteProcessar){
		super.store(clienteProcessar);
	}

	public List<ClienteProcessar> findClientesProcessar(Long idRecalculoFrete) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("recalculoFrete.id", idRecalculoFrete));
		
		return findByDetachedCriteria(dc);		
	}
	
}
