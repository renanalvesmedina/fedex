package com.mercurio.lms.sim.model.dao;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.sim.model.TemplateRelatorio;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class TemplateRelatorioDAO extends BaseCrudDao<TemplateRelatorio, Long>{

	@Override
	protected Class<TemplateRelatorio> getPersistentClass() {
		return TemplateRelatorio.class;
	}
    
    public TemplateRelatorio findById(Long id) {
    	return super.findById(id);
    }
    
	public Blob findArquivo(Long idTemplateRelatorio) {
		Session session = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select dc_arquivo ");
		sql.append("from template_relatorios tp ");
		sql.append("where  tp.id_template_relatorio = :idTemplateRelatorio ");

		final ConfigureSqlQuery csq1 = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("dc_arquivo", Hibernate.BLOB);
			}
		};
		
		SQLQuery queryConsulta = session.createSQLQuery(sql.toString());
		queryConsulta.setParameter("idTemplateRelatorio", idTemplateRelatorio);
		csq1.configQuery(queryConsulta);
				
		return (Blob) queryConsulta.uniqueResult();
	}
    
    public ResultSetPage<TemplateRelatorio> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as tr ");
		query.append(" where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if (MapUtils.getObject(criteria, "nmTemplate") != null) {
			query.append(" and tr.nmTemplate like :nmTemplate ");
		}
		if (MapUtils.getObject(criteria, "tpRelatorio") != null) {
			query.append(" and tr.tpRelatorio = :tpRelatorio ");
		}
		
		query.append(" order by tr.nmTemplate ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

	public Serializable update(final TemplateRelatorio templateRelatorio) {
		HibernateCallback updateTemplateRelatorio = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String update = " update TemplateRelatorio as tr set tr.nmTemplate = ? , tr.tpRelatorio = ? where tr.idTemplate = ? ";
				session.createQuery(update)
						.setString(0, templateRelatorio.getNmTemplate())
						.setString(1, templateRelatorio.getTpRelatorio().getValue())
						.setLong(2, templateRelatorio.getIdTemplate())
						.executeUpdate();
				return new Object();
			}
		};
		getAdsmHibernateTemplate().execute(updateTemplateRelatorio);
		return findById(templateRelatorio.getIdTemplate());
	}
}
