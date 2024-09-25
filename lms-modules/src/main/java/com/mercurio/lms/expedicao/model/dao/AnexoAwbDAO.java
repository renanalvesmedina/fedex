package com.mercurio.lms.expedicao.model.dao;

import java.sql.Blob;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.AnexoAwb;
import com.mercurio.lms.expedicao.model.Awb;

public class AnexoAwbDAO extends BaseCrudDao<AnexoAwb, Long>{
	
	private static final String TP_ANEXO_PDF = "P";

	@Override
	protected Class getPersistentClass() {
		return AnexoAwb.class;
	}
	
	public Blob findPdfAnexo(Long idAwb) {
		Session session = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select dc_anexo ");
		sql.append("from anexo_awb aa ");
		sql.append("where  aa.id_awb = :idAwb ");
		sql.append("and  aa.tp_anexo = :tpAnexo ");
		sql.append("order by id_anexo_awb ");

		final ConfigureSqlQuery csq1 = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("dc_anexo", Hibernate.BLOB);
			}
		};
		
		SQLQuery queryConsulta = session.createSQLQuery(sql.toString());
		queryConsulta.setParameter("idAwb", idAwb);
		queryConsulta.setParameter("tpAnexo", TP_ANEXO_PDF);
		csq1.configQuery(queryConsulta);
		
		List list = queryConsulta.list();
		
		if(list != null && !list.isEmpty()){
			return (Blob) list.get(0);
		}		
		return null;
	}

}
