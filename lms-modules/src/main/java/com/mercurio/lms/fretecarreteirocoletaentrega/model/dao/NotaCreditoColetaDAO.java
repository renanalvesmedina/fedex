package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoColeta;

public class NotaCreditoColetaDAO extends BaseCrudDao<NotaCreditoColeta, Long> {

	@Override
	protected Class<NotaCreditoColeta> getPersistentClass() {
		return NotaCreditoColeta.class;
	}

    @SuppressWarnings("unchecked")
    public List<NotaCreditoColeta> findByIdNotaCredito(Long idNotaCredito) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        criteria.setFetchMode("notaCredito", FetchMode.JOIN)
                .createAlias("notaCredito", "nc");
        criteria.add(Restrictions.eq("nc.idNotaCredito", idNotaCredito));

        return (List<NotaCreditoColeta>) findByDetachedCriteria(criteria);
    }

    public Integer findQuantidadeColetasEfetuadasControleCarga(Long idControleCarga) {
        StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" ncc ");
        hql.append(" join ncc.notaCredito nc ");
        hql.append(" join nc.controleCarga cc ");
        hql.append(" where cc.idControleCarga = ? ");

        return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[]{ idControleCarga });
    }

	public Long findCountQtColetasExecutadasByIdNotaCredito(Long idNotaCredito) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select count(ncco) ");
		hql.append(" from NotaCreditoColeta as ncco ");
		hql.append(" join ncco.notaCredito as nocr ");
		hql.append(" where nocr.idNotaCredito = ? ");
		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idNotaCredito});
	}

}
