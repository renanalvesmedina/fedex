package com.mercurio.lms.ppd.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.ppd.model.AvisoPagoRim;
import com.mercurio.lms.ppd.model.PpdRecibo;


public class AvisoPagoRimDao extends BaseCrudDao<AvisoPagoRim, Long> {
	
	@Override
	protected Class getPersistentClass() {		
		return AvisoPagoRim.class;
	}

	public List<AvisoPagoRim> findAvisoPagoRim(PpdRecibo reciboIndenizacao, String blRetornou) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("av");
		
		hql.addLeftOuterJoin(getPersistentClass().getName(),"av");
		hql.addLeftOuterJoin("av.reciboIndenizacao","re");
		hql.addCriteria("re.idRecibo","=",reciboIndenizacao.getIdRecibo());
		hql.addCriteria("av.blRetornou","=",(blRetornou=="S" ? true: false));
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

}
