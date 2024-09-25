package com.mercurio.lms.indenizacoes.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.indenizacoes.model.Jde;

public class JdeDAO extends BaseCrudDao<Jde, Long>  {
				
	@Override
	protected Class getPersistentClass() {		
		return Jde.class;
	}
	
	
	public List<Jde> findPagamentosFromJde(String bitytn, String biedsp, String bimid){
		DetachedCriteria dc = DetachedCriteria.forClass(Jde.class, "jde");
		
		dc.add(Restrictions.eq("jde.bitytn", bitytn));   
		dc.add(Restrictions.eq("jde.biedsp", biedsp));
		dc.add(Restrictions.like("jde.bimid", bimid));
		
		List<Jde> listPagamentosJde = super.findByDetachedCriteria(dc);
				
		return listPagamentosJde;
	}
	
	
}	
