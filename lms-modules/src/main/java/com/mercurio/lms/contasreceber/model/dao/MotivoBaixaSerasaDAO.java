package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.MotivoBaixaSerasa;

public class MotivoBaixaSerasaDAO extends BaseCrudDao<MotivoBaixaSerasa, Long> {

	
    protected final Class getPersistentClass() {
        return MotivoBaixaSerasa.class;
    }
	
	public MotivoBaixaSerasa store(final MotivoBaixaSerasa loteSerasa) {
		super.store(loteSerasa, true);
		return loteSerasa;
	}
	
	public MotivoBaixaSerasa findById(Long id){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(MotivoBaixaSerasa.class.getName());
		sql.addCriteria("idMotivoBaixaSerasa", "=", id);
		List l = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if ( l == null || l.isEmpty() ) {
			return null;
		}
		return (MotivoBaixaSerasa) l.get(0);
	}

	public List findMotivosBaixa() {
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(MotivoBaixaSerasa.class.getName());
		return getAdsmHibernateTemplate().find(sql.getSql());
	}

}