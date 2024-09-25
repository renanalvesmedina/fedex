package com.mercurio.lms.ppd.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.ppd.model.PpdReciboNumeracao;

public class PpdReciboNumeracaoDAO extends BaseCrudDao<PpdReciboNumeracao, Long>  {

	@Override
	protected Class getPersistentClass() {		
		return PpdReciboNumeracao.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {		
		lazyFindById.put("filial", FetchMode.JOIN);			
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
	}
	
	public void store(PpdReciboNumeracao ppdReciboNumeracao) {
		super.store(ppdReciboNumeracao);
	}
		
	public PpdReciboNumeracao findById(Long id) {
		return (PpdReciboNumeracao)super.findById(id);
	}
	
	public PpdReciboNumeracao findByIdFilial(Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setFetchMode("filial", FetchMode.JOIN);		
		dc.add(Restrictions.eq("filial.idFilial", idFilial));		
		return (PpdReciboNumeracao)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
}	
