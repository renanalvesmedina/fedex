package com.mercurio.lms.portaria.model.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.portaria.model.Doca;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DocaDAO extends BaseCrudDao<Doca, Long>
{

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("terminal", FetchMode.JOIN);
		lazyFindPaginated.put("terminal.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("terminal.filial", FetchMode.JOIN);	
	}
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("terminal", FetchMode.JOIN);
		lazyFindById.put("terminal.filial", FetchMode.JOIN);	
		lazyFindById.put("terminal.filial.pessoa", FetchMode.JOIN);	
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Doca.class;
    }

    public List findCombo(Map criteria) {
    	DetachedCriteria dc = createDetachedCriteria();
    	ProjectionList pl = Projections.projectionList();
    		pl.add(Projections.property("nrDoca"))
    			.add(Projections.property("dsDoca"))
    			.add(Projections.property("idDoca"));
    	dc.setProjection(pl)
    		.createAlias("terminal","T")
    		.addOrder(Order.asc("nrDoca"))
    		.addOrder(Order.asc("dsDoca"));
    	if (criteria.get("terminal.idTerminal") != null)
    		dc.add(Restrictions.eq("T.idTerminal",Long.valueOf((String)criteria.get("terminal.idTerminal"))));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
   
    public boolean verificaVigencia(Doca doca){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	if (doca.getIdDoca() != null)
    		dc.add(Restrictions.ne("idDoca", doca.getIdDoca()));
    	
    	dc.add(Restrictions.eq("terminal.idTerminal", doca.getTerminal().getIdTerminal()));    	
    	dc.add(Restrictions.eq("nrDoca", doca.getNrDoca()));
    	JTVigenciaUtils.getDetachedVigencia(dc, doca.getDtVigenciaInicial(), doca.getDtVigenciaFinal());
    	
    	dc.setProjection(Projections.rowCount());
    	
    	return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
    }

    public boolean findDocaValidaVigencias(Long idDoca, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {		
    	DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idDoca",idDoca));
		
		dc.add(Restrictions.sqlRestriction("dt_Vigencia_Inicial <= '"+dtVigenciaInicial.toString()+"'"));
		dc.add(Restrictions.sqlRestriction("dt_Vigencia_Final >= '"+JTDateTimeUtils.maxYmd(dtVigenciaFinal).toString()+"'"));
		
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() > 0;
	}
    
    
    public List findDocaVigenteByTerminal(Long idTerminal){
    	return findDocaVigenteByTerminal(idTerminal, null);
    }
    
    public List findDocaVigenteByTerminal(Long idTerminal, Long idDoca){
        DetachedCriteria dc = createDetachedCriteria();
   	   
   	   YearMonthDay today = JTDateTimeUtils.getDataAtual();
   	   
   	   if (idTerminal != null)
   		   dc.add(Restrictions.eq("terminal.idTerminal", idTerminal));
		

		
   	   Criterion vigencia = Restrictions.sqlRestriction("dt_Vigencia_Final >= '"+today.toString()+"'");
   	   
   	   if (idDoca != null) {
   		   vigencia = Restrictions.or(
   				   			vigencia,
   				   			Restrictions.eq("id", idDoca));
   	   }
   	   dc.add(vigencia);
   	   dc.addOrder(Order.asc("dsDoca"));
   	     	     	   
   	   return findByDetachedCriteria(dc);
     }
    
    
    public Integer getRowCountDocasVigenteByTerminal(Long idTerminal) {
        DetachedCriteria dc = createDetachedCriteria()
        						.setProjection(Projections.rowCount());
   	   			
   	   YearMonthDay today = JTDateTimeUtils.getDataAtual();
   	   
   	   if (idTerminal != null)
   		   dc.add(Restrictions.eq("terminal.idTerminal", idTerminal));
   	   
   	   Criterion vigencia = 
			   	Restrictions.and(
			   			
			   			Restrictions.sqlRestriction("dt_Vigencia_Inicial <= '"+today.toString()+"'"),
			   			Restrictions.sqlRestriction("dt_Vigencia_Final >= '"+today.toString()+"'"));
			   				
   	   
   	   dc.add(vigencia);
   	   return (Integer)dc.getExecutableCriteria(getSession()).uniqueResult();
     }

}