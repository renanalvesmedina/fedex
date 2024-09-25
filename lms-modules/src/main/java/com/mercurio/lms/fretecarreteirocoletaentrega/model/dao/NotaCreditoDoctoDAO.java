package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;

public class NotaCreditoDoctoDAO extends BaseCrudDao<NotaCreditoDocto, Long> {

	@Override
	protected Class<NotaCreditoDocto> getPersistentClass() {
		return NotaCreditoDocto.class;
	}

	@SuppressWarnings("unchecked")
    public List<NotaCreditoDocto> findByIdNotaCredito(Long idNotaCredito) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        criteria.setFetchMode("notaCredito", FetchMode.JOIN)
                .createAlias("notaCredito", "nc");
        criteria.add(Restrictions.eq("nc.idNotaCredito", idNotaCredito));

        return (List<NotaCreditoDocto>) findByDetachedCriteria(criteria);
    }

	@SuppressWarnings("unchecked")
    public List<NotaCreditoDocto> findByIdDoctoServico(Long idDoctoServico){
		return getSession().createCriteria(getPersistentClass(), "notaCreditoDocto").add(
                Restrictions.eq("notaCreditoDocto.doctoServico.id", idDoctoServico)).list();
	}

	public Integer findQuantidadeEntregasEfetuadasControleCarga(Long idControleCarga) {
	    StringBuffer hql = new StringBuffer(" from ");
        hql.append(getPersistentClass().getName()).append(" ncd ");
        hql.append(" join ncd.notaCredito nc ");
        hql.append(" join nc.controleCarga cc ");
        hql.append(" where cc.idControleCarga = ? ");

        return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[]{ idControleCarga });
	}

	public Long findCountQtEntregasRealizadasByIdNotaCredito(Long idNotaCredito) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select count(ncdo) ");
		hql.append(" from NotaCreditoDocto as ncdo ");
		hql.append(" join ncdo.notaCredito as nocr ");
		hql.append(" where nocr.idNotaCredito = ? ");
		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idNotaCredito});
	}
}
