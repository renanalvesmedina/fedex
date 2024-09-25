package com.mercurio.lms.sim.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.sim.model.EventoMonitoramentoCCT;

public class EventoMonitoramentoCCTDAO extends BaseCrudDao<EventoMonitoramentoCCT, Long> {

    @Override
    protected Class<EventoMonitoramentoCCT> getPersistentClass() {
	return EventoMonitoramentoCCT.class;
    }

    @SuppressWarnings("rawtypes")
    public List findByIdMonitoramentoCCTAndTpSituacao(Long idMonitoramentoCCT, boolean hasOrderByDhInclusaoAsc,
	    String[] fetches, String... tpSituacao) {
	Criteria c = getSession().createCriteria(getPersistentClass());
	c.add(Restrictions.eq("monitoramentoCCT.idMonitoramentoCCT", idMonitoramentoCCT));
	
	if(tpSituacao != null) {
	    c.add(Restrictions.in("tpSituacaoNotaFiscalCCT", tpSituacao));
	}
	
	if (fetches != null && fetches.length != 0) {
	    for (int i = 0; i < fetches.length; i++) {
		c.setFetchMode(fetches[i], FetchMode.JOIN);
	    }
	}
	
	if (hasOrderByDhInclusaoAsc) {
	    c.addOrder(Order.asc("dhInclusao"));
	}
	else {
	    c.addOrder(Order.desc("dhInclusao"));
	}
	
	return c.list();
    }
    
    @SuppressWarnings("rawtypes")
    public List findByIdMonitoramentoCCTAndTpSituacao(Long idMonitoramentoCCT, boolean hasOrderByDhInclusaoAsc,
	    String[] fetches) {
	Criteria c = getSession().createCriteria(getPersistentClass());
	c.add(Restrictions.eq("monitoramentoCCT.idMonitoramentoCCT", idMonitoramentoCCT));
	
	if (fetches != null && fetches.length != 0) {
	    for (int i = 0; i < fetches.length; i++) {
		c.setFetchMode(fetches[i], FetchMode.JOIN);
	    }
	}
	
	if (hasOrderByDhInclusaoAsc) {
	    c.addOrder(Order.asc("dhInclusao"));
	}
	else {
	    c.addOrder(Order.desc("dhInclusao"));
	}
	
	return c.list();
    }

    @SuppressWarnings("rawtypes")
    public List findEventoMonitoramentoCCT(TypedFlatMap criteria) {
		List param = new ArrayList();
		
		StringBuilder sql = new StringBuilder();
	    sql.append(" SELECT new map( ")
	    .append(" 	emcct.idEventoMonitoramentoCCT 	AS idEventoMonitoramentoCCT, ")
	    .append(" 	emcct.dhInclusao 				AS dhInclusao, ")
	    .append(" 	usu.nmUsuario 					AS nmUsuario, ")
	    .append(" 	emcct.tpSituacaoNotaFiscalCCT 	AS tpSituacaoNotaFiscalCCT, ")
	    .append(" 	emcct.dsComentario 				AS dsComentario) ")
		.append(" FROM ")
		.append(getPersistentClass().getName()).append(" AS emcct, ")
		.append(Usuario.class.getName()).append(" AS usu")
		.append(" WHERE ")
		.append(" 	emcct.monitoramentoCCT.idMonitoramentoCCT = ? ")
		.append(" 	AND emcct.usuario.idUsuario = usu.idUsuario ")
		.append(" ORDER BY emcct.dhInclusao.value desc");
	
		param.add(criteria.getLong("idMonitoramentoCCT"));
		
		List rsp = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());

		return rsp;
    }
    
    @SuppressWarnings("rawtypes")
    public List findNmUsuarioEventoByIdMonitoramentoCCT(Long idMonitoramentoCCT) {    	
    	List param = new ArrayList();		
    	StringBuilder sql = new StringBuilder();
	    sql.append(" SELECT new map( ")
	    	.append(" usu.nmUsuario AS nmUsuario) ")
	    	.append(" FROM ")	
	    	.append(getPersistentClass().getName()).append(" AS emcct, ")
	    	.append(Usuario.class.getName()).append(" AS usu")
	    	.append(" WHERE ")
	    	.append(" 	emcct.monitoramentoCCT.idMonitoramentoCCT = ? ")
	    	.append(" 	AND emcct.usuario.idUsuario = usu.idUsuario ")
	    	.append(" ORDER BY emcct.dhInclusao.value DESC");
		param.add(idMonitoramentoCCT);		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }

    @SuppressWarnings("rawtypes")
	public List findMonitoramentoCCTByConfirmacaoAgendamento(Long id) {
		List<Long> param = new ArrayList<Long>();
		StringBuilder sql = new StringBuilder();
	    sql.append(" SELECT emcct ")
		.append(" FROM ")	
		.append(getPersistentClass().getName()).append(" AS emcct ")
		.append(" WHERE ")
		.append(" emcct.monitoramentoCCT.idMonitoramentoCCT = ? ")
		.append(" and emcct.tpSituacaoNotaFiscalCCT = 'AG' ")
			.append(" AND exists (select emcct2 from ")
			.append(getPersistentClass().getName()).append(" AS emcct2 ")
			.append(" where emcct2.tpSituacaoNotaFiscalCCT = 'AA' ")
			.append(" and emcct2.monitoramentoCCT.idMonitoramentoCCT = ? ) ")
		.append(" ORDER BY emcct.dhInclusao.value  DESC ");
		param.add(id);
		param.add(id);
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}

}
