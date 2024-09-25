package com.mercurio.lms.configuracoes.model.dao;

import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.AjusteDB;
import com.mercurio.lms.configuracoes.model.AjusteDBBind;

public class AjusteContingenciaDAO extends BaseCrudDao<AjusteDB, Long> {

    public int executeQuery(final String queryString, final Map<String, Object> parameters) {
        if (queryString != null) {
            return (Integer) getAdsmHibernateTemplate().execute(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    SQLQuery query = session.createSQLQuery(queryString);

                    if (parameters != null && !parameters.isEmpty()) {
                        for (Entry<String, Object> parameter : parameters.entrySet()) {
                            query.setParameter(parameter.getKey(), parameter.getValue());
                        }
                    }

                    return query.executeUpdate();
                }
            });
        }

        return 0;
    }

    public AjusteDBBind findBindById(Long idAjusteDBBind) {
        StringBuilder query = new StringBuilder();
        query.append(" select adbb ")
            .append("   from AjusteDBBind as adbb ")
            .append("  where adbb.id = ? " );

        return (AjusteDBBind) getAdsmHibernateTemplate().findUniqueResult(
                query.toString(), new Object[]{idAjusteDBBind});
    }

    @SuppressWarnings("unchecked")
    public ResultSetPage<AjusteDB> findPaginated(Map<String, Object> criteria) {
        int currentPage = Integer.parseInt((String) criteria.get("_currentPage"));
        int pageSize = Integer.parseInt((String) criteria.get("_pageSize"));

        return getAdsmHibernateTemplate().findPaginated(createDefaultHQLQuery(), currentPage, pageSize, criteria);
    }

    public Integer getRowCount() {
        return getAdsmHibernateTemplate().getRowCountForQuery(createDefaultHQLQuery());
    }

    private String createDefaultHQLQuery() {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" adb ");
        hql.append(" where adb.blAtivo = 'S' ");
        hql.append(" and adb.id <> 1 ");

        return hql.toString();
    }

    @SuppressWarnings("rawtypes")
    protected Class getPersistentClass() {
        return AjusteDB.class;
    }

}
