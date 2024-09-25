package com.mercurio.lms.entrega.model.dao;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.lms.entrega.model.ReciboReembolso;

/**
 * 
 * @author LuisMAN
 * @spring.bean
 *
 */
public class GerarReciboReembolsoDAO extends AdsmDao {
	
	public java.io.Serializable store(ReciboReembolso rb) {
		
		getAdsmHibernateTemplate().save(rb);
		getSession().flush();
		
		return rb.getIdDoctoServico();
	}
	

}
