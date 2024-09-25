package com.mercurio.lms.expedicao.model.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.OrdemServicoDocumento;

public class OrdemServicoDocumentoDAO extends BaseCrudDao<OrdemServicoDocumento, Long> {

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return OrdemServicoDocumento.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdemServicoDocumento> findByOrdemServico(Long idOrdemServico) {
		StringBuilder query = new StringBuilder()
		.append("from " + getPersistentClass().getName() + " as docto ")				
		.append("where ")
		.append("  docto.ordemServico.idOrdemServico = ? ");
				
		return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idOrdemServico});			
	}
	
	public void removeByIdOrdemServicoByNotInIds(final Long idOrdemServico, final List<Long> ids) {
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = null;
				String hqlDelete = " delete from " + getPersistentClass().getName() +  " as osd" +
								   " where osd.ordemServico.idOrdemServico = :idOrdemServico";
				if (ids != null && !ids.isEmpty()) {
					hqlDelete += " and osd.idOrdemServicoDocumento  not in  (:ids)" ;
				}
				query = session.createQuery(hqlDelete);
				if (ids != null && !ids.isEmpty()) {
					query.setParameterList("ids", ids);
				}
				query.setLong("idOrdemServico", idOrdemServico);
				
				query.executeUpdate();
		
				return null;
			}
		});
	}
}
