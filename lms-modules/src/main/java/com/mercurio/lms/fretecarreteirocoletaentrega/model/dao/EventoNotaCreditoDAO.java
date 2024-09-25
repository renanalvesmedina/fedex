package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.EventoNotaCredito;

public class EventoNotaCreditoDAO extends BaseCrudDao<EventoNotaCredito, Long> {
    
    @SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
        return EventoNotaCredito.class;
    }

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedEventos(TypedFlatMap criteria, FindDefinition findDefinition) {
		SqlTemplate hql = this.getHqlForFindPaginatedEventos(criteria);		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCountEventos(TypedFlatMap criteria) {
		SqlTemplate hql = this.getHqlForFindPaginatedEventos(criteria);		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
	}
	
	private SqlTemplate getHqlForFindPaginatedEventos(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(eventoNotaCredito.idEventoNotaCredito", "idEventoNotaCredito");
		hql.addProjection("eventoNotaCredito.tpOrigemEvento", "tpOrigemEvento");	
		hql.addProjection("eventoNotaCredito.tpComplementoEvento", "tpComplementoEvento");
		hql.addProjection("eventoNotaCredito.dhEvento", "dhEvento");
		hql.addProjection("usuario.usuarioADSM.nmUsuario", "nmUsuario)");
		
    	StringBuilder from = new StringBuilder(EventoNotaCredito.class.getName()).append(" AS eventoNotaCredito ")
    		.append("LEFT JOIN eventoNotaCredito.usuario AS usuario ");
    	
    	hql.addFrom(from.toString());
    	
    	hql.addCriteria("eventoNotaCredito.notaCredito.idNotaCredito","=", criteria.getLong("idNotaCredito"));
		
    	hql.addOrderBy("eventoNotaCredito.dhEvento");
    	
    	return hql;
	}
    
}