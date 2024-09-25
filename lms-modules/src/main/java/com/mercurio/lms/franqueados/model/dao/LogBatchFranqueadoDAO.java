package com.mercurio.lms.franqueados.model.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.LogBatchFranqueado;

public class LogBatchFranqueadoDAO extends BaseCrudDao<LogBatchFranqueado, Long> {

	@Override
	protected Class<LogBatchFranqueado> getPersistentClass() {
		return LogBatchFranqueado.class;
	}

	public void storeNewSession(final LogBatchFranqueado log) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					
					session.beginTransaction();
					
					session.saveOrUpdate(log);
						
					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
	}
	
}
