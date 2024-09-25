package com.mercurio.lms.configuracoes.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.DispositivoContato;
import org.hibernate.criterion.Restrictions;

public class DispositivoContatoDAO extends BaseCrudDao<DispositivoContato, Long> {

	@Override
	protected Class getPersistentClass() {
		return DispositivoContato.class;
	}

	public DispositivoContato findDispositivo(String token, String plataforma, String ddd, String numero) {
		return (DispositivoContato) getSession()
				.createCriteria(DispositivoContato.class)
				.add(Restrictions.eq("dsToken", token))
                .add(Restrictions.eq("tpPlataforma", plataforma))
                .add(Restrictions.eq("nrDdd", ddd))
                .add(Restrictions.eq("nrTelefone", numero))
				.uniqueResult();
	}

	public DispositivoContato findByToken(String token) {
		return (DispositivoContato) getSession()
				.createCriteria(DispositivoContato.class)
				.add(Restrictions.eq("dsToken", token))
				.uniqueResult();
	}
}
