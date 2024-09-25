package com.mercurio.lms.workflow.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.workflow.model.EventoWorkflow;
import com.mercurio.lms.workflow.model.TipoEvento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoWorkflowDAO extends BaseCrudDao<EventoWorkflow, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EventoWorkflow.class;
    }
    
    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
    	
    	DetachedCriteria dc = findDef.getDetachedCriteria();
    	
    	if (dc == null){
    		dc = createDetachedCriteria();
    	}
    	dc.createAlias("comite", "c");
    	dc.createAlias("tipoEvento", "te");

    	String comite = (String) ReflectionUtils.getNestedBeanPropertyValue(criteria, "comite.idComite");
    	if (!comite.equals("")){
    		criteria.remove("comite");
			dc.add(Restrictions.eq("c.id", Long.valueOf(comite)));
    	}
    	
    	String tipoEvento = (String) ReflectionUtils.getNestedBeanPropertyValue(criteria, "tipoEvento.idTipoEvento");
    	if (!tipoEvento.equals("")){
    		dc.add(Restrictions.eq("te.id", Long.valueOf(tipoEvento)));
    	}
    	
    	//tratando o nrEvento
    	String nrTipoEvento = (String) ReflectionUtils.getNestedBeanPropertyValue(criteria, "tipoEvento.nrTipoEvento");
    	if (!nrTipoEvento.equals("")){
    		dc.add(Restrictions.eq("te.nrTipoEvento", Short.valueOf(nrTipoEvento)));
    	}
    	
    	if (!"".equals( nrTipoEvento ) || !"".equals( tipoEvento ) ){
    		criteria.remove("tipoEvento");	
    	}
    	
    	dc.addOrder(OrderVarcharI18n.asc("c.nmComite", LocaleContextHolder.getLocale()));
    	dc.addOrder(Order.asc("te.nrTipoEvento"));
    	dc.addOrder(OrderVarcharI18n.asc("te.dsTipoEvento", LocaleContextHolder.getLocale()));
    	
    	findDef.setDetachedCriteria(dc);
    	
    	return super.findPaginated(criteria, findDef);
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("comite", FetchMode.JOIN);
    	lazyFindPaginated.put("tipoEvento", FetchMode.JOIN);
    }
   
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("comite", FetchMode.JOIN);
    	lazyFindById.put("tipoEvento", FetchMode.JOIN);
    }

    public EventoWorkflow findByTipoEvento(Short nrTipoEvento){
		SqlTemplate sql = new SqlTemplate();		
		sql.addProjection("ew");
		sql.addFrom(TipoEvento.class.getName(), "tpe " +
											"join tpe.eventoWorkflow as ew " +
											"join fetch ew.comite as co ");	
		sql.addCriteria("tpe.nrTipoEvento","=",nrTipoEvento);			
		List list = this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if (!list.isEmpty()) {
			return (EventoWorkflow)list.get(0);
		} else {
			return null;
		}
    }
    
    

    /**
     * Retorna uma lista EventosWorkflow que se resultem do filtro
     * @param Map map
     * @return List
     */    
    public List findLookupEventoWorkflow(Map map){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("ew");
    	sql.addFrom(EventoWorkflow.class.getName() + " ew join fetch ew.tipoEvento te ");
    	sql.addCriteria("te.nrTipoEvento","=",map.get("tipoEvento.nrTipoEvento"), Short.class);
    	sql.addCriteria("ew.tpSituacao","like",map.get("tpSituacao"));    	
    	sql.addOrderBy(OrderVarcharI18n.hqlOrder("te.dsTipoEvento", LocaleContextHolder.getLocale()));
    	
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
}