package com.mercurio.lms.sim.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;

public class AgendamentoMonitCCTDAO extends BaseCrudDao<AgendamentoMonitCCT, Long> {

	@Override
	protected final Class<AgendamentoMonitCCT> getPersistentClass() {
		return AgendamentoMonitCCT.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<AgendamentoMonitCCT> findAgendamentoMonitCCTByAgendamentoEntrega(Long idAgendamentoEntrega) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.createAlias("agendamentoEntrega", "ag");
		dc.add(Restrictions.eq("ag.idAgendamentoEntrega", idAgendamentoEntrega));
		return this.getAdsmHibernateTemplate().findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<AgendamentoMonitCCT> findAgendamentoMonitCCTByMonitCCT(Long idMonitoramentoCCT) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.createAlias("monitoramentoCCT", "mcct");
		dc.createAlias("agendamentoEntrega", "ag");
		dc.add(Restrictions.eq("mcct.idMonitoramentoCCT", idMonitoramentoCCT));
		dc.add(Restrictions.not(Restrictions.in("ag.tpSituacaoAgendamento", new String[] {"C", "R"})));
		dc.add(Restrictions.not(Restrictions.eq("ag.tpAgendamento", "TA")));
		dc.addOrder(Order.desc("ag.idAgendamentoEntrega"));
		return this.getAdsmHibernateTemplate().findByCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public AgendamentoMonitCCT findAgendamentoMonitCCTByNrChave(String chaveMonitoramentoCCT) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "amcct");
		dc.createAlias("amcct.agendamentoEntrega", "ag", Criteria.LEFT_JOIN);
		dc.createAlias("amcct.monitoramentoCCT", "mcct");
		dc.createAlias("mcct.doctoServico", "ds");
		dc.add(Restrictions.eq("mcct.nrChave", chaveMonitoramentoCCT));
		dc.add(Restrictions.eq("ag.tpSituacaoAgendamento", "A"));
		
		return (AgendamentoMonitCCT) this.getAdsmHibernateTemplate().findUniqueResult(dc);
	}	
	
}