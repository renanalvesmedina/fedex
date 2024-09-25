package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ClienteNaoProcessar;

public class ClienteNaoProcessarDAO extends BaseCrudDao<ClienteNaoProcessar, Long>{

	@Override
	protected Class getPersistentClass() {
		return ClienteNaoProcessar.class;
	}

	public void store(ClienteNaoProcessar clienteNaoProcessar){
		super.store(clienteNaoProcessar);
	}

	public List<ClienteNaoProcessar> findClientesNaoProcessar(Long idRecalculoFrete) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("recalculoFrete.id", idRecalculoFrete));
		
		return findByDetachedCriteria(dc);
	}
	
}
