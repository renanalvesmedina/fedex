package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.ItemRelacaoPagtoParcial;

public class ItemRelacaoPagtoParcialDAO extends BaseCrudDao<ItemRelacaoPagtoParcial, Long>{

	@Override
	protected Class<ItemRelacaoPagtoParcial> getPersistentClass() {
		return ItemRelacaoPagtoParcial.class;
	}

	@SuppressWarnings("unchecked")
	public List<ItemRelacaoPagtoParcial> findByIdDevedorDocServFat(Long idDevedorDocServFat) {
		Criteria c = getSession().createCriteria(persistentClass);
		c.add(Expression.eq("devedorDocServFat.id",idDevedorDocServFat));
		return c.list();
	}

}
