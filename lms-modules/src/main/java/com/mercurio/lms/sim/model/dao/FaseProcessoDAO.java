package com.mercurio.lms.sim.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;

public class FaseProcessoDAO extends BaseCrudDao<FaseProcesso, Long> {

	@Override
	protected Class getPersistentClass() {
		return FaseProcesso.class;
	}

	public List<FaseProcesso> find(FaseProcesso bean) {
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("cdFase", bean.getCdFase()));
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	} 
	
	public FaseProcesso findById(Long id) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.add(Restrictions.eq("idFaseProcesso", id));
				
		return (FaseProcesso)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public FaseProcesso findByIdDoctoServico(Long idDoctoServico) {
		StringBuffer hql = new StringBuffer()
		.append(" select fp ")
		.append("from ").append(DoctoServico.class.getName()).append(" ds, ")
		.append(LocalizacaoMercadoria.class.getName()).append(" lm, ")
		.append(FaseProcesso.class.getName()).append(" fp ")
		.append("where ds.localizacaoMercadoria = lm ")
		.append(" and lm.faseProcesso = fp ")
		.append(" and ds.idDoctoServico = ?");
		
	
		List<FaseProcesso> lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDoctoServico});
		if (lista.isEmpty())
			return null;

		return lista.get(0);
	}
		
	public boolean validateBean(FaseProcesso bean) {
		Integer cdFaseExists = (Integer) getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(getPersistentClass())
				.setProjection(Projections.rowCount())
				.add(Restrictions.eq("cdFase", bean.getCdFase()))
				.uniqueResult();
		
		if(bean.getIdFaseProcesso() == null) {
			return cdFaseExists.intValue() == 0;
		} else {
			Short cdFaseBD = (Short) getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(getPersistentClass())
			.setProjection(Projections.property("cdFase"))
			.add(Restrictions.eq("idFaseProcesso", bean.getIdFaseProcesso()))
			.uniqueResult();
			if(cdFaseExists.intValue() == 0 || cdFaseBD.equals(bean.getCdFase())){
				return true;
			}
		}
		return false;
	}
	
}
