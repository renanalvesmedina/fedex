package com.mercurio.lms.ppd.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.ppd.model.PpdJde;

public class PpdJdeDAO extends BaseCrudDao<PpdJde, Long>  {
				
	@Override
	protected Class getPersistentClass() {		
		return PpdJde.class;
	}
	
	public List<PpdJde> findPagamentosFromJde(String bitytn, String biedsp, String bimid){
		DetachedCriteria dc = DetachedCriteria.forClass(PpdJde.class, "jde");
		
		dc.add(Restrictions.eq("jde.bitytn", bitytn));   
		dc.add(Restrictions.eq("jde.biedsp", biedsp));
		dc.add(Restrictions.like("jde.bimid", bimid));
		
		List<PpdJde> listPagamentosJde = super.findByDetachedCriteria(dc);
				
		return listPagamentosJde;
}	
	
}	
